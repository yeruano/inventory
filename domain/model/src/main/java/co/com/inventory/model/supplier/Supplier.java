package co.com.inventory.model.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Supplier {

    private Long id;
    private String nit;
    private String companyName;
    private String supplierName;
    private String description;
    private String cellPhoneNumber;
    private String address;
    private String email;
    private String webPage;
    private String createdAt;
    private String updatedAt;
}
