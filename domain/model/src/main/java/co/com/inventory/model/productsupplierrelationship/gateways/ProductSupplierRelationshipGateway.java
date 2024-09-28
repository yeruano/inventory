package co.com.inventory.model.productsupplierrelationship.gateways;

import co.com.inventory.model.productsupplierrelationship.ProductSupplierRelationship;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductSupplierRelationshipGateway {

    Flux<ProductSupplierRelationship> findAllByProductId(@NonNull Long productId);
    Mono<ProductSupplierRelationship> findByProductIdAndSupplierId(@NonNull ProductSupplierRelationship productSupplierRelationship);
    Mono<ProductSupplierRelationship> findOrSave(@NonNull ProductSupplierRelationship productSupplierRelationship);
    Mono<ProductSupplierRelationship> save(@NonNull ProductSupplierRelationship productSupplierRelationship);
}
