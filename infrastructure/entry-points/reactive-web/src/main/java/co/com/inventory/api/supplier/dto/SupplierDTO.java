package co.com.inventory.api.supplier.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5003852613068219200L;

    @Positive
    private Long id;

    @NotBlank
    private String nit;

    @NotBlank
    private String companyName;

    @NotBlank
    private String supplierName;

    private String description;

    @NotBlank
    private String cellPhoneNumber;

    private String address;

    private String email;

    private String webPage;

    private String createdAt;

    private String updatedAt;
}
