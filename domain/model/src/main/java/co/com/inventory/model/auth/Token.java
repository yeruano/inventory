package co.com.inventory.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Token {

    private Long id;

    @NonNull
    private String accessToken;

    @NonNull
    private String refreshToken;

    @NonNull
    private String type;

    private boolean revoked;

    private boolean expired;

    private Long userId;
}
