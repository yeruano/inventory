package co.com.inventory.r2dbc.supplier;

import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.model.supplier.SupplierQuery;
import co.com.inventory.model.supplier.gateways.SupplierGateway;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import co.com.inventory.r2dbc.supplier.mapper.SupplierMapper;
import co.com.inventory.r2dbc.supplier.repository.SupplierRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Objects;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.RESOURCES_NOT_FOUND;
import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.RESOURCE_NOT_FOUND;

@Repository
@Transactional
@RequiredArgsConstructor
public class SupplierAdapter implements SupplierGateway {

    private final SupplierRepository supplierRepository;
    private static final Long ZERO_SUPPLIERS = 0L;

    @Override
    public Mono<Supplier> findById(@NonNull Long supplierId) {
        return supplierRepository.findById(supplierId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .map(SupplierMapper::buildSupplier)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Flux<Supplier> findByIds(@NonNull List<Long> listSupplierId) {
        return supplierRepository.findAllById(listSupplierId)
                .map(SupplierMapper::buildSupplier)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Tuple2<List<Supplier>, SupplierQuery>> findByQuery(@NonNull SupplierQuery supplierQuery) {
        return findAllSupplierByQuery(supplierQuery)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCES_NOT_FOUND))))
                .collectList()
                .zipWith(countTotalElementsByQuery(supplierQuery).map(totalItems -> supplierQuery.toBuilder()
                        .totalItems(totalItems)
                        .totalPages(getTotalPages(totalItems, supplierQuery.getPageSize()))
                        .build()
                ));
    }

    @Override
    public Mono<Supplier> findOrSave(@NonNull Supplier supplier) {
        return Mono.justOrEmpty(supplier.getId())
                .filter(Objects::nonNull)
                .flatMap(this::findById)
                .switchIfEmpty(Mono.defer(() -> save(supplier)))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Supplier> save(@NonNull Supplier supplier) {
        return supplierRepository.save(SupplierMapper.buildSupplierData(supplier))
                .flatMap(savedSupplierData -> supplierRepository.findById(savedSupplierData.getId()))
                .map(SupplierMapper::buildSupplier)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Supplier> updateById(@NonNull Supplier supplier) {
        return supplierRepository.findById(supplier.getId())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .map(supplierData -> supplier.toBuilder().createdAt(supplierData.getCreatedAt().toString()).build())
                .flatMap(supplierFull -> supplierRepository.save(SupplierMapper.buildSupplierData(supplierFull)))
                .flatMap(savedSupplierData -> supplierRepository.findById(savedSupplierData.getId()))
                .map(SupplierMapper::buildSupplier)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Void> deleteById(@NonNull Long supplierId) {
        return supplierRepository.findById(supplierId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .flatMap(supplierFromDatabase -> supplierRepository.deleteById(supplierId))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    public Flux<Supplier> findAllSupplierByQuery(SupplierQuery supplierQuery) {
        return supplierRepository.findAllSuppliersByQuery(supplierQuery, supplierQuery.getPageSize(),
                        getOffset(supplierQuery.getPageSize(), supplierQuery.getPageNumber()))
                .map(SupplierMapper::buildSupplier)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    public Mono<Long> countTotalElementsByQuery(@NonNull SupplierQuery supplierQuery) {
        return supplierRepository.countSuppliersByQuery(supplierQuery)
                .switchIfEmpty(Mono.defer(() -> Mono.just(ZERO_SUPPLIERS)))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    private Long getOffset(Long pageSize, Long pageNumber) {
        return (pageNumber - 1) * pageSize;
    }

    private Long getTotalPages(Long totalItems, Long pageSize) {
        return (totalItems % pageSize == 0) ? (totalItems / pageSize) : ((totalItems / pageSize) + 1);
    }
}
