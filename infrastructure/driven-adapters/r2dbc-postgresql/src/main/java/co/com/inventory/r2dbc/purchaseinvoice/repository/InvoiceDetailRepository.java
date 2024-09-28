package co.com.inventory.r2dbc.purchaseinvoice.repository;

import co.com.inventory.r2dbc.purchaseinvoice.data.InvoiceDetailData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface InvoiceDetailRepository extends ReactiveCrudRepository<InvoiceDetailData, Long> {

    Flux<InvoiceDetailData> findByEntryDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("INSERT INTO purchase_invoice(url_photo, purchase_date, entry_date, total, amount_paid, state, supplier_id) " +
            "VALUES (" +
            ":#{#invoiceDetailData.urlPhoto}, " +
            ":#{#invoiceDetailData.purchaseDate}, " +
            ":#{#invoiceDetailData.entryDate}, " +
            ":#{#invoiceDetailData.total}, " +
            ":#{#invoiceDetailData.amountPaid}, " +
            ":#{#invoiceDetailData.state}::public.invoice_status_enum, " +
            ":#{#invoiceDetailData.supplierId}) " +
            "RETURNING *")
    Mono<InvoiceDetailData> saveInvoiceDetail(@Param("invoiceDetailData") InvoiceDetailData invoiceDetailData);
}
