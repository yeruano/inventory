package co.com.inventory.api.role;

import co.com.inventory.api.config.ApplicationRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.com.inventory.commons.utils.PathVariableConstant.ROLE_ID;
import static co.com.inventory.commons.utils.PathVariableConstant.getPathVariable;

@Configuration
@RequiredArgsConstructor
public class RoleRouter {

    private final ApplicationRoute applicationRoute;

    @Bean
    public RouterFunction<ServerResponse> roleRoutes(RoleHandler roleHandler) {
        String roleIdPath = applicationRoute.getRoles() + getPathVariable(ROLE_ID);
        return RouterFunctions.route().nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), builder -> builder
                        .GET(roleIdPath, roleHandler::findRoleById)
                        .GET(applicationRoute.getRoles(), roleHandler::findAllRoles)
                        .POST(applicationRoute.getRoles(), roleHandler::saveRole)
                        .PUT(roleIdPath, roleHandler::updateRoleById)
                        .DELETE(roleIdPath, roleHandler::deleteRoleById))
                .build();
    }
}
