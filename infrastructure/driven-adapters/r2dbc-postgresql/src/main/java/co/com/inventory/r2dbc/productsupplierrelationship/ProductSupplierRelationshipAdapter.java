package co.com.inventory.r2dbc.productsupplierrelationship;

import co.com.inventory.model.productsupplierrelationship.ProductSupplierRelationship;
import co.com.inventory.model.productsupplierrelationship.gateways.ProductSupplierRelationshipGateway;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import co.com.inventory.r2dbc.productsupplierrelationship.mapper.ProductSupplierRelationshipMapper;
import co.com.inventory.r2dbc.productsupplierrelationship.repository.ProductSupplierRelationshipRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Transactional
@RequiredArgsConstructor
public class ProductSupplierRelationshipAdapter implements ProductSupplierRelationshipGateway {

    private final ProductSupplierRelationshipRepository productSupplierRelationshipRepository;

    @Override
    public Flux<ProductSupplierRelationship> findAllByProductId(@NonNull Long productId) {
        return productSupplierRelationshipRepository.findAllByProductId(productId)
                .map(ProductSupplierRelationshipMapper::buildProductSupplierRelationship)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<ProductSupplierRelationship> findByProductIdAndSupplierId
            (@NonNull ProductSupplierRelationship productSupplierRelationship) {
        var productId = productSupplierRelationship.getProductId();
        var supplierId = productSupplierRelationship.getSupplierId();
        return productSupplierRelationshipRepository.findByProductIdAndSupplierId(productId, supplierId)
                .map(ProductSupplierRelationshipMapper::buildProductSupplierRelationship)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<ProductSupplierRelationship> findOrSave(@NonNull ProductSupplierRelationship relationship) {
        return findByProductIdAndSupplierId(relationship)
                .switchIfEmpty(Mono.defer(() -> save(relationship)))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<ProductSupplierRelationship> save(@NonNull ProductSupplierRelationship productSupplierRelationship) {
        var productSupplierRelationshipData = ProductSupplierRelationshipMapper.buildProductSupplierRelationshipData(productSupplierRelationship);
        return productSupplierRelationshipRepository.save(productSupplierRelationshipData)
                .map(ProductSupplierRelationshipMapper::buildProductSupplierRelationship)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
}
