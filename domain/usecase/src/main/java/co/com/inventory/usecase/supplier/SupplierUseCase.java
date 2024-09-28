package co.com.inventory.usecase.supplier;

import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.model.supplier.SupplierQuery;
import co.com.inventory.model.supplier.gateways.SupplierGateway;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@RequiredArgsConstructor
public class SupplierUseCase {

    private final SupplierGateway supplierGateway;

    public Mono<Supplier> findSupplierById(@NonNull Long supplierId) {
        return supplierGateway.findById(supplierId);
    }

    public Mono<Tuple2<List<Supplier>, SupplierQuery>> findSuppliersByQuery(@NonNull SupplierQuery supplierQuery) {
        return supplierGateway.findByQuery(supplierQuery);
    }

    public Mono<Supplier> saveSupplier(@NonNull Supplier supplier) {
        return supplierGateway.save(supplier);
    }

    public Mono<Supplier> updateSupplierById(@NonNull Supplier supplier) {
        return supplierGateway.updateById(supplier);
    }

    public Mono<Void> deleteSupplierById(@NonNull Long supplierId) {
        return supplierGateway.deleteById(supplierId);
    }
}
