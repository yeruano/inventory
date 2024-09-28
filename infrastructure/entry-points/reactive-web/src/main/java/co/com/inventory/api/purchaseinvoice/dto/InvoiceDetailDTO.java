package co.com.inventory.api.purchaseinvoice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceDetailDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1010876179776402214L;

    private Long id;

    @NonNull
    private String purchaseDate;

    @NonNull
    private String entryDate;

    @NonNull
    private String urlPhoto;

    @NonNull
    private Double total;

    private Double amountPaid;

    private String state;

    private Long supplierId;
}
