package co.com.inventory.model.brand.gateways;

import co.com.inventory.model.brand.Brand;
import co.com.inventory.model.brand.BrandQuery;
import lombok.NonNull;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public interface BrandGateway {

    Mono<Brand> findById(@NonNull Long brandId);
    Mono<Tuple2<List<Brand>, BrandQuery>> findByQuery(@NonNull BrandQuery brandQuery);
    Mono<Brand> findOrSave(@NonNull Brand brand);
    Mono<Brand> save(@NonNull Brand brand);
    Mono<Brand> updateById(@NonNull Brand brand);
    Mono<Void> deleteById(@NonNull Long brandId);
}
