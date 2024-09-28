package co.com.inventory.model.exception.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionMessage implements ExceptionMessage {

    THE_AMOUNT_OF_THE_INSTALLMENTS_IS_NOT_VALID("IMB0001", "Invalid installmets",
            "The amount of the installments is not valid"),
    USER_ALREADY_CREATED("IMB0002", "User Already Created", "User Already Created"),
    EMPTY_ROLE_LIST("IMB0003", "Empty Role List", "Empty Role List"),
    INVALID_CREDENTIALS("IMB0004", "Invalid Credentials", "The credentials provided are incorrect"),
    USERNAME_NOT_FOUND("IMB0005", "Username Not Found", "The username provided was not found"),
    PASSWORD_ALREADY_USED("IMB0006", "Password Already Used", "The password provided has already been used"),
    PASSWORD_CHANGE_REQUIRED("IMB0007", "Password Change Required", "The user must change their password");

    private final String code;
    private final String description;
    private final String message;
}
