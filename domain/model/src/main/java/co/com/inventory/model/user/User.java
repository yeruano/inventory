package co.com.inventory.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    private LocalDateTime creationDate;

    private LocalDateTime lastAccessDate;

    private LocalDateTime passwordExpiryDate;

    private Boolean isPasswordExpired;

    private String status;
}
