package co.com.inventory.r2dbc.purchaseinvoice.repository;

import co.com.inventory.r2dbc.purchaseinvoice.data.SupplierPaymentHistoryData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SupplierPaymentHistoryRepository extends ReactiveCrudRepository<SupplierPaymentHistoryData, Long> {

    Flux<SupplierPaymentHistoryData> findAllByInvoiceId(Long invoiceId);
}
