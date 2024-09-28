package co.com.inventory.api.purchaseinvoice.router;

import co.com.inventory.api.config.ApplicationRoute;
import co.com.inventory.api.purchaseinvoice.handler.PurchaseInvoiceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.com.inventory.commons.utils.PathVariableConstant.INVOICE_ID;
import static co.com.inventory.commons.utils.PathVariableConstant.getPathVariable;

@Configuration
@RequiredArgsConstructor
public class PurchaseInvoiceRouter {

    private final ApplicationRoute applicationRoute;

    @Bean
    public RouterFunction<ServerResponse> purchaseInvoiceRoutes(PurchaseInvoiceHandler purchaseInvoiceHandler) {
        String invoiceIdPath = applicationRoute.getPurchaseInvoice() + getPathVariable(INVOICE_ID);
        return RouterFunctions.route().nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), builder -> builder
                .GET(invoiceIdPath, purchaseInvoiceHandler::findPurchaseInvoiceById)
                .GET(applicationRoute.getPurchaseInvoice(), purchaseInvoiceHandler::findPurchaseInvoice)
                .POST(applicationRoute.getPurchaseInvoice(), purchaseInvoiceHandler::savePurchaseInvoice))
                .build();
    }
}
