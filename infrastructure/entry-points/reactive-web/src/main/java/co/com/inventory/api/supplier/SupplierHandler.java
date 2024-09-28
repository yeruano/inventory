package co.com.inventory.api.supplier;

import co.com.inventory.api.commons.DataDTO;
import co.com.inventory.api.config.ApplicationRoute;
import co.com.inventory.api.supplier.dto.SupplierDTO;
import co.com.inventory.api.supplier.mapper.SupplierMapper;
import co.com.inventory.api.util.RequestValidator;
import co.com.inventory.commons.utils.PathVariableConstant;
import co.com.inventory.usecase.supplier.SupplierUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class SupplierHandler {

    private final SupplierUseCase supplierUseCase;
    private final RequestValidator requestValidator;
    private final ApplicationRoute applicationRoute;

    public Mono<ServerResponse> findSupplierById(ServerRequest serverRequest) {
        long supplierId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.SUPPLIER_ID));
        return supplierUseCase.findSupplierById(supplierId)
                .map(SupplierMapper::buildSupplierListDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findSuppliersByQuery(ServerRequest serverRequest) {
        return supplierUseCase.findSuppliersByQuery(SupplierMapper.buildSupplierQuery(serverRequest))
                .map(SupplierMapper::buildSupplierListDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> saveSupplier(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, SupplierDTO.class)
                .flatMap(supplierDTO -> requestValidator.validateBody(supplierDTO)
                        .map(SupplierMapper::buildSupplier)
                        .flatMap(supplierUseCase::saveSupplier)
                        .map(SupplierMapper::buildSupplierDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(supplierDTO1 -> ServerResponse.created(createURI(supplierDTO1.getData().getId()))
                                .bodyValue(supplierDTO1)));
    }

    public Mono<ServerResponse> updateSupplierById(ServerRequest serverRequest) {
        long supplierId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.SUPPLIER_ID));
        return requestValidator.validateRequestBody(serverRequest, SupplierDTO.class)
                .flatMap(supplierDTO -> requestValidator.validateBody(supplierDTO)
                        .map(validSupplierDTO -> validSupplierDTO.toBuilder().id(supplierId).build())
                        .map(SupplierMapper::buildSupplier)
                        .flatMap(supplierUseCase::updateSupplierById)
                        .map(SupplierMapper::buildSupplierDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(ServerResponse.ok()::bodyValue));
    }

    public Mono<ServerResponse> deleteSupplierById(ServerRequest serverRequest) {
        long supplierId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.SUPPLIER_ID));
        return supplierUseCase.deleteSupplierById(supplierId).then(ServerResponse.noContent().build());
    }

    private URI createURI(Long path) {
        return URI.create(applicationRoute.getSuppliers() + "/" + path);
    }
}
