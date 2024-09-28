package co.com.inventory.api.purchaseinvoice.handler;

import co.com.inventory.api.commons.DataDTO;
import co.com.inventory.api.purchaseinvoice.dto.PurchaseInvoiceDTO;
import co.com.inventory.api.purchaseinvoice.dto.PurchaseInvoiceQueryDTO;
import co.com.inventory.api.purchaseinvoice.mapper.PurchaseInvoiceMapper;
import co.com.inventory.api.util.RequestValidator;
import co.com.inventory.commons.utils.PathVariableConstant;
import co.com.inventory.usecase.purchaseinvoice.PurchaseInvoiceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PurchaseInvoiceHandler {

    private final PurchaseInvoiceUseCase purchaseInvoiceUseCase;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> findPurchaseInvoiceById(ServerRequest serverRequest) {
        long invoiceId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.INVOICE_ID));
        return purchaseInvoiceUseCase.findPurchaseInvoiceById(invoiceId)
                .map(PurchaseInvoiceMapper::buildPurchaseInvoiceDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Transactional
    public Mono<ServerResponse> savePurchaseInvoice(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, PurchaseInvoiceDTO.class)
                .flatMap(purchaseInvoiceDTO -> requestValidator.validateBody(purchaseInvoiceDTO)
                        .map(PurchaseInvoiceMapper::buildPurchaseInvoice)
                        .flatMap(purchaseInvoiceUseCase::savePurchaseInvoice)
                        .map(PurchaseInvoiceMapper::buildPurchaseInvoiceDTO)
                        .flatMap(ServerResponse.ok()::bodyValue));
    }

    @Transactional
    public Mono<ServerResponse> findPurchaseInvoice(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, PurchaseInvoiceQueryDTO.class)
                .flatMap(purchaseInvoiceQueryDTO -> requestValidator.validateBody(purchaseInvoiceQueryDTO)
                        .map(PurchaseInvoiceMapper::buildPurchaseInvoiceQuery)
                        .flatMap(purchaseInvoiceUseCase::findPurchaseInvoicesByQuery)
                        .map(PurchaseInvoiceMapper::buildPurchaseInvoiceDTOS)
                        .flatMap(ServerResponse.ok()::bodyValue));
    }
}
