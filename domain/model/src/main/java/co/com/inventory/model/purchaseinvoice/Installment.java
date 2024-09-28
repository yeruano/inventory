package co.com.inventory.model.purchaseinvoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Installment {

    private Long id;

    private Long invoiceId;

    @NonNull
    private Double amount;

    @NonNull
    private String paymentDate;

    private String urlPhoto;
}
