package co.com.inventory.usecase.purchaseinvoice.helper;

import co.com.inventory.model.product.Product;
import co.com.inventory.model.product.gateways.ProductGateway;
import co.com.inventory.model.purchaseinvoice.Installment;
import co.com.inventory.model.purchaseinvoice.InvoiceDetail;
import co.com.inventory.model.purchaseinvoice.InvoiceItem;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoice;
import co.com.inventory.model.purchaseinvoice.gateways.InstallmentGateway;
import co.com.inventory.model.purchaseinvoice.gateways.InvoiceDetailGateway;
import co.com.inventory.model.purchaseinvoice.gateways.InvoiceItemGateway;
import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.model.supplier.gateways.SupplierGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.util.List;

@RequiredArgsConstructor
public class PurchaseInvoiceRetrievalHelper {

    private final SupplierGateway supplierGateway;
    private final ProductGateway productGateway;
    private final InvoiceDetailGateway invoiceDetailGateway;
    private final InvoiceItemGateway invoiceItemGateway;
    private final InstallmentGateway installmentGateway;

    public Mono<PurchaseInvoice> findPurchaseInvoiceById(Long invoiceId) {
        return invoiceDetailGateway.findById(invoiceId)
                .flatMap(this::findPurchaseInvoiceDetails);
    }

    public Mono<PurchaseInvoice> findPurchaseInvoiceDetails(InvoiceDetail invoiceDetail) {
        Mono<Supplier> supplierMono = supplierGateway.findById(invoiceDetail.getSupplierId());
        Mono<List<Installment>> installmentsMono = installmentGateway.findByInvoiceId(invoiceDetail.getId())
                .collectList();
        Mono<List<Product>> productsMono = fetchProductsWithDetails(invoiceDetail.getId(), supplierMono);

        return Mono.zip(supplierMono, installmentsMono, productsMono)
                .map(tuple -> buildPurchaseInvoice(tuple, invoiceDetail));
    }

    private Mono<List<Product>> fetchProductsWithDetails(Long invoiceId, Mono<Supplier> supplierMono) {
        return invoiceItemGateway.findByInvoiceId(invoiceId)
                .flatMap(invoiceItem -> productGateway.findById(invoiceItem.getProductId())
                        .zipWith(supplierMono)
                        .map(tuple2 -> buildProduct(tuple2, invoiceItem)))
                .collectList();
    }

    private Product buildProduct(Tuple2<Product, Supplier> tuple2, InvoiceItem invoiceItem) {
        Product product = tuple2.getT1();
        Supplier supplier = tuple2.getT2();
        return product.toBuilder()
                .purchasePrice(invoiceItem.getPrice())
                .amount(invoiceItem.getAmount())
                .suppliers(List.of(supplier))
                .build();
    }

    private PurchaseInvoice buildPurchaseInvoice(Tuple3<Supplier, List<Installment>, List<Product>> tuple,
                                                 InvoiceDetail invoiceDetail) {
        return PurchaseInvoice.builder()
                .supplier(tuple.getT1())
                .installments(tuple.getT2())
                .products(tuple.getT3())
                .invoice(invoiceDetail)
                .build();
    }
}
