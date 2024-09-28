package co.com.inventory.api.manageaccountinformation.mapper;

import co.com.inventory.api.manageaccountinformation.dto.ChangePasswordDTO;
import co.com.inventory.model.auth.ChangePassword;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ManageAccountInformationMapper {

    public ChangePassword buildChangePassword(ChangePasswordDTO changePasswordDTO) {
        return ChangePassword.builder()
                .username(changePasswordDTO.getUsername())
                .password(changePasswordDTO.getPassword())
                .build();
    }
}
