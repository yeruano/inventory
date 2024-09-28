package co.com.inventory.r2dbc.config.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "adapters.postgresql")
public class PostgresqlConnectionProperties {

    private String dbname;
    private String username;
    private String password;
    private String host;
    private int port;
    private PostgresqlPoolProperties pool;

    @Getter
    @Setter
    public static class PostgresqlPoolProperties {
        private int initialSize;
        private int maxSize;
        private int maxIdleTime;
    }
}
