package co.com.inventory.model.user;

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
public class Person {

    private Long id;

    @NonNull
    private String names;

    @NonNull
    private String surnames;

    @NonNull
    private String address;

    @NonNull
    private String cellPhoneNumber;

    @NonNull
    private String email;
}
