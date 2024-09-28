package co.com.inventory.usecase.brand;

import co.com.inventory.model.brand.Brand;
import co.com.inventory.model.brand.BrandQuery;
import co.com.inventory.model.brand.gateways.BrandGateway;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@RequiredArgsConstructor
public class BrandUseCase {

    private final BrandGateway brandGateway;

    public Mono<Brand> findBrandById(@NonNull Long brandId) {
        return brandGateway.findById(brandId);
    }

    public Mono<Tuple2<List<Brand>, BrandQuery>> findBrandsByQuery(@NonNull BrandQuery brandQuery) {
        return brandGateway.findByQuery(brandQuery);
    }

    public Mono<Brand> saveBrand(@NonNull Brand brand) {
        return brandGateway.save(brand);
    }

    public Mono<Brand> updateBrandById(@NonNull Brand brand) {
        return brandGateway.updateById(brand);
    }

    public Mono<Void> deleteBrandById(@NonNull Long brandId) {
        return brandGateway.deleteById(brandId);
    }
}
