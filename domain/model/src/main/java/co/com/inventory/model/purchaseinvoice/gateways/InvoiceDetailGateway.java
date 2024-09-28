package co.com.inventory.model.purchaseinvoice.gateways;

import co.com.inventory.model.purchaseinvoice.InvoiceDetail;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoiceQuery;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InvoiceDetailGateway {

    Mono<InvoiceDetail> findById(@NonNull Long invoiceId);
    Flux<InvoiceDetail> findByQuery(@NonNull PurchaseInvoiceQuery purchaseInvoiceQuery);
    Mono<InvoiceDetail> save(@NonNull InvoiceDetail invoiceDetail);
}
