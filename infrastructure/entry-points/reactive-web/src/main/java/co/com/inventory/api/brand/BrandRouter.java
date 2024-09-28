package co.com.inventory.api.brand;

import co.com.inventory.api.config.ApplicationRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.com.inventory.commons.utils.PathVariableConstant.BRAND_ID;
import static co.com.inventory.commons.utils.PathVariableConstant.getPathVariable;

@Configuration
@RequiredArgsConstructor
public class BrandRouter {

    private final ApplicationRoute applicationRoute;

    @Bean
    public RouterFunction<ServerResponse> brandRoutes(BrandHandler brandHandler) {
        String brandIdPath = applicationRoute.getBrands() + getPathVariable(BRAND_ID);
        return RouterFunctions.route().nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), builder -> builder
                .GET(brandIdPath, brandHandler::findBrandById)
                .GET(applicationRoute.getBrands(), brandHandler::findBrandsByQuery)
                .POST(applicationRoute.getBrands(), brandHandler::saveBrand)
                .PUT(brandIdPath, brandHandler::updateBrandById)
                .DELETE(brandIdPath, brandHandler::deleteBrandById))
                .build();
    }
}
