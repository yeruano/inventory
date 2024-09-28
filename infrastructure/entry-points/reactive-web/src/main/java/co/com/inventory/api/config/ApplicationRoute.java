package co.com.inventory.api.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "entries.reactive-web")
public class ApplicationRoute {

    @NotBlank private String brands;
    @NotBlank private String suppliers;
    @NotBlank private String products;
    @NotBlank private String installments;
    @NotBlank private String purchaseInvoice;
    @NotBlank private String roles;
    @NotBlank private String authentication;
    @NotBlank private String users;
    @NotBlank private ManageAccountInformation manageAccountInformation;

    @Getter
    @Setter
    public static class ManageAccountInformation {
        @NotBlank private String changePassword;
    }
}
