package co.com.inventory.api.manageaccountinformation;

import co.com.inventory.api.config.ApplicationRoute;
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
public class ManageAccountInformationRouter {

    private final ApplicationRoute applicationRoute;

    @Bean
    public RouterFunction<ServerResponse> manageAccountInformationRoutes(
            ManageAccountInformationHandler manageAccountInformationHandler) {
        final String CHANGE_PASSWORD_PATH = applicationRoute.getManageAccountInformation().getChangePassword();
        return RouterFunctions.route().nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), builder -> builder
                        .POST(CHANGE_PASSWORD_PATH, manageAccountInformationHandler::changePassword))
                .build();
    }
}
