package co.com.inventory.r2dbc.purchaseinvoice;

import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.model.purchaseinvoice.Installment;
import co.com.inventory.model.purchaseinvoice.gateways.InstallmentGateway;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import co.com.inventory.r2dbc.purchaseinvoice.mapper.InstallmentMapper;
import co.com.inventory.r2dbc.purchaseinvoice.repository.SupplierPaymentHistoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.RESOURCE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class InstallmentAdapter implements InstallmentGateway {

    private final SupplierPaymentHistoryRepository supplierPaymentHistoryRepository;

    @Override
    public Mono<Installment> findById(@NonNull Long installmentId) {
        return supplierPaymentHistoryRepository.findById(installmentId)
                .map(InstallmentMapper::buildInstallment)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Flux<Installment> findByInvoiceId(@NonNull Long invoiceId) {
        return supplierPaymentHistoryRepository.findAllByInvoiceId(invoiceId)
                .map(InstallmentMapper::buildInstallment)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Transactional
    @Override
    public Flux<Installment> saveAll(@NonNull List<Installment> installments) {
        var paymentHistoryData = InstallmentMapper.buildSupplierPaymentHistoryDataList(installments);
        return supplierPaymentHistoryRepository.saveAll(paymentHistoryData)
                .map(InstallmentMapper::buildInstallment)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
    
    @Transactional
    @Override
    public Mono<Void> deleteById(@NonNull Long installmentId) {
        return supplierPaymentHistoryRepository.deleteById(installmentId)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Transactional
    @Override
    public Mono<Void> deleteByIds(@NonNull List<Long> installmentIds) {
        return supplierPaymentHistoryRepository.deleteAllById(installmentIds)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
}
