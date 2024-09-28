package co.com.inventory.api.purchaseinvoice.router;

import co.com.inventory.api.config.ApplicationRoute;
import co.com.inventory.api.purchaseinvoice.handler.InstallmentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class InstallmentRouter {

    private final ApplicationRoute applicationRoute;

    @Bean
    public RouterFunction<ServerResponse> installmentRoutes(InstallmentHandler installmentHandler) {
        return RouterFunctions.route().nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), builder -> builder
                        .POST(applicationRoute.getInstallments(), installmentHandler::saveInstallments)
                        .PUT(applicationRoute.getInstallments(), installmentHandler::updateInstallment)
                        .DELETE(applicationRoute.getInstallments(), installmentHandler::deleteInstallments))
                .build();
    }
}
