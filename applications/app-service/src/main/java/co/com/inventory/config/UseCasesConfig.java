package co.com.inventory.config;

import co.com.inventory.usecase.product.ProductMapper;
import co.com.inventory.usecase.purchaseinvoice.PurchaseInvoiceMapper;
import co.com.inventory.usecase.purchaseinvoice.util.PurchaseInvoiceUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.inventory.usecase",
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
    },
    useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public ProductMapper productMapper() {
        return new ProductMapper();
    }

    @Bean
    public PurchaseInvoiceMapper supplierPurchaseInvoiceMapper() {
        return new PurchaseInvoiceMapper();
    }

    @Bean
    public PurchaseInvoiceUtil purchaseInvoiceUtil() {
        return new PurchaseInvoiceUtil();
    }
}
