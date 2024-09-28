package co.com.inventory.api.product.dto;

import co.com.inventory.api.brand.dto.BrandDTO;
import co.com.inventory.api.supplier.dto.SupplierDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
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
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4043860358733732358L;

    @Positive
    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    @Positive
    private Double salePrice;

    @NotNull
    @Positive
    private Double purchasePrice;

    @NotNull
    @Positive
    private int amount;

    @NotNull
    @Positive
    private int minimumStock;

    @NotNull
    @Positive
    private int maximumStock;

    private String createdAt;

    private String updatedAt;

    @NotNull
    private BrandDTO brand;

    @NotNull
    private List<SupplierDTO> suppliers;
}
