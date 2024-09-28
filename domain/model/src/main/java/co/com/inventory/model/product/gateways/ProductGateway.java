package co.com.inventory.model.product.gateways;

import co.com.inventory.model.product.Product;
import co.com.inventory.model.product.ProductQuery;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public interface ProductGateway {

    Mono<Product> findById(@NonNull Long productId);
    Mono<Tuple2<List<Product>, ProductQuery>> findByQuery(@NonNull ProductQuery productQuery);
    Flux<Product> findAllProducts();
    Mono<Product> save(@NonNull Product product);
    Mono<Product> updateById(@NonNull Product product);
    Mono<Void> deleteById(@NonNull Long productId);
    Mono<Void> deleteByIds(@NonNull List<Long> productsId);
}
