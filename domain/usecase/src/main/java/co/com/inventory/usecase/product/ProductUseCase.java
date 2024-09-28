package co.com.inventory.usecase.product;

import co.com.inventory.model.brand.Brand;
import co.com.inventory.model.brand.gateways.BrandGateway;
import co.com.inventory.model.product.Product;
import co.com.inventory.model.product.gateways.ProductGateway;
import co.com.inventory.model.productsupplierrelationship.gateways.ProductSupplierRelationshipGateway;
import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.model.supplier.gateways.SupplierGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductGateway productGateway;
    private final BrandGateway brandGateway;
    private final SupplierGateway supplierGateway;
    private final ProductSupplierRelationshipGateway productSupplierRelationshipGateway;
    private final ProductMapper productMapper;

    public Mono<Product> findProductById(Long productId) {
        return productGateway.findById(productId)
                .zipWhen(product -> findBrandAndSuppliers(productId, product.getBrand().getId()),
                        (productMapper::buildProduct));
    }

    public Mono<List<Product>> findAllProducts() {
        return productGateway.findAllProducts()
                .flatMap(product -> findBrandAndSuppliers(product.getId(), product.getBrand().getId())
                        .map(tuple2 -> product.toBuilder().brand(tuple2.getT1()).suppliers(tuple2.getT2()).build()))
                .collectList();
    }

    public Mono<Product> updateProductById(Product product) {
        return productGateway.updateById(product);
    }

    public Mono<Void> deleteProductById(Long productId) {
        return productGateway.deleteById(productId);
    }

    private Mono<Tuple2<Brand, List<Supplier>>> findBrandAndSuppliers(Long productId, Long brandId) {
        Mono<List<Supplier>> suppliersMono = productSupplierRelationshipGateway.findAllByProductId(productId)
                .flatMap(relationship -> supplierGateway.findById(relationship.getSupplierId()))
                .collectList();
        Mono<Brand> brandMono = brandGateway.findById(brandId);

        return Mono.zip(brandMono, suppliersMono);
    }
}
