package co.com.inventory.api.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1929168020515226637L;

    private Long id;

    @NotBlank
    private String names;

    @NotBlank
    private String surnames;

    @NotBlank
    private String address;

    @NotBlank
    private String cellPhoneNumber;

    @NotBlank
    private String email;
}
