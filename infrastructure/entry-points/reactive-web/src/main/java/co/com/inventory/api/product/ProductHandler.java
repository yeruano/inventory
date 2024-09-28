package co.com.inventory.api.product;

import co.com.inventory.api.commons.DataDTO;
import co.com.inventory.api.product.dto.ProductDTO;
import co.com.inventory.api.product.mapper.ProductMapper;
import co.com.inventory.api.util.RequestValidator;
import co.com.inventory.commons.utils.PathVariableConstant;
import co.com.inventory.commons.utils.RoleConstant;
import co.com.inventory.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> findProductById(ServerRequest serverRequest) {
        long productId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.PRODUCT_ID));
        return productUseCase.findProductById(productId)
                .map(ProductMapper::buildProductListDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @PreAuthorize("hasRole(" + RoleConstant.ROLE_ADMIN + ")")
    public Mono<ServerResponse> findAllProducts(ServerRequest serverRequest) {
        return productUseCase.findAllProducts()
                .map(ProductMapper::buildProductListDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Transactional
    public Mono<ServerResponse> updateProductById(ServerRequest serverRequest) {
        long productId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.PRODUCT_ID));
        return requestValidator.validateRequestBody(serverRequest, ProductDTO.class)
                .flatMap(productDTO -> requestValidator.validateBody(productDTO)
                        .map(validProductDTO -> validProductDTO.toBuilder().id(productId).build())
                        .map(ProductMapper::buildProduct)
                        .flatMap(productUseCase::updateProductById)
                        .map(ProductMapper::buildProductDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(ServerResponse.ok()::bodyValue)
                );
    }

    @Transactional
    public Mono<ServerResponse> deleteProductById(ServerRequest serverRequest) {
        long productId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.PRODUCT_ID));
        return productUseCase.deleteProductById(productId).then(ServerResponse.noContent().build());
    }
}
