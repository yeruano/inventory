package co.com.inventory.model.auth.gateways;

public interface PasswordEncoderGateway {

    String encode(String password);
    boolean matches(String password, String encodedPassword);
}
