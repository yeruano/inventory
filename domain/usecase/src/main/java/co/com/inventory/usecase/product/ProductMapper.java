package co.com.inventory.usecase.product;

import co.com.inventory.model.brand.Brand;
import co.com.inventory.model.product.Product;
import co.com.inventory.model.supplier.Supplier;
import reactor.util.function.Tuple2;

import java.util.List;

public class ProductMapper {

    public Product buildProduct(Product product, Tuple2<Brand, List<Supplier>> tuple2) {
        return product.toBuilder()
                .brand(tuple2.getT1())
                .suppliers(tuple2.getT2())
                .build();
    }
}
