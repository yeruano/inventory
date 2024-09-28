package co.com.inventory.api.exceptionhandler;

import co.com.inventory.commons.exceptions.DatabaseException;
import co.com.inventory.commons.technicalexception.BadRequestException;
import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage;
import co.com.inventory.api.util.exception.ExceptionUtil;
import co.com.inventory.model.exception.BusinessException;
import co.com.inventory.model.exception.messages.BusinessExceptionMessage;
import co.com.inventory.model.exception.messages.ErrorList;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;

/**
 * Handles global error handling for the application using WebFlux framework.
 * This class extends AbstractErrorWebExceptionHandler to provide custom error handling logic.
 */
@Order(-2)
@Component
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final ErrorStatusMapper errorStatusMapper;

    /**
     * Constructs a new GlobalErrorWebExceptionHandler.
     *
     * @param errorAttributes       the error attributes to use for error handling
     * @param applicationContext    the Spring application context
     * @param serverCodecConfigurer the server codec configurer to use for error handling
     */
    public GlobalErrorWebExceptionHandler(DefaultErrorAttributes errorAttributes,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer,
                                          ErrorStatusMapper errorStatusMapper) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        this.errorStatusMapper = errorStatusMapper;
        this.setUpErrorMappings();
    }

    /**
     * Initialize the error mappings assigning the corresponding HTTP status codes for every error.
     * By default, all technical errors are mapped to HTTP status code 500 and
     * all business errors are mapped to HTTP status code 409.
     */
    public void setUpErrorMappings() {
        errorStatusMapper
                .addErrorMappings(Arrays.asList(BusinessExceptionMessage.values()), HttpStatus.CONFLICT)
                .addErrorMappings(Arrays.asList(TechnicalExceptionMessage.values()), HttpStatus.INTERNAL_SERVER_ERROR)
                .addErrorMapping(TechnicalExceptionMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST)
                .addErrorMapping(TechnicalExceptionMessage.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    /**
     * Gets the routing function to use for error handling.
     *
     * @param errorAttributes the error attributes to use for error handling
     * @return the routing function to use for error handling
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * Renders an error response for the given request.
     *
     * @param request the request to render an error response for
     * @return a Mono of the server response for the error
     */
    private @NonNull Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
        return Mono.just(request)
                .map(this::getError)
                .flatMap(Mono::error)
                .onErrorResume(BadRequestException.class, this::buildErrorResponse)
                .onErrorResume(TechnicalException.class, this::buildErrorResponse)
                .onErrorResume(BusinessException.class, this::buildErrorResponse)
                .onErrorResume(DatabaseException.class, this::buildErrorResponse)
                .onErrorResume(this::buildErrorResponse)
                .cast(Tuple2.class)
                .map(errorTuple -> {
                    var error = (ErrorList.Error) errorTuple.getT1();
                    HttpStatus httpStatus = (HttpStatus) errorTuple.getT2();
                    error = ExceptionUtil.addDomain(request, error);
                    return Tuples.of(error, httpStatus);
                })
                .flatMap(newTuple -> this.buildServerResponse(newTuple.getT1(), request, newTuple.getT2()))
                .doAfterTerminate(() -> System.out.println("Aqui deberia ir la impresion de los logs"));
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(BadRequestException badRequestException) {
        return ExceptionUtil.buildErrorResponse(badRequestException)
                .zipWith(Mono.just(errorStatusMapper.getHttpStatus(TechnicalExceptionMessage.BAD_REQUEST)));
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(TechnicalException technicalException) {
        return ExceptionUtil.buildErrorResponse(technicalException)
                .zipWith(Mono.just(errorStatusMapper.getHttpStatus(technicalException.getTechnicalExceptionMessage())));
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(BusinessException businessException) {
        return ExceptionUtil.buildErrorResponse(businessException)
                .zipWith(Mono.just(errorStatusMapper.getHttpStatus(businessException.getBusinessExceptionMessage())));
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(DatabaseException databaseException) {
        return ExceptionUtil.buildErrorResponse(databaseException)
                .zipWith(Mono.just(HttpStatus.valueOf(
                        Integer.parseInt(databaseException.getDatabaseExceptionMessage().getHttpStatus()))));
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(Throwable throwable) {
        return ExceptionUtil.buildErrorResponse(throwable)
                .zipWith(Mono.just(errorStatusMapper.getHttpStatus(TechnicalExceptionMessage.TECHNICAL_SERVER_ERROR)));
    }

    private Mono<ServerResponse> buildServerResponse(
            ErrorList.Error error, ServerRequest request, HttpStatus httpStatus) {

        var errorResponse = new ErrorList(List.of(error));

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse)
                .doOnNext(response -> request.attributes().put("CACHE_RESPONSE_BODY", errorResponse));
    }
}
