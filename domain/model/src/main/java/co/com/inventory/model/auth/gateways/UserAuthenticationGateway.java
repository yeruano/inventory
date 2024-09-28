package co.com.inventory.model.auth.gateways;

import co.com.inventory.model.auth.AuthenticatedUser;
import co.com.inventory.model.role.Role;
import co.com.inventory.model.user.User;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserAuthenticationGateway {

    Mono<Void> authenticate(User user, List<Role> roles);
    Mono<Void> logout();
    Mono<AuthenticatedUser> getAuthenticatedUser();
}
