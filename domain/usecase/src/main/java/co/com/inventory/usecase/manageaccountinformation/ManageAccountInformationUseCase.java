package co.com.inventory.usecase.manageaccountinformation;

import co.com.inventory.model.auth.ChangePassword;
import co.com.inventory.model.user.PasswordHistory;
import co.com.inventory.model.user.User;
import co.com.inventory.model.auth.gateways.PasswordEncoderGateway;
import co.com.inventory.model.user.gateways.PasswordHistoryGateway;
import co.com.inventory.model.user.gateways.UserGateway;
import co.com.inventory.model.commons.GeneralStatus;
import co.com.inventory.model.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static co.com.inventory.model.exception.messages.BusinessExceptionMessage.PASSWORD_ALREADY_USED;
import static co.com.inventory.model.exception.messages.BusinessExceptionMessage.USERNAME_NOT_FOUND;

@RequiredArgsConstructor
public class ManageAccountInformationUseCase {

    private static final int PASSWORD_EXPIRY_DAYS = 30;
    private static final boolean IS_PASSWORD_EXPIRED = false;
    private final UserGateway userGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;
    private final PasswordHistoryGateway passwordHistoryGateway;

    public Mono<Void> changePassword(ChangePassword changePassword) {
        return userGateway.findByUsername(changePassword.getUsername(), GeneralStatus.ACTIVE)
                .switchIfEmpty(Mono.error(new BusinessException(USERNAME_NOT_FOUND)))
                .flatMap(user -> validateNewPassword(user, changePassword.getPassword()))
                .flatMap(user -> {
                    String encodePassword = passwordEncoderGateway.encode(changePassword.getPassword());
                    user.setPassword(encodePassword);
                    user.setIsPasswordExpired(IS_PASSWORD_EXPIRED);
                    user.setPasswordExpiryDate(LocalDateTime.now().plusDays(PASSWORD_EXPIRY_DAYS));
                    return Mono.zip(userGateway.changePassword(user),
                            passwordHistoryGateway.save(buildPasswordHistory(user))).then();
                });
    }

    private Mono<User> validateNewPassword(User user, String newPassword) {
        return passwordHistoryGateway.findAllByUserId(user.getId())
                .collectList()
                .filter(passwordHistories -> isPasswordUsed(passwordHistories, newPassword))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(PASSWORD_ALREADY_USED))))
                .thenReturn(user);
    }

    private PasswordHistory buildPasswordHistory(User user) {
        return PasswordHistory.builder()
                .password(user.getPassword())
                .changeDate(LocalDateTime.now())
                .userId(user.getId())
                .build();
    }

    private boolean isPasswordUsed(List<PasswordHistory> passwordHistories, String newPassword) {
        return passwordHistories.stream()
                .noneMatch(passHistory -> passwordEncoderGateway.matches(newPassword, passHistory.getPassword()));
    }
}
