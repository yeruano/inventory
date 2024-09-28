package co.com.inventory.model.purchaseinvoice.gateways;

import co.com.inventory.model.product.Product;
import lombok.NonNull;
import reactor.core.publisher.Flux;

public interface ProductInvoiceGateway {

    Flux<Product> findProductsByInvoiceId(@NonNull Long invoiceId);
}
