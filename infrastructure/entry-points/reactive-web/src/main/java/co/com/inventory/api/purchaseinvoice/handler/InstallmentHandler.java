package co.com.inventory.api.purchaseinvoice.handler;

import co.com.inventory.api.commons.DataDTO;
import co.com.inventory.api.purchaseinvoice.dto.PurchaseInvoiceDTO;
import co.com.inventory.api.purchaseinvoice.mapper.PurchaseInvoiceMapper;
import co.com.inventory.api.util.RequestValidator;
import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoice;
import co.com.inventory.usecase.installment.InstallmentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InstallmentHandler {

    private final InstallmentUseCase installmentUseCase;
    private final RequestValidator requestValidator;

    @Transactional
    public Mono<ServerResponse> saveInstallments(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, PurchaseInvoiceDTO.class)
                .flatMap(purchaseInvoiceDTO -> requestValidator.validateBody(purchaseInvoiceDTO)
                        .map(PurchaseInvoiceMapper::buildPurchaseInvoice)
                        .map(this::validateInstallments)
                        .flatMap(installmentUseCase::saveInstallments)
                        .map(PurchaseInvoiceMapper::buildPurchaseInvoiceDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(ServerResponse.ok()::bodyValue));
    }

    @Transactional
    public Mono<ServerResponse> updateInstallment(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, PurchaseInvoiceDTO.class)
                .flatMap(purchaseInvoiceDTO -> requestValidator.validateBody(purchaseInvoiceDTO)
                        .map(PurchaseInvoiceMapper::buildPurchaseInvoice)
                        .map(this::validateInstallments)
                        .flatMap(installmentUseCase::updateInstallment)
                        .map(PurchaseInvoiceMapper::buildPurchaseInvoiceDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(ServerResponse.ok()::bodyValue));
    }

    @Transactional
    public Mono<ServerResponse> deleteInstallments(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, PurchaseInvoiceDTO.class)
                .flatMap(purchaseInvoiceDTO -> requestValidator.validateBody(purchaseInvoiceDTO)
                        .map(PurchaseInvoiceMapper::buildPurchaseInvoice)
                        .map(this::validateInstallments)
                        .flatMap(installmentUseCase::deleteInstallments)
                        .then(ServerResponse.noContent().build()));
    }

    private PurchaseInvoice validateInstallments(PurchaseInvoice purchaseInvoice) {
        if (purchaseInvoice.getInstallments().isEmpty()) {
            throw new TechnicalException(TechnicalExceptionMessage.LIST_OF_INSTALLMENTS_CANNOT_BE_EMPTY);
        }

        return purchaseInvoice;
    }
}
