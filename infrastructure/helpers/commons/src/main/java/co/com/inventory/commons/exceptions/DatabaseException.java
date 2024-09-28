package co.com.inventory.commons.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import reactor.util.function.Tuple3;

import java.io.Serial;
import java.util.Optional;

@Getter
public class DatabaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7809797993407134436L;

    private final DatabaseExceptionMessage databaseExceptionMessage;
    private static final String DEFAULT_ERROR_CODE = "MIT9999";
    private static final String DEFAULT_ERROR_MESSAGE = "Database Unexpected Error";
    private static final String DEFAULT_ERROR_HTTP_STATUS = "500";

    public DatabaseException(DatabaseExceptionMessage databaseExceptionMessage) {
        super(databaseExceptionMessage.getMessage());
        this.databaseExceptionMessage = databaseExceptionMessage;
    }

    public static Exception buildException(Throwable throwable, Tuple3<String, String, HttpStatus> errorDetails) {
        System.out.println(throwable);
        System.out.println(throwable.getMessage());
        String code = errorDetails.getT1();
        String description = errorDetails.getT2();
        String httpStatus = String.valueOf(errorDetails.getT3().value());

        var databaseExceptionMessage = DatabaseExceptionMessage.builder()
                .code(code)
                .message(throwable.getMessage())
                .description(description)
                .httpStatus(httpStatus)
                .build();

        return new DatabaseException(databaseExceptionMessage);
    }

    public static DatabaseException buildException(Throwable throwable) {
        var databaseExceptionMessage = DatabaseExceptionMessage.builder()
                .code(DEFAULT_ERROR_CODE)
                .message(Optional.ofNullable(throwable.getMessage()).orElse(DEFAULT_ERROR_MESSAGE))
                .description(DEFAULT_ERROR_MESSAGE)
                .httpStatus(DEFAULT_ERROR_HTTP_STATUS)
                .build();

        return new DatabaseException(databaseExceptionMessage);
    }
}
