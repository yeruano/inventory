package co.com.inventory.api.supplier.mapper;

import co.com.inventory.api.supplier.dto.SupplierDTO;
import co.com.inventory.api.supplier.dto.SupplierListDTO;
import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.model.supplier.SupplierQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.NEGATIVE_QUERY_PARAMS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SupplierMapper {

    public static final String DEFAULT_PAGE_NUMBER = "1";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_NAME = "";
    public static final String DEFAULT_DESCRIPTION = "";

    public static SupplierListDTO buildSupplierListDTO(Supplier supplier) {
        var supplierDTO = SupplierDTO.builder()
                .id(supplier.getId())
                .nit(supplier.getNit())
                .companyName(supplier.getCompanyName())
                .supplierName(supplier.getSupplierName())
                .description(supplier.getDescription())
                .cellPhoneNumber(supplier.getCellPhoneNumber())
                .address(supplier.getAddress())
                .email(supplier.getEmail())
                .webPage(supplier.getWebPage())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();

        return SupplierListDTO.builder().suppliers(List.of(supplierDTO)).build();
    }

    public static SupplierQuery buildSupplierQuery(ServerRequest serverRequest) {
        String pageNumber = validatePaginationFilters(serverRequest, "pageNumber").orElse(DEFAULT_PAGE_NUMBER);
        String pageSize = validatePaginationFilters(serverRequest, "pageSize").orElse(DEFAULT_PAGE_SIZE);
        String name = serverRequest.queryParam("name").orElse(DEFAULT_NAME);
        String description = serverRequest.queryParam("name").orElse(DEFAULT_DESCRIPTION);

        return SupplierQuery.builder()
                .supplierName(name)
                .description(description)
                .pageNumber(Long.parseLong(pageNumber))
                .pageSize(Long.parseLong(pageSize))
                .build();
    }

    public static SupplierListDTO buildSupplierListDTO(Tuple2<List<Supplier>, SupplierQuery> tuples) {
        List<Supplier> supplierList = tuples.getT1();
        SupplierQuery supplierQuery = tuples.getT2();
        return SupplierListDTO.builder()
                .suppliers(buildSupplierDTOList(supplierList))
                .totalItems(supplierQuery.getTotalItems())
                .currentPage(supplierQuery.getPageNumber())
                .totalPages(supplierQuery.getTotalPages())
                .build();
    }

    public static List<SupplierDTO> buildSupplierDTOList(List<Supplier> brandList) {
        return brandList.stream().map(SupplierMapper::buildSupplierDTO).toList();
    }

    public static SupplierDTO buildSupplierDTO(Supplier supplier) {
        return SupplierDTO.builder()
                .id(supplier.getId())
                .nit(supplier.getNit())
                .companyName(supplier.getCompanyName())
                .supplierName(supplier.getSupplierName())
                .description(supplier.getDescription())
                .cellPhoneNumber(supplier.getCellPhoneNumber())
                .address(supplier.getAddress())
                .email(supplier.getEmail())
                .webPage(supplier.getWebPage())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }

    public static Supplier buildSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = Supplier.builder()
                .nit(supplierDTO.getNit())
                .companyName(supplierDTO.getCompanyName())
                .supplierName(supplierDTO.getSupplierName())
                .description(supplierDTO.getDescription())
                .cellPhoneNumber(supplierDTO.getCellPhoneNumber())
                .address(supplierDTO.getAddress())
                .email(supplierDTO.getEmail())
                .webPage(supplierDTO.getWebPage())
                .build();

        if (Objects.nonNull(supplierDTO.getId())) {
            return supplier.toBuilder().id(supplierDTO.getId()).build();
        }

        return supplier;
    }

    private static Optional<String> validatePaginationFilters(ServerRequest serverRequest, String queryParamName) {
        Optional<String> queryParam = serverRequest.queryParam(queryParamName);
        if (queryParam.isPresent() && (Long.parseLong(queryParam.get()) < 1)) {
            throw new TechnicalException(NEGATIVE_QUERY_PARAMS);
        }

        return queryParam;
    }
}
