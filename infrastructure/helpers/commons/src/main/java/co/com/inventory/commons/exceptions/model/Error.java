package co.com.inventory.commons.exceptions.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Error {

    private String reason;
    private String domain;
    private String code;
    private String message;
}
