package co.com.inventory.usecase.purchaseinvoice;

import co.com.inventory.model.purchaseinvoice.PurchaseInvoice;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoiceQuery;
import co.com.inventory.model.purchaseinvoice.gateways.InvoiceDetailGateway;
import co.com.inventory.usecase.purchaseinvoice.helper.PurchaseInvoiceCreationHelper;
import co.com.inventory.usecase.purchaseinvoice.helper.PurchaseInvoiceRetrievalHelper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class PurchaseInvoiceUseCase {

    private final InvoiceDetailGateway invoiceDetailGateway;
    private final PurchaseInvoiceCreationHelper purchaseInvoiceCreationHelper;
    private final PurchaseInvoiceRetrievalHelper purchaseInvoiceRetrievalHelper;

    public Mono<PurchaseInvoice> findPurchaseInvoiceById(Long invoiceId) {
        return purchaseInvoiceRetrievalHelper.findPurchaseInvoiceById(invoiceId);
    }

    public Mono<List<PurchaseInvoice>> findPurchaseInvoicesByQuery(PurchaseInvoiceQuery purchaseInvoiceQuery) {
        return invoiceDetailGateway.findByQuery(purchaseInvoiceQuery)
                .flatMap(purchaseInvoiceRetrievalHelper::findPurchaseInvoiceDetails)
                .collectList();
    }

    public Mono<PurchaseInvoice> savePurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        return purchaseInvoiceCreationHelper.savePurchaseInvoice(purchaseInvoice);
    }
}
