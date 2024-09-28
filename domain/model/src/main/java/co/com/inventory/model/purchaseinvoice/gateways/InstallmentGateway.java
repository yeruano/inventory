package co.com.inventory.model.purchaseinvoice.gateways;

import co.com.inventory.model.purchaseinvoice.Installment;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InstallmentGateway {

    Mono<Installment> findById(@NonNull Long installmentId);
    Flux<Installment> findByInvoiceId(@NonNull Long invoiceId);
    Flux<Installment> saveAll(@NonNull List<Installment> installments);
    Mono<Void> deleteById(@NonNull Long installmentId);
    Mono<Void> deleteByIds(@NonNull List<Long> installmentIds);
}
