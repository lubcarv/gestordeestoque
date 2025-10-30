package com.luizabau.gestordeestoque.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SwaggerAutoOpener implements ApplicationRunner {

    @Value("${server.port:8080}")
    private String serverPort;

    private final Environment environment;

    public SwaggerAutoOpener(Environment environment) {
        this.environment = environment;
    }


    @Override
    public void run(ApplicationArguments args) {
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isDevelopment = activeProfiles.length == 0 ||
                java.util.Arrays.asList(activeProfiles).contains("dev");

        if (!isDevelopment) {
            String baseUrl = System.getenv("RENDER_EXTERNAL_URL");
            if (baseUrl == null || baseUrl.isBlank()) {
                baseUrl = "http://localhost:" + serverPort;
            }
            System.out.println("🚀 Swagger UI disponível em: " + baseUrl + "/swagger-ui/index.html");

        }

        try {
            Thread.sleep(3000);

            String swaggerUrl = "http://localhost:" + serverPort + "/swagger-ui/index.html";
            String os = System.getProperty("os.name").toLowerCase();

            System.out.println("🚀 Iniciando Gestor de Estoque...");
            System.out.println("📋 Documentação da API: " + swaggerUrl);

            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", swaggerUrl).start();
                System.out.println(" Abrindo Swagger UI no Windows...");
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", swaggerUrl).start();
                System.out.println(" Abrindo Swagger UI no macOS...");
            } else if (os.contains("nix") || os.contains("nux")) {
                new ProcessBuilder("xdg-open", swaggerUrl).start();
                System.out.println("Abrindo Swagger UI no Linux...");
            }

            System.out.println(" Swagger UI disponível em: " + swaggerUrl);
            System.out.println("Endpoints principais:");
            System.out.println("   • GET    /api/produtos - Listar produtos");
            System.out.println("   • POST   /api/produtos - Criar produto");
            System.out.println("   • PUT    /api/produtos/{id}/repor - Repor estoque");
            System.out.println("   • GET    /api/produtos/estoque-baixo - Alertas");

        } catch (Exception e) {
            System.err.println(" Erro ao tentar abrir o navegador: " + e.getMessage());
            System.out.println(" Acesse manualmente: http://localhost:" + serverPort + "/swagger-ui/index.html");
        }
    }
}