package co.com.inventory.api.purchaseinvoice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstallmentDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 9148018139608293085L;

    @Positive
    private Long id;

    @NonNull
    @Positive
    private Double amount;

    @NonNull
    private String paymentDate;

    private String urlPhoto;

    private Long invoiceId;
}
