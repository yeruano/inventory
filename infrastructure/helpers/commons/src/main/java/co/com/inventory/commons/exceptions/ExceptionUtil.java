package co.com.inventory.commons.exceptions;

import co.com.inventory.commons.exceptions.model.Error;
import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage;
import co.com.inventory.model.exception.BusinessException;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class ExceptionUtil {

    public static Mono<Error> buildError(Throwable exception, String domain) {
        return Mono.error(exception)
                .onErrorResume(BusinessException.class, ExceptionUtil::buildErrorFromBusinessException)
                .onErrorResume(TechnicalException.class, ExceptionUtil::buildErrorFromTechnicalException)
                .onErrorResume(Exception.class, ExceptionUtil::buildGenericError)
                .cast(Error.class)
                .map(error -> error.toBuilder().domain(domain).build());
    }

    private static Mono<Error> buildErrorFromBusinessException(BusinessException businessException) {
        return Mono.just(Error.builder()
                .reason(businessException.getBusinessExceptionMessage().getDescription())
                .code(businessException.getBusinessExceptionMessage().getCode())
                .message(businessException.getBusinessExceptionMessage().getMessage())
                .build());
    }

    private static Mono<Error> buildErrorFromTechnicalException(TechnicalException technicalException) {
        return Mono.just(Error.builder()
                .reason(technicalException.getTechnicalExceptionMessage().getDescription())
                .code(technicalException.getTechnicalExceptionMessage().getCode())
                .message(technicalException.getTechnicalExceptionMessage().getMessage())
                .build());
    }

    private static Mono<Error> buildGenericError(Exception exception) {
        return Mono.just(Error.builder()
                .reason(TechnicalExceptionMessage.TECHNICAL_SERVER_ERROR.getDescription())
                .code(TechnicalExceptionMessage.TECHNICAL_SERVER_ERROR.getCode())
                .message(exception.getMessage())
                .build());
    }
}
