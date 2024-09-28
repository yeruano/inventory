package co.com.inventory.r2dbc.commons.errorhandler;

import co.com.inventory.commons.exceptions.DatabaseException;
import co.com.inventory.commons.technicalexception.TechnicalException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.BadSqlGrammarException;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.Map;
import java.util.function.Function;

import static co.com.inventory.commons.exceptions.DatabaseException.buildException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseErrorHandler {

    private static final Tuple3<String, String, HttpStatus> BAD_GRAMMAR_DETAILS =
            Tuples.of("MIT9001", "SQL syntax error.", BAD_REQUEST);
    private static final Tuple3<String, String, HttpStatus> TRANSIENT_RESOURCE_DETAILS =
            Tuples.of("MIT9002", "Temporary database error.", SERVICE_UNAVAILABLE);
    private static final Tuple3<String, String, HttpStatus> DATA_INTEGRITY_VIOLATION_DETAILS =
            Tuples.of("MIT9003", "A data integrity violation occurred.", CONFLICT);
    private static final Tuple3<String, String, HttpStatus> PERMISSION_DENIED_DETAILS =
            Tuples.of("MIT9004", "Permission denied for the attempted operation.", FORBIDDEN);
    private static final Tuple3<String, String, HttpStatus> NON_TRANSIENT_RESOURCE_DETAILS =
            Tuples.of("MIT9005", "Non-transient database error.", INTERNAL_SERVER_ERROR);
    private static final Tuple3<String, String, HttpStatus> TIMEOUT_DETAILS =
            Tuples.of("MIT9006", "Database operation timed out.", REQUEST_TIMEOUT);
    private static final Tuple3<String, String, HttpStatus> DUPLICATE_KEY_DETAILS =
            Tuples.of("MIT9007", "Duplicate key exception.", BAD_REQUEST);

    static final Map<Class<? extends Throwable>, Function<Throwable, Exception>> ERROR_MAP = Map.of(
            BadSqlGrammarException.class, ex -> buildException(ex, BAD_GRAMMAR_DETAILS),
            TransientDataAccessResourceException.class, ex -> buildException(ex, TRANSIENT_RESOURCE_DETAILS),
            DuplicateKeyException.class, ex -> buildException(ex, DUPLICATE_KEY_DETAILS),
            TechnicalException.class, ex ->
                    new TechnicalException(((TechnicalException) ex).getTechnicalExceptionMessage()),
            DataIntegrityViolationException.class, ex -> buildException(ex, DATA_INTEGRITY_VIOLATION_DETAILS),
            PermissionDeniedDataAccessException.class, ex -> buildException(ex, PERMISSION_DENIED_DETAILS),
            QueryTimeoutException.class, ex -> buildException(ex, TIMEOUT_DETAILS),
            NonTransientDataAccessResourceException.class, ex -> buildException(ex, NON_TRANSIENT_RESOURCE_DETAILS)
    );

    public static Exception handleError(Throwable error) {
        return ERROR_MAP
                .getOrDefault(error.getClass(), DatabaseException::buildException)
                .apply(error);
    }
}
