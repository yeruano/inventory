package co.com.inventory.r2dbc.config;

import java.time.Duration;

import co.com.inventory.r2dbc.config.model.PostgresqlConnectionProperties;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;

import io.r2dbc.postgresql.client.SSLMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "co.com.inventory.r2dbc.*")
public class PostgresqlConnectionPool {

	private final String sslMode;
	private final String schema;
	private final PostgresqlConnectionProperties postgresqlConnectionProperties;

	public PostgresqlConnectionPool(
			@Value("${adapters.postgresql.sslMode}") String sslMode,
			@Value("${adapters.postgresql.schema}") String schema,
			PostgresqlConnectionProperties postgresqlConnectionProperties
	) {
		this.sslMode = sslMode;
		this.schema = schema;
		this.postgresqlConnectionProperties = postgresqlConnectionProperties;
	}

	@Bean
	@Primary
	public ConnectionPool getPostgresqlConnection() {
		return buildConnectionConfiguration();
	}

	private ConnectionPool buildConnectionConfiguration() {
		PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration.builder()
				.host(postgresqlConnectionProperties.getHost())
				.port(postgresqlConnectionProperties.getPort())
				.database(postgresqlConnectionProperties.getDbname())
				.schema(this.schema)
				.username(postgresqlConnectionProperties.getUsername())
				.password(postgresqlConnectionProperties.getPassword())
				.sslMode(SSLMode.fromValue(this.sslMode))
				.build();

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder()
                .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
                .name("api-postgres-connection-pool")
                .initialSize(postgresqlConnectionProperties.getPool().getInitialSize())
                .maxSize(postgresqlConnectionProperties.getPool().getMaxSize())
                .maxIdleTime(Duration.ofMinutes(postgresqlConnectionProperties.getPool().getMaxIdleTime()))
                .validationQuery("SELECT 1")
                .build();

		return new ConnectionPool(poolConfiguration);
	}
}
