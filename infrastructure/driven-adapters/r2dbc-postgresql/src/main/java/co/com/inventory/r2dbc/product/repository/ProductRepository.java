package co.com.inventory.r2dbc.product.repository;

import co.com.inventory.model.product.ProductQuery;
import co.com.inventory.r2dbc.product.data.ProductData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductData, Long> {

    @Query("SELECT * FROM product " +
            "WHERE name ILIKE '%' || :#{#productQuery.name} || '%' " +
            "AND description ILIKE '%' || :#{#productQuery.description} || '%' " +
            "ORDER BY description ASC LIMIT :limit OFFSET :offset")
    Flux<ProductData> findAllProductsByQuery(
            @Param("productQuery") ProductQuery productQuery,
            @Param("limit") Long limit,
            @Param("offset") Long offset
    );

    @Query("SELECT COUNT(*) FROM product " +
            "WHERE name ILIKE '%' || :#{#productQuery.name} || '%' " +
            "AND description ILIKE '%' || :#{#productQuery.description} || '%' ")
    Mono<Long> countProductsByQuery(@Param("productQuery") ProductQuery productQuery);
}
