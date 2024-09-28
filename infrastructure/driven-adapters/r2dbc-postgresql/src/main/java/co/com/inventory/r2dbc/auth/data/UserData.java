package co.com.inventory.r2dbc.auth.data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "user", schema = "public")
public class UserData {

    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("creation_date")
    private LocalDateTime creationDate;

    @Column("last_access_date")
    private LocalDateTime lastAccessDate;

    @Column("status")
    private String status;

    @Column("password_expiry_date")
    private LocalDateTime passwordExpiryDate;

    @Column("is_password_expired")
    private Boolean isPasswordExpired;

    @Column("person_id")
    private Long personId;
}
