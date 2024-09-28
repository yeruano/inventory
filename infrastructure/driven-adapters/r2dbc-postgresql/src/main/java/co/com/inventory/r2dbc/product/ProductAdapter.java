package co.com.inventory.r2dbc.product;

import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.model.product.Product;
import co.com.inventory.model.product.ProductQuery;
import co.com.inventory.model.product.gateways.ProductGateway;
import co.com.inventory.r2dbc.brand.BrandAdapter;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import co.com.inventory.r2dbc.product.mapper.ProductMapper;
import co.com.inventory.r2dbc.product.repository.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.RESOURCES_NOT_FOUND;
import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.RESOURCE_NOT_FOUND;

@Repository
@Transactional
@RequiredArgsConstructor
public class ProductAdapter implements ProductGateway {

    private final ProductRepository productRepository;
    private final BrandAdapter brandAdapter;
    private static final Long ZERO_PRODUCTS = 0L;

    @Override
    public Mono<Product> findById(@NonNull Long productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .map(ProductMapper::buildProduct)
                .flatMap(product -> brandAdapter.findById(product.getBrand().getId())
                        .map(brand -> product.toBuilder().brand(brand).build()))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Tuple2<List<Product>, ProductQuery>> findByQuery(@NonNull ProductQuery productQuery) {
        return findAllSupplierByQuery(productQuery)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCES_NOT_FOUND))))
                .collectList()
                .zipWith(countTotalElementsByQuery(productQuery).map(totalItems -> productQuery.toBuilder()
                        .totalItems(totalItems)
                        .totalPages(getTotalPages(totalItems, productQuery.getPageSize()))
                        .build()
                ));
    }

    @Override
    public Flux<Product> findAllProducts() {
        return productRepository.findAll()
                .map(ProductMapper::buildProduct)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Product> save(@NonNull Product product) {
        return productRepository.save(ProductMapper.buildProductData(product))
                .flatMap(productData -> productRepository.findById(productData.getId()))
                .map(ProductMapper::buildProduct)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Product> updateById(@NonNull Product product) {
        return productRepository.findById(product.getId())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .map(productData -> product.toBuilder().createdAt(productData.getCreatedAt().toString()).build())
                .flatMap(productFull -> productRepository.save(ProductMapper.buildProductData(product)))
                .flatMap(savedProductData -> productRepository.findById(savedProductData.getId()))
                .map(ProductMapper::buildProduct)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Void> deleteById(@NonNull Long productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .flatMap(productData -> productRepository.deleteById(productId))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Transactional
    @Override
    public Mono<Void> deleteByIds(@NonNull List<Long> productsId) {
        return Flux.fromIterable(productsId)
                .flatMap(this::deleteById)
                .then();
    }

    public Flux<Product> findAllSupplierByQuery(ProductQuery productQuery) {
        return productRepository.findAllProductsByQuery(productQuery, productQuery.getPageSize(),
                        getOffset(productQuery.getPageSize(), productQuery.getPageNumber()))
                .map(ProductMapper::buildProduct)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    public Mono<Long> countTotalElementsByQuery(@NonNull ProductQuery productQuery) {
        return productRepository.countProductsByQuery(productQuery)
                .switchIfEmpty(Mono.defer(() -> Mono.just(ZERO_PRODUCTS)))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    private Long getOffset(Long pageSize, Long pageNumber) {
        return (pageNumber - 1) * pageSize;
    }

    private Long getTotalPages(Long totalItems, Long pageSize) {
        return (totalItems % pageSize == 0) ? (totalItems / pageSize) : ((totalItems / pageSize) + 1);
    }
}
