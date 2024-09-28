package co.com.inventory.r2dbc.purchaseinvoice.data;

import co.com.inventory.model.commons.InvoiceStatus;
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
@Table(name = "purchase_invoice", schema = "public")
public class InvoiceDetailData {

    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("url_photo")
    private String urlPhoto;

    @Column("purchase_date")
    private LocalDateTime purchaseDate;

    @Column("entry_date")
    private LocalDateTime entryDate;

    @Column("total")
    private Double total;

    @Column("amount_paid")
    private Double amountPaid;

    @Column("state")
    private InvoiceStatus state;

    @Column("supplier_id")
    private Long supplierId;
}
