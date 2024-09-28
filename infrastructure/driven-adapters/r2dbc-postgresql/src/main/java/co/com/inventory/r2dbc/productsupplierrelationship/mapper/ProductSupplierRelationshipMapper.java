package co.com.inventory.r2dbc.productsupplierrelationship.mapper;

import co.com.inventory.model.productsupplierrelationship.ProductSupplierRelationship;
import co.com.inventory.r2dbc.productsupplierrelationship.data.ProductSupplierRelationshipData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductSupplierRelationshipMapper {

    public ProductSupplierRelationship buildProductSupplierRelationship(
            ProductSupplierRelationshipData productSupplierRelationshipData) {
        return ProductSupplierRelationship.builder()
                .productId(productSupplierRelationshipData.getProductId())
                .supplierId(productSupplierRelationshipData.getSupplierId())
                .build();
    }

    public ProductSupplierRelationshipData buildProductSupplierRelationshipData(
            ProductSupplierRelationship productSupplierRelationship) {
        return ProductSupplierRelationshipData.builder()
                .productId(productSupplierRelationship.getProductId())
                .supplierId(productSupplierRelationship.getSupplierId())
                .build();
    }
}
