package co.com.inventory.model.purchaseinvoice;

import co.com.inventory.model.product.Product;
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
public class PurchaseInvoice {

    private List<Product> products;
    private InvoiceDetail invoice;
    private Supplier supplier;
    private List<Installment> installments;
}
