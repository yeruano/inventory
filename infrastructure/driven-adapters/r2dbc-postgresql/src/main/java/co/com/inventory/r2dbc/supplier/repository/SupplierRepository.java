package co.com.inventory.r2dbc.supplier.repository;

import co.com.inventory.model.supplier.SupplierQuery;
import co.com.inventory.r2dbc.supplier.data.SupplierData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SupplierRepository extends ReactiveCrudRepository<SupplierData, Long> {

    @Query("SELECT * FROM supplier " +
            "WHERE supplier_name ILIKE '%' || :#{#supplierQuery.supplierName} || '%' " +
            "AND description ILIKE '%' || :#{#supplierQuery.description} || '%' " +
            "ORDER BY supplier_name ASC LIMIT :limit OFFSET :offset")
    Flux<SupplierData> findAllSuppliersByQuery(
            @Param("supplierQuery") SupplierQuery supplierQuery,
            @Param("limit") Long limit,
            @Param("offset") Long offset
    );

    @Query("SELECT COUNT(*) FROM supplier " +
            "WHERE supplier_name ILIKE '%' || :#{#supplierQuery.supplierName} || '%' " +
            "AND description ILIKE '%' || :#{#supplierQuery.description} || '%' ")
    Mono<Long> countSuppliersByQuery(@Param("supplierQuery") SupplierQuery supplierQuery);
}
