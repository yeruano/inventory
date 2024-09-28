package co.com.inventory.r2dbc.auth.repository;

import co.com.inventory.r2dbc.auth.data.UserPermissionData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends ReactiveCrudRepository<UserPermissionData, Long> {
}
