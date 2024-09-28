package co.com.inventory.commons.technicalexception;

import co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;

@Getter
@EqualsAndHashCode(callSuper = false)
public class TechnicalException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2691047327429866223L;

    private final TechnicalExceptionMessage technicalExceptionMessage;

    public TechnicalException(Throwable throwable, TechnicalExceptionMessage technicalExceptionMessage) {
        super(technicalExceptionMessage.getMessage(), throwable);
        this.technicalExceptionMessage = technicalExceptionMessage;
    }

    public TechnicalException(String message, TechnicalExceptionMessage technicalExceptionMessage) {
        super(message);
        this.technicalExceptionMessage = technicalExceptionMessage;
    }

    public TechnicalException(TechnicalExceptionMessage technicalExceptionMessage) {
        super(technicalExceptionMessage.getMessage());
        this.technicalExceptionMessage = technicalExceptionMessage;
    }
}
