package co.com.inventory.r2dbc.purchaseinvoice.data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "supplier_payment_history", schema = "public")
public class SupplierPaymentHistoryData {

    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("invoice_id")
    private Long invoiceId;

    @Column("url_photo")
    private String urlPhoto;

    @Column("payment_date")
    private LocalDateTime paymentDate;

    @Column("installment_amount")
    private Double installmentAmount;
}
