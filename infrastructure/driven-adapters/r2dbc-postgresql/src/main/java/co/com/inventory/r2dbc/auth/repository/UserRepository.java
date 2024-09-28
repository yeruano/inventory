package co.com.inventory.r2dbc.auth.repository;

import co.com.inventory.model.commons.GeneralStatus;
import co.com.inventory.r2dbc.auth.data.UserData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserData, Long> {

    @Query("SELECT * FROM \"user\" WHERE person_id = :personId AND status = :status.name")
    Mono<UserData> findByPersonIdAndStatus(Long personId, GeneralStatus status);

    @Query("SELECT * FROM \"user\" WHERE username = :username AND status = :status.name::public.status_enum")
    Mono<UserData> findByUsername(String username, GeneralStatus status);

    @Query("INSERT INTO \"user\"(username, password, status, person_id) " +
            "VALUES (" +
            ":#{#userData.username}, " +
            ":#{#userData.password}, " +
            ":#{#userData.status}::public.status_enum, " +
            ":#{#userData.personId}) " +
            "RETURNING *")
    Mono<UserData> saveUser(@Param("userData") UserData userData);

    @Query("UPDATE \"user\" SET " +
            "password = :#{#userData.password}, " +
            "password_expiry_date = :#{#userData.passwordExpiryDate}, " +
            "is_password_expired = FALSE " +
            "WHERE username = :#{#userData.username}")
    Mono<UserData> changePassword(@Param("userData") UserData userData);
}
