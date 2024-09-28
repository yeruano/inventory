package co.com.inventory.model.supplier.gateways;

import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.model.supplier.SupplierQuery;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public interface SupplierGateway {

    Mono<Supplier> findById(@NonNull Long supplierId);
    Flux<Supplier> findByIds(@NonNull List<Long> listSupplierId);
    Mono<Tuple2<List<Supplier>, SupplierQuery>> findByQuery(@NonNull SupplierQuery supplierQuery);
    Mono<Supplier> findOrSave(@NonNull Supplier supplier);
    Mono<Supplier> save(@NonNull Supplier supplier);
    Mono<Supplier> updateById(@NonNull Supplier supplier);
    Mono<Void> deleteById(@NonNull Long supplierId);
}
