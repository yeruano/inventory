package co.com.inventory.usecase.purchaseinvoice.helper;

import co.com.inventory.model.brand.gateways.BrandGateway;
import co.com.inventory.model.product.Product;
import co.com.inventory.model.product.gateways.ProductGateway;
import co.com.inventory.model.productsupplierrelationship.ProductSupplierRelationship;
import co.com.inventory.model.productsupplierrelationship.gateways.ProductSupplierRelationshipGateway;
import co.com.inventory.model.purchaseinvoice.Installment;
import co.com.inventory.model.purchaseinvoice.InvoiceDetail;
import co.com.inventory.model.purchaseinvoice.InvoiceItem;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoice;
import co.com.inventory.model.purchaseinvoice.gateways.InstallmentGateway;
import co.com.inventory.model.purchaseinvoice.gateways.InvoiceDetailGateway;
import co.com.inventory.model.purchaseinvoice.gateways.InvoiceItemGateway;
import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.model.supplier.gateways.SupplierGateway;
import co.com.inventory.usecase.purchaseinvoice.PurchaseInvoiceMapper;
import co.com.inventory.usecase.purchaseinvoice.util.PurchaseInvoiceUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class PurchaseInvoiceCreationHelper {

    private final BrandGateway brandGateway;
    private final SupplierGateway supplierGateway;
    private final ProductGateway productGateway;
    private final InstallmentGateway installmentGateway;
    private final InvoiceDetailGateway invoiceDetailGateway;
    private final InvoiceItemGateway invoiceItemGateway;
    private final ProductSupplierRelationshipGateway productSupplierRelationshipGateway;
    private final PurchaseInvoiceUtil purchaseInvoiceUtil;
    private final PurchaseInvoiceMapper purchaseInvoiceMapper;

    public Mono<PurchaseInvoice> savePurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        return supplierGateway.findOrSave(purchaseInvoice.getSupplier())
                .flatMap(supplier -> saveInvoiceAndInstallments(purchaseInvoice, supplier))
                .flatMap(tuple3 -> saveInvoiceProducts(tuple3, purchaseInvoice));
    }

    private Mono<Tuple3<Supplier, InvoiceDetail, List<Installment>>> saveInvoiceAndInstallments(
            PurchaseInvoice purchaseInvoice, Supplier supplier) {
        InvoiceDetail invoiceDetail = purchaseInvoiceUtil.validateInstallmentsForNewInvoice(purchaseInvoice, supplier);
        return invoiceDetailGateway.save(invoiceDetail)
                .flatMap(savedInvoiceDetail -> saveInstallments(purchaseInvoice, savedInvoiceDetail, supplier));
    }

    private Mono<Tuple3<Supplier, InvoiceDetail, List<Installment>>> saveInstallments(
            PurchaseInvoice purchaseInvoice, InvoiceDetail savedInvoiceDetail, Supplier supplier) {
        var installments = purchaseInvoiceMapper.buildInstallments(purchaseInvoice, savedInvoiceDetail);
        return installmentGateway.saveAll(installments)
                .collectList()
                .map(savedInstallments -> Tuples.of(supplier, savedInvoiceDetail, savedInstallments));
    }

    private Mono<PurchaseInvoice> saveInvoiceProducts(Tuple3<Supplier, InvoiceDetail, List<Installment>> tuple3,
                                                      PurchaseInvoice purchaseInvoice) {
        return Flux.fromIterable(purchaseInvoice.getProducts())
                .flatMap(productToSave -> brandGateway.findOrSave(productToSave.getBrand())
                        .map(savedBrand -> productToSave.toBuilder().brand(savedBrand).build())
                        .zipWhen(this::findOrSaveProduct)
                        .map(Tuple2::getT2)
                        .flatMap(savedProduct -> saveProductDetails(savedProduct, productToSave, tuple3.getT2(), tuple3.getT1())))
                .collectList()
                .map(products -> purchaseInvoiceMapper.buildPurchaseInvoice(products, tuple3));
    }

    private Mono<Product> findOrSaveProduct(Product productToSave) {
        return Mono.justOrEmpty(productToSave.getId())
                .filter(Objects::nonNull)
                .flatMap(productGateway::findById)
                .flatMap(productFromStorage -> updateProduct(productToSave, productFromStorage))
                .switchIfEmpty(Mono.defer(() -> productGateway.save(productToSave)))
                .map(productFromStorage -> productFromStorage.toBuilder().brand(productToSave.getBrand()).build());
    }

    private Mono<Product> updateProduct(Product productToSave, Product productFromStorage) {
        var adjustedProduct = purchaseInvoiceUtil.calculateAmountAndPurchasePrice(productToSave, productFromStorage);
        return productGateway.save(adjustedProduct);
    }

    private Mono<Product> saveProductDetails(Product savedProduct, Product productToSave, InvoiceDetail invoiceDetail,
                                             Supplier supplier) {
        return Mono.zip(
                saveInvoiceItem(savedProduct, invoiceDetail, productToSave),
                saveProductSupplierRelationship(savedProduct, supplier)
        ).thenReturn(savedProduct);
    }

    private Mono<Product> saveInvoiceItem(Product savedProduct, InvoiceDetail invoiceDetail, Product productToSave) {
        InvoiceItem invoiceItem = InvoiceItem.builder()
                .price(productToSave.getPurchasePrice())
                .amount(productToSave.getAmount())
                .invoiceId(invoiceDetail.getId())
                .productId(savedProduct.getId())
                .build();

        return invoiceItemGateway.save(invoiceItem).thenReturn(savedProduct);
    }

    private Mono<Product> saveProductSupplierRelationship(Product product, Supplier supplier) {
        var productSupplierRelationship = ProductSupplierRelationship.builder()
                .productId(product.getId())
                .supplierId(supplier.getId())
                .build();

        return productSupplierRelationshipGateway.findOrSave(productSupplierRelationship)
                .thenReturn(product);
    }
}
