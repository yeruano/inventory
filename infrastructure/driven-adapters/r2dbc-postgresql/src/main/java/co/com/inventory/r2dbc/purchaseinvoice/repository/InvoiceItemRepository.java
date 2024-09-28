package co.com.inventory.r2dbc.purchaseinvoice.repository;

import co.com.inventory.r2dbc.purchaseinvoice.data.InvoiceItemData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface InvoiceItemRepository extends ReactiveCrudRepository<InvoiceItemData, Long> {

    Flux<InvoiceItemData> findAllByInvoiceId(Long invoiceId);
}
