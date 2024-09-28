package co.com.inventory.api.purchaseinvoice.dto;

import co.com.inventory.api.product.dto.ProductDTO;
import co.com.inventory.api.supplier.dto.SupplierDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseInvoiceDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5393911970800312039L;

    private List<ProductDTO> products;
    private InvoiceDetailDTO invoice;
    private SupplierDTO supplier;
    private List<InstallmentDTO> installments;
}
