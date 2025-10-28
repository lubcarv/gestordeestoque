package com.luizabau.gestordeestoque.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        if (databaseUrl != null && databaseUrl.startsWith("postgres://")) {
            try {
                URI dbUri = new URI(databaseUrl);
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DataSourceBuilder
                        .create()
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .driverClassName("org.postgresql.Driver")
                        .build();
            } catch (URISyntaxException e) {
                throw new RuntimeException("Error parsing DATABASE_URL", e);
            }
        }

        // Fallback para configuração local do application.properties
        return DataSourceBuilder
                .create()
                .url(System.getenv().getOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/db_gestordeestoque"))
                .username(System.getenv().getOrDefault("DB_USERNAME", "root"))
                .password(System.getenv().getOrDefault("DB_PASSWORD", "admin"))
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}