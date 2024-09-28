package co.com.inventory.r2dbc.productsupplierrelationship.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "product_supplier", schema = "public")
public class ProductSupplierRelationshipData {

    @Column("product_id")
    private Long productId;

    @Column("supplier_id")
    private Long supplierId;
}
