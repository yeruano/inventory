package co.com.inventory.r2dbc.purchaseinvoice;

import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.model.purchaseinvoice.InvoiceDetail;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoiceQuery;
import co.com.inventory.model.purchaseinvoice.gateways.InvoiceDetailGateway;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import co.com.inventory.r2dbc.purchaseinvoice.mapper.InvoiceDetailMapper;
import co.com.inventory.r2dbc.purchaseinvoice.repository.InvoiceDetailRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.RESOURCE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class InvoiceDetailAdapter implements InvoiceDetailGateway {

    private final InvoiceDetailRepository invoiceDetailRepository;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public Mono<InvoiceDetail> findById(@NonNull Long invoiceId) {
        return invoiceDetailRepository.findById(invoiceId)
                .map(InvoiceDetailMapper::buildInvoiceDetail)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Transactional
    @Override
    public Flux<InvoiceDetail> findByQuery(@NonNull PurchaseInvoiceQuery purchaseInvoiceQuery) {
        var startDate = LocalDateTime.parse(purchaseInvoiceQuery.getStartDate(), DATE_TIME_FORMATTER);
        var endDate = LocalDateTime.parse(purchaseInvoiceQuery.getEndDate(), DATE_TIME_FORMATTER);
        return invoiceDetailRepository.findByEntryDateBetween(startDate, endDate)
                .map(InvoiceDetailMapper::buildInvoiceDetail)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Transactional
    @Override
    public Mono<InvoiceDetail> save(@NonNull InvoiceDetail invoiceDetail) {
        var purchaseInvoiceData = InvoiceDetailMapper.buildInvoiceDetailData(invoiceDetail);
        return invoiceDetailRepository.saveInvoiceDetail(purchaseInvoiceData)
                .map(InvoiceDetailMapper::buildInvoiceDetail)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
}
