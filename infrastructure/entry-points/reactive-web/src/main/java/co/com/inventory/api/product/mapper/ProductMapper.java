package co.com.inventory.api.product.mapper;

import co.com.inventory.api.brand.mapper.BrandMapper;
import co.com.inventory.api.product.dto.ProductDTO;
import co.com.inventory.api.product.dto.ProductListDTO;
import co.com.inventory.api.supplier.mapper.SupplierMapper;
import co.com.inventory.model.product.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static ProductDTO buildProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .purchasePrice(product.getPurchasePrice())
                .salePrice(product.getSalePrice())
                .amount(product.getAmount())
                .minimumStock(product.getMinimumStock())
                .maximumStock(product.getMaximumStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .brand(BrandMapper.buildBrandDTO(product.getBrand()))
                .suppliers(Objects.nonNull(product.getSuppliers())
                        ? product.getSuppliers().stream().map(SupplierMapper::buildSupplierDTO).toList()
                        : List.of())
                .build();
    }

    public static ProductListDTO buildProductListDTO(Product product) {
        return ProductListDTO.builder().products(List.of(buildProductDTO(product))).build();
    }

    public static ProductListDTO buildProductListDTO(List<Product> products) {
        return ProductListDTO.builder().products(products.stream()
                .map(ProductMapper::buildProductDTO).toList()).build();
    }

    public static Product buildProduct(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .purchasePrice(productDTO.getPurchasePrice())
                .salePrice(productDTO.getSalePrice())
                .amount(productDTO.getAmount())
                .minimumStock(productDTO.getMinimumStock())
                .maximumStock(productDTO.getMaximumStock())
                .brand(BrandMapper.buildBrand(productDTO.getBrand()))
                .suppliers(Objects.nonNull(productDTO.getSuppliers())
                        ? productDTO.getSuppliers().stream().map(SupplierMapper::buildSupplier).toList()
                        : List.of())
                .build();
    }
}
