package co.com.inventory.api.product;

import co.com.inventory.api.config.ApplicationRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.com.inventory.commons.utils.PathVariableConstant.PRODUCT_ID;
import static co.com.inventory.commons.utils.PathVariableConstant.getPathVariable;

@Configuration
@RequiredArgsConstructor
public class ProductRouter {

    private final ApplicationRoute applicationRoute;

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler productHandler) {
        String productIdPath = applicationRoute.getProducts() + getPathVariable(PRODUCT_ID);
        return RouterFunctions.route().nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), builder -> builder
                .GET(productIdPath, productHandler::findProductById)
                .GET(applicationRoute.getProducts(), productHandler::findAllProducts)
                .PUT(productIdPath, productHandler::updateProductById)
                .DELETE(productIdPath, productHandler::deleteProductById))
                .build();
    }
}
