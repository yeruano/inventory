package co.com.inventory.commons.exceptions;

import co.com.inventory.model.exception.messages.ExceptionMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DatabaseExceptionMessage implements ExceptionMessage, Serializable {

    @Serial
    private static final long serialVersionUID = 6507348555575008891L;

    private String code;
    private String message;
    private String description;
    private String httpStatus;
}
