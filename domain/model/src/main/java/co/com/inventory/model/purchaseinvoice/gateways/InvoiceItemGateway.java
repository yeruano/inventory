package co.com.inventory.model.purchaseinvoice.gateways;

import co.com.inventory.model.purchaseinvoice.InvoiceItem;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InvoiceItemGateway {

    Flux<InvoiceItem> findByInvoiceId(@NonNull Long invoiceId);
    Mono<InvoiceItem> save(@NonNull InvoiceItem invoiceItem);
}
