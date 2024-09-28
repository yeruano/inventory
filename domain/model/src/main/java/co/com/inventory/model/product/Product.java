package co.com.inventory.model.product;

import co.com.inventory.model.brand.Brand;
import co.com.inventory.model.supplier.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {

    private Long id;
    private String name;
    private String description;
    private Double purchasePrice;
    private Double salePrice;
    private int amount;
    private int minimumStock;
    private int maximumStock;
    private String createdAt;
    private String updatedAt;
    private Brand brand;
    private List<Supplier> suppliers;
}
