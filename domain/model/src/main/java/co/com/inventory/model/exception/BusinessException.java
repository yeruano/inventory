package co.com.inventory.model.exception;

import co.com.inventory.model.exception.messages.BusinessExceptionMessage;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;

@Getter
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2435145755412237331L;

    private final BusinessExceptionMessage businessExceptionMessage;

    public BusinessException(Throwable throwable, BusinessExceptionMessage businessExceptionMessage) {
        super(businessExceptionMessage.getMessage(), throwable);
        this.businessExceptionMessage = businessExceptionMessage;
    }

    public BusinessException(BusinessExceptionMessage businessExceptionMessage) {
        super(businessExceptionMessage.getMessage());
        this.businessExceptionMessage = businessExceptionMessage;
    }
}
