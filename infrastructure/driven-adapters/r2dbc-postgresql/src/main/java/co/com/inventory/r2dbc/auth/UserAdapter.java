package co.com.inventory.r2dbc.auth;

import co.com.inventory.model.user.Person;
import co.com.inventory.model.user.User;
import co.com.inventory.model.user.gateways.UserGateway;
import co.com.inventory.model.commons.GeneralStatus;
import co.com.inventory.r2dbc.auth.mapper.UserMapper;
import co.com.inventory.r2dbc.auth.repository.UserRepository;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserAdapter implements UserGateway {

    private final UserRepository userRepository;

    @Override
    public Mono<User> findActiveUserByPersonId(Long personId) {
        return userRepository.findByPersonIdAndStatus(personId, GeneralStatus.ACTIVE)
                .map(UserMapper::buildUser)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<User> findByUsername(String username, GeneralStatus status) {
        return userRepository.findByUsername(username, status)
                .map(UserMapper::buildUser)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<User> save(User user, Person person) {
        return userRepository.saveUser(UserMapper.buildUserData(user, person))
                .map(UserMapper::buildUser)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<User> changePassword(User user) {
        return userRepository.changePassword(UserMapper.buildUserData(user))
                .map(UserMapper::buildUser)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
}
