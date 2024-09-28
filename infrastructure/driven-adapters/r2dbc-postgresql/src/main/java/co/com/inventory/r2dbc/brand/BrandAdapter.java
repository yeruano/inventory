package co.com.inventory.r2dbc.brand;

import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.model.brand.Brand;
import co.com.inventory.model.brand.BrandQuery;
import co.com.inventory.model.brand.gateways.BrandGateway;
import co.com.inventory.r2dbc.brand.mapper.BrandMapper;
import co.com.inventory.r2dbc.brand.repository.BrandRepository;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Objects;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.RESOURCES_NOT_FOUND;
import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.RESOURCE_NOT_FOUND;

@Repository
@Transactional
@RequiredArgsConstructor
public class BrandAdapter implements BrandGateway {

    private final BrandRepository brandRepository;
    private static final Long ZERO_BRANDS = 0L;

    @Override
    public Mono<Brand> findById(@NonNull Long brandId) {
        return brandRepository.findById(brandId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .map(BrandMapper::buildBrand)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Tuple2<List<Brand>, BrandQuery>> findByQuery(@NonNull BrandQuery brandQuery) {
        return findAllBrandByQuery(brandQuery)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCES_NOT_FOUND))))
                .collectList()
                .zipWith(countTotalElementsByQuery(brandQuery).map(totalItems -> brandQuery.toBuilder()
                        .totalItems(totalItems)
                        .totalPages(getTotalPages(totalItems, brandQuery.getPageSize()))
                        .build()));
    }

    @Override
    public Mono<Brand> findOrSave(@NonNull Brand brand) {
        return Mono.justOrEmpty(brand.getId())
                .filter(Objects::nonNull)
                .flatMap(this::findById)
                .switchIfEmpty(Mono.defer(() -> save(brand)))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Brand> save(@NonNull Brand brand) {
        return brandRepository.save(BrandMapper.buildBrandData(brand))
                .map(BrandMapper::buildBrand)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Brand> updateById(@NonNull Brand brand) {
        return brandRepository.findById(brand.getId())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .flatMap(brandFromDatabase -> brandRepository.save(BrandMapper.buildBrandData(brand)))
                .map(BrandMapper::buildBrand)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Void> deleteById(@NonNull Long brandId) {
        return brandRepository.findById(brandId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .flatMap(brandFromDatabase -> brandRepository.deleteById(brandId))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    public Flux<Brand> findAllBrandByQuery(BrandQuery brandQuery) {
        return brandRepository.findAllBrandsByQuery(brandQuery, brandQuery.getPageSize(),
                        getOffset(brandQuery.getPageSize(), brandQuery.getPageNumber()))
                .map(BrandMapper::buildBrand)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    public Mono<Long> countTotalElementsByQuery(@NonNull BrandQuery brandQuery) {
        return brandRepository.countBrandsByQuery(brandQuery)
                .switchIfEmpty(Mono.defer(() -> Mono.just(ZERO_BRANDS)))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    private Long getOffset(Long pageSize, Long pageNumber) {
        return (pageNumber - 1) * pageSize;
    }

    private Long getTotalPages(Long totalItems, Long pageSize) {
        return (totalItems % pageSize == 0) ? (totalItems / pageSize) : ((totalItems / pageSize) + 1);
    }
}
