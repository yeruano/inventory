package co.com.inventory.r2dbc.productsupplierrelationship.repository;

import co.com.inventory.r2dbc.productsupplierrelationship.data.ProductSupplierRelationshipData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductSupplierRelationshipRepository
        extends ReactiveCrudRepository<ProductSupplierRelationshipData, Long> {

    Flux<ProductSupplierRelationshipData> findAllByProductId(Long productId);
    Mono<ProductSupplierRelationshipData> findByProductIdAndSupplierId(Long productId, Long supplierId);
}
