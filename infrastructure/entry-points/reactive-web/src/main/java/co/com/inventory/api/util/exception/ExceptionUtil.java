package co.com.inventory.api.util.exception;

import co.com.inventory.commons.exceptions.DatabaseException;
import co.com.inventory.commons.technicalexception.BadRequestException;
import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage;
import co.com.inventory.model.exception.BusinessException;
import co.com.inventory.model.exception.messages.ErrorList.Error;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@UtilityClass
public class ExceptionUtil {

    private static final String COLON = ":";

    public Error addDomain(ServerRequest serverRequest, Error error) {
        return error.toBuilder()
                .domain(String.join(COLON, serverRequest.method().name(), serverRequest.path()))
                .build();
    }

    public Mono<Error> buildErrorResponse(BadRequestException badRequestException) {
        return Mono.just(Error.builder()
                .reason(TechnicalExceptionMessage.BAD_REQUEST.getDescription())
                .code(TechnicalExceptionMessage.BAD_REQUEST.getCode())
                .message(badRequestException.getMessage())
                .build());
    }

    public Mono<Error> buildErrorResponse(TechnicalException technicalException) {
        return Mono.just(Error.builder()
                .reason(technicalException.getTechnicalExceptionMessage().getDescription())
                .code(technicalException.getTechnicalExceptionMessage().getCode())
                .message(technicalException.getCause() == null ?
                        technicalException.getMessage() : technicalException.getCause().getMessage())
                .build());
    }

    public Mono<Error> buildErrorResponse(BusinessException businessException) {
        return Mono.just(Error.builder()
                .reason(businessException.getBusinessExceptionMessage().getDescription())
                .code(businessException.getBusinessExceptionMessage().getCode())
                .message(businessException.getBusinessExceptionMessage().getMessage())
                .build());
    }

    public Mono<Error> buildErrorResponse(DatabaseException databaseException) {
        return Mono.just(Error.builder()
                .reason(databaseException.getDatabaseExceptionMessage().getDescription())
                .code(databaseException.getDatabaseExceptionMessage().getCode())
                .message(databaseException.getDatabaseExceptionMessage().getMessage())
                .build());
    }

    public Mono<Error> buildErrorResponse(Throwable throwable) {
        return Mono.just(Error.builder()
                .reason(TechnicalExceptionMessage.TECHNICAL_SERVER_ERROR.getDescription())
                .code(TechnicalExceptionMessage.TECHNICAL_SERVER_ERROR.getCode())
                .message(throwable.getCause() == null ? throwable.getMessage() : throwable.getCause().getMessage())
                .build());
    }
}
