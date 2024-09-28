package co.com.inventory.api.user.dto;

import co.com.inventory.api.role.dto.RoleDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterUserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7064468641657644279L;

    @NotNull
    private PersonDTO person;

    @NotNull
    private UserDTO user;

    @NotEmpty
    private List<RoleDTO> roles;
}
