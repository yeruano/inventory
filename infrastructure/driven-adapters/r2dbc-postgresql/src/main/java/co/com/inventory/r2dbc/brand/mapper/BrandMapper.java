package co.com.inventory.r2dbc.brand.mapper;

import co.com.inventory.model.brand.Brand;
import co.com.inventory.r2dbc.brand.data.BrandData;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class BrandMapper {

    public Brand buildBrand(BrandData brandData) {
        return Brand.builder()
                .id(brandData.getId())
                .name(brandData.getName())
                .description(brandData.getDescription())
                .build();
    }

    public BrandData buildBrandData(Brand brand) {
        BrandData brandData = BrandData.builder()
                .name(brand.getName())
                .description(brand.getDescription())
                .build();

        if (Objects.nonNull(brand.getId())) {
            return brandData.toBuilder().id(brand.getId()).build();
        }

        return brandData;
    }
}
