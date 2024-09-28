package co.com.inventory.r2dbc.supplier.data;

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
@Table(name = "supplier", schema = "public")
public class SupplierData {

    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("nit")
    private String nit;

    @Column("company_name")
    private String companyName;

    @Column("supplier_name")
    private String supplierName;

    @Column("description")
    private String description;

    @Column("cell_phone_number")
    private String cellPhoneNumber;

    @Column("address")
    private String address;

    @Column("email")
    private String email;

    @Column("web_page")
    private String webPage;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
