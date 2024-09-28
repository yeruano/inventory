package co.com.inventory.api.brand.mapper;

import co.com.inventory.api.brand.dto.BrandDTO;
import co.com.inventory.api.brand.dto.BrandListDTO;
import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.model.brand.Brand;
import co.com.inventory.model.brand.BrandQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.NEGATIVE_QUERY_PARAMS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BrandMapper {

    public static final String DEFAULT_PAGE_NUMBER = "1";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_NAME = "";
    public static final String DEFAULT_DESCRIPTION = "";

    public static BrandListDTO buildBrandListDTO(Brand brand) {
        var brandDTO = BrandDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .build();

        return BrandListDTO.builder().brands(List.of(brandDTO)).build();
    }

    public static BrandQuery buildBrandQuery(ServerRequest serverRequest) {
        String pageNumber = validatePaginationFilters(serverRequest, "pageNumber").orElse(DEFAULT_PAGE_NUMBER);
        String pageSize = validatePaginationFilters(serverRequest, "pageSize").orElse(DEFAULT_PAGE_SIZE);
        String name = serverRequest.queryParam("name").orElse(DEFAULT_NAME);
        String description = serverRequest.queryParam("description").orElse(DEFAULT_DESCRIPTION);

        return BrandQuery.builder()
                .name(name)
                .description(description)
                .pageNumber(Long.parseLong(pageNumber))
                .pageSize(Long.parseLong(pageSize))
                .build();
    }

    public static BrandListDTO buildBrandListDTO(Tuple2<List<Brand>, BrandQuery> tuples) {
        List<Brand> brandList = tuples.getT1();
        BrandQuery brandQuery = tuples.getT2();
        return BrandListDTO.builder()
                .brands(buildBrandDTOList(brandList))
                .totalItems(brandQuery.getTotalItems())
                .currentPage(brandQuery.getPageNumber())
                .totalPages(brandQuery.getTotalPages())
                .build();
    }

    public static List<BrandDTO> buildBrandDTOList(List<Brand> brandList) {
        return brandList.stream().map(BrandMapper::buildBrandDTO).toList();
    }

    public static Brand buildBrand(BrandDTO brandDTO) {
        Brand brand = Brand.builder()
                .name(brandDTO.getName())
                .description(brandDTO.getDescription())
                .build();

        if (Objects.nonNull(brandDTO.getId())) {
            brand = brand.toBuilder().id(brandDTO.getId()).build();
        }

        return brand;
    }

    public static BrandDTO buildBrandDTO(Brand brand) {
        return BrandDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .build();
    }

    public static Optional<String> validatePaginationFilters(ServerRequest serverRequest, String queryParamName) {
        Optional<String> queryParam = serverRequest.queryParam(queryParamName);
        if (queryParam.isPresent() && (Long.parseLong(queryParam.get()) < 1)) {
            throw new TechnicalException(NEGATIVE_QUERY_PARAMS);
        }

        return queryParam;
    }
}
