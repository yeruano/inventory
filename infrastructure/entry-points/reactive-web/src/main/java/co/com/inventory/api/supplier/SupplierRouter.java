package co.com.inventory.api.supplier;

import co.com.inventory.api.config.ApplicationRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.com.inventory.commons.utils.PathVariableConstant.SUPPLIER_ID;
import static co.com.inventory.commons.utils.PathVariableConstant.getPathVariable;

@Configuration
@RequiredArgsConstructor
public class SupplierRouter {

    private final ApplicationRoute applicationRoute;

    @Bean
    public RouterFunction<ServerResponse> supplierRoutes(SupplierHandler supplierHandler) {
        String supplierIdPath = applicationRoute.getSuppliers() + getPathVariable(SUPPLIER_ID);
        return RouterFunctions.route().nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), builder -> builder
                .GET(supplierIdPath, supplierHandler::findSupplierById)
                .GET(applicationRoute.getSuppliers(), supplierHandler::findSuppliersByQuery)
                .POST(applicationRoute.getSuppliers(), supplierHandler::saveSupplier)
                .PUT(supplierIdPath, supplierHandler::updateSupplierById)
                .DELETE(supplierIdPath, supplierHandler::deleteSupplierById))
                .build();
    }
}
