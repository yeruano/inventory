package co.com.inventory.api.brand;

import co.com.inventory.api.brand.dto.BrandDTO;
import co.com.inventory.api.brand.mapper.BrandMapper;
import co.com.inventory.api.commons.DataDTO;
import co.com.inventory.api.config.ApplicationRoute;
import co.com.inventory.api.util.RequestValidator;
import co.com.inventory.commons.utils.PathVariableConstant;
import co.com.inventory.usecase.brand.BrandUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class BrandHandler {

    private final BrandUseCase brandUseCase;
    private final RequestValidator requestValidator;
    private final ApplicationRoute applicationRoute;

    public Mono<ServerResponse> findBrandById(ServerRequest serverRequest) {
        long brandId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.BRAND_ID));
        return brandUseCase.findBrandById(brandId)
                .map(BrandMapper::buildBrandListDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findBrandsByQuery(ServerRequest serverRequest) {
        return brandUseCase.findBrandsByQuery(BrandMapper.buildBrandQuery(serverRequest))
                .map(BrandMapper::buildBrandListDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> saveBrand(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, BrandDTO.class)
                .flatMap(brandDTO -> requestValidator.validateBody(brandDTO)
                        .map(BrandMapper::buildBrand)
                        .flatMap(brandUseCase::saveBrand)
                        .map(BrandMapper::buildBrandDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(brandDTO1 -> ServerResponse.created(createURI(brandDTO1.getData().getId()))
                                .bodyValue(brandDTO1))
                );
    }

    public Mono<ServerResponse> updateBrandById(ServerRequest serverRequest) {
        long brandId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.BRAND_ID));
        return requestValidator.validateRequestBody(serverRequest, BrandDTO.class)
                .flatMap(brandDTO -> requestValidator.validateBody(brandDTO)
                        .map(validBrandDTO -> validBrandDTO.toBuilder().id(brandId).build())
                        .map(BrandMapper::buildBrand)
                        .flatMap(brandUseCase::updateBrandById)
                        .map(BrandMapper::buildBrandDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(ServerResponse.ok()::bodyValue)
                );
    }

    public Mono<ServerResponse> deleteBrandById(ServerRequest serverRequest) {
        Long brandId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.BRAND_ID));
        return brandUseCase.deleteBrandById(brandId).then(ServerResponse.noContent().build());
    }

    private URI createURI(Long path) {
        return URI.create(applicationRoute.getBrands() + "/" + path);
    }
}
