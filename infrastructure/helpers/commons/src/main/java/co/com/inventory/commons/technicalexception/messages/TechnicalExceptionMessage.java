package co.com.inventory.commons.technicalexception.messages;

import co.com.inventory.model.exception.messages.ExceptionMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionMessage implements ExceptionMessage {

    TECHNICAL_SERVER_ERROR("MIT0001", "Internal server error", "Internal server error"),
    BAD_REQUEST("MIT0002", "Bad Request", "Invalid Request"),
    TECHNICAL_JSON_EXCEPTION("MIT0003", "Technical json exception", "Error while parsing the json"),
    R2DBC_OPERATION_EXCEPTION("MIT0004", "R2DBC operation exception", "Exception while performing R2DBC operation"),
    RESOURCE_NOT_FOUND("MIT0005", "Resource not found", "The requested resource does not exist"),
    RESOURCES_NOT_FOUND("MIT0006", "Resources not found", "No resources were found with the specified conditions"),
    NEGATIVE_QUERY_PARAMS("MIT0007", "Negative query params", "Query params cannot be negative"),
    RESOURCE_ALREADY_CREATED("MIT0008", "Resource already created", "The resource you are trying to create already exists"),
    LIST_OF_INSTALLMENTS_CANNOT_BE_EMPTY("MIT0009", "List of installments cannot be empty",
            "The list of installments cannot be empty"),
    INVALID_TOKEN("MIT0010", "Invalid token", "Invalid token"),
    INVALID_TOKEN_STRUCTURE("MIT0011", "Invalid token structure", "The token provided does not have a valid structure"),
    USER_NOT_LOGGED_IN("MIT0012", "User not logged in", "No session information was found for the user"),
    USER_NOT_FOUND("MIT0013", "User not found", "No user was found with the specified conditions"),
    TOKEN_NOT_FOUND("MIT0014", "Token not found", "The requested token was not found"),
    SECURITY_VALIDATION_FAILED("MIT0015", "Security validation failed", "The security validations performed failed");

    private final String code;
    private final String description;
    private final String message;
}
