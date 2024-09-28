package co.com.inventory.api.auth.mapper;

import co.com.inventory.api.auth.dto.AuthorizationTokenDTO;
import co.com.inventory.api.auth.dto.LoginUserDTO;
import co.com.inventory.model.auth.AuthorizationToken;
import co.com.inventory.model.auth.LoginUser;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthenticationMapper {

    public LoginUser buildLoginUser(LoginUserDTO loginUserDTO) {
        return LoginUser.builder()
                .username(loginUserDTO.getUsername())
                .password(loginUserDTO.getPassword())
                .build();
    }

    public AuthorizationTokenDTO buildTokenDTO(AuthorizationToken authorizationToken) {
        return AuthorizationTokenDTO.builder()
                .accessToken(authorizationToken.getAccessToken())
                .refreshToken(authorizationToken.getRefreshToken())
                .build();
    }
}
