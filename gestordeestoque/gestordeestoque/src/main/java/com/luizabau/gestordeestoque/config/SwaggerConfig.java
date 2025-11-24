package com.luizabau.gestordeestoque.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestor de Estoque API")
                        .version("1.0.0")
                        .description("Sistema completo de gestão de estoque desenvolvido por Luiza Carvalho. " +
                                "Oferece recursos para controle de produtos, movimentações de estoque, " +
                                "alertas automáticos e relatórios de inventário.")
                        .termsOfService("https://github.com/lubcarv/gestordeestoque/terms")
                        .contact(new Contact()
                                .name("Luiza Carvalho (lubcarv)")
                                .email("lubcarv@ifsc.edu.br")
                                .url("https://github.com/lubcarv"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Servidor de Desenvolvimento"),
                new Server()
                        .url("https://gestordeestoque.herokuapp.com")
                        .description("Servidor de Produção")
        ));}}