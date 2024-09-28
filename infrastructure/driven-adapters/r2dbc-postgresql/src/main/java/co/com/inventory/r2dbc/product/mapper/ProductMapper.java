package co.com.inventory.r2dbc.product.mapper;

import co.com.inventory.model.brand.Brand;
import co.com.inventory.model.product.Product;
import co.com.inventory.r2dbc.product.data.ProductData;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Objects;

@UtilityClass
public class ProductMapper {

    public Product buildProduct(ProductData productData) {
        return Product.builder()
                .id(productData.getId())
                .name(productData.getName())
                .description(productData.getDescription())
                .salePrice(productData.getSalePrice())
                .purchasePrice(productData.getPurchasePrice())
                .amount(productData.getAmount())
                .minimumStock(productData.getMinimumStock())
                .maximumStock(productData.getMaximumStock())
                .createdAt(Objects.nonNull(productData.getCreatedAt()) ? productData.getCreatedAt().toString() : null)
                .updatedAt(Objects.nonNull(productData.getUpdatedAt()) ? productData.getUpdatedAt().toString() : null)
                .brand(Brand.builder().id(productData.getBrandId()).build())
                .build();
    }

    public ProductData buildProductData(Product product) {
        ProductData productData = ProductData.builder()
                .name(product.getName())
                .description(product.getDescription())
                .salePrice(product.getSalePrice())
                .purchasePrice(product.getPurchasePrice())
                .amount(product.getAmount())
                .minimumStock(product.getMinimumStock())
                .maximumStock(product.getMaximumStock())
                .brandId(product.getBrand().getId())
                .createdAt(Objects.nonNull(product.getCreatedAt()) ? LocalDateTime.parse(product.getCreatedAt()) : null)
                .build();

        return Objects.nonNull(product.getId()) ? productData.toBuilder().id(product.getId()).build() : productData;
    }
}
