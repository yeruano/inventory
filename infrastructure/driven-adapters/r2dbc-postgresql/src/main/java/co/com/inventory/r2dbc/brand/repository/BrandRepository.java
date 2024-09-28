package co.com.inventory.r2dbc.brand.repository;

import co.com.inventory.model.brand.BrandQuery;
import co.com.inventory.r2dbc.brand.data.BrandData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BrandRepository extends ReactiveCrudRepository<BrandData, Long> {

    @Query("SELECT * FROM brand " +
            "WHERE name ILIKE '%' || :#{#brandQuery.name} || '%' " +
            "AND description ILIKE '%' || :#{#brandQuery.description} || '%' " +
            "ORDER BY name ASC LIMIT :limit OFFSET :offset")
    Flux<BrandData> findAllBrandsByQuery(
            @Param("brandQuery") BrandQuery brandQuery,
            @Param("limit") Long limit,
            @Param("offset") Long offset
    );

    @Query("SELECT COUNT(*) FROM brand " +
            "WHERE name ILIKE '%' || :#{#brandQuery.name} || '%' " +
            "AND description ILIKE '%' || :#{#brandQuery.description} || '%' ")
    Mono<Long> countBrandsByQuery(@Param("brandQuery") BrandQuery brandQuery);
}
