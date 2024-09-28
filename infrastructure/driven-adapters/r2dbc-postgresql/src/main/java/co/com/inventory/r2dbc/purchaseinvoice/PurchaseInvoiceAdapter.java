package co.com.inventory.r2dbc.purchaseinvoice;

import co.com.inventory.model.product.Product;
import co.com.inventory.model.purchaseinvoice.InvoiceItem;
import co.com.inventory.model.purchaseinvoice.gateways.InvoiceItemGateway;
import co.com.inventory.model.purchaseinvoice.gateways.ProductInvoiceGateway;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import co.com.inventory.r2dbc.product.repository.ProductRepository;
import co.com.inventory.r2dbc.product.mapper.ProductMapper;
import co.com.inventory.r2dbc.purchaseinvoice.mapper.PurchaseInvoiceMapper;
import co.com.inventory.r2dbc.purchaseinvoice.repository.InvoiceItemRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PurchaseInvoiceAdapter implements InvoiceItemGateway, ProductInvoiceGateway {

    private final InvoiceItemRepository invoiceItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Flux<InvoiceItem> findByInvoiceId(@NonNull Long invoiceId) {
        return invoiceItemRepository.findAllByInvoiceId(invoiceId)
                .map(PurchaseInvoiceMapper::buildInvoiceItem)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Transactional
    @Override
    public Flux<Product> findProductsByInvoiceId(@NonNull Long invoiceId) {
        return invoiceItemRepository.findAllByInvoiceId(invoiceId)
                .flatMap(invoiceDetailData -> productRepository.findById(invoiceDetailData.getProductId()))
                .map(ProductMapper::buildProduct)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Transactional
    @Override
    public Mono<InvoiceItem> save(@NonNull InvoiceItem invoiceItem) {
        var purchaseInvoiceDetailData = PurchaseInvoiceMapper.buildPurchaseInvoiceDetailData(invoiceItem);
        return invoiceItemRepository.save(purchaseInvoiceDetailData)
                .map(PurchaseInvoiceMapper::buildInvoiceItem)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
}
