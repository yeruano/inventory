package co.com.inventory.model.exception.user;

import co.com.inventory.model.exception.BusinessException;
import co.com.inventory.model.exception.messages.BusinessExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class UsernameAlreadyExistException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 5490372413155292936L;

    public UsernameAlreadyExistException() {
        super(BusinessExceptionMessage.USER_ALREADY_CREATED);
    }
}
