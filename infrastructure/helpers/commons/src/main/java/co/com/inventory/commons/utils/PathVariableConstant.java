package co.com.inventory.commons.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathVariableConstant {

    public final String BRAND_ID = "brandId";
    public final String SUPPLIER_ID = "supplierId";
    public final String PRODUCT_ID = "productId";
    public final String INVOICE_ID = "invoiceId";
    public final String ROLE_ID = "roleId";

    public String getPathVariable(String name) {
        return String.format("/{%s}", name);
    }
}
