package co.com.inventory.r2dbc.supplier.mapper;

import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.r2dbc.supplier.data.SupplierData;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Objects;

@UtilityClass
public class SupplierMapper {

    public Supplier buildSupplier(SupplierData supplierData) {
        return Supplier.builder()
                .id(supplierData.getId())
                .nit(supplierData.getNit())
                .companyName(supplierData.getCompanyName())
                .supplierName(supplierData.getSupplierName())
                .description(supplierData.getDescription())
                .cellPhoneNumber(supplierData.getCellPhoneNumber())
                .address(supplierData.getAddress())
                .email(supplierData.getEmail())
                .webPage(supplierData.getWebPage())
                .createdAt(Objects.nonNull(supplierData.getCreatedAt()) ? supplierData.getCreatedAt().toString() : null)
                .updatedAt(Objects.nonNull(supplierData.getUpdatedAt()) ? supplierData.getUpdatedAt().toString() : null)
                .build();
    }

    public SupplierData buildSupplierData(Supplier supplier) {
        SupplierData supplierData = SupplierData.builder()
                .nit(supplier.getNit())
                .companyName(supplier.getCompanyName())
                .supplierName(supplier.getSupplierName())
                .description(supplier.getDescription())
                .cellPhoneNumber(supplier.getCellPhoneNumber())
                .address(supplier.getAddress())
                .email(supplier.getEmail())
                .webPage(supplier.getWebPage())
                .createdAt(Objects.nonNull(supplier.getCreatedAt())
                        ? LocalDateTime.parse(supplier.getCreatedAt()) : null)
                .build();

        if (Objects.nonNull(supplier.getId())) {
            return supplierData.toBuilder().id(supplier.getId()).build();
        }

        return supplierData;
    }
}
