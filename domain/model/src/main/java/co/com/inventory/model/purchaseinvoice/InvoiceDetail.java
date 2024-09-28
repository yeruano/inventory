package co.com.inventory.model.purchaseinvoice;

import co.com.inventory.model.commons.InvoiceStatus;
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
public class InvoiceDetail {

    private Long id;

    @NonNull
    private String urlPhoto;

    @NonNull
    private String purchaseDate;

    @NonNull
    private String entryDate;

    @NonNull
    private Double total;

    private Double amountPaid;

    private InvoiceStatus state;

    private Long supplierId;
}
