# Copilot Instructions for gestordeestoque

## Project Overview
This is a Spring Boot-based inventory management system. The main application entry point is `GestordeestoqueApplication.java`. The codebase follows a layered architecture with clear separation of concerns:
- **Domain Layer**: Business entities in `domain/` (e.g., `Categoria`, `Estoque`, `Produto`, `Fornecedor`).
- **DTOs**: Data transfer objects in `dto/` for request/response payloads.
- **Controllers**: REST endpoints in `controller/` (e.g., `ProdutoController`, `CategoriaController`).
- **Services**: Business logic in `service/`.
- **Repositories**: Data access in `repository/` (Spring Data JPA).
- **Config**: Application configuration (security, Swagger) in `config/`.

## Build & Run
- Use the included Maven wrapper (`mvnw.cmd` on Windows) for builds and tests:
  - Build: `./mvnw.cmd clean package`
  - Run: `./mvnw.cmd spring-boot:run`
  - Test: `./mvnw.cmd test`
- Main config: `src/main/resources/application.properties`

## Patterns & Conventions
- **DTO Usage**: All controller endpoints use DTOs for input/output. Avoid exposing domain entities directly.
- **Builder Pattern**: Entities and DTOs use Lombok's `@Builder` for object construction.
- **Exception Handling**: Custom exceptions are in `exceptions/`. Controllers use `@ExceptionHandler` for error responses.
- **Mapping**: Use classes in `mapper/` for converting between domain and DTO objects.
- **Security**: Configured via `SecurityConfig` in `config/`. Swagger UI auto-opens via `SwaggerAutoOpener`.
- **Testing**: Tests are in `src/test/java/com/luizabau/gestordeestoque/`. Use Spring Boot's test framework.

## Integration Points
- **Spring Data JPA**: Repositories interface with the database using standard Spring Data patterns.
- **Swagger**: API documentation is enabled and auto-opens on startup.
- **Static/Template Resources**: Place static files in `resources/static/` and templates in `resources/templates/`.

## Examples
- To add a new entity:
  1. Create domain class in `domain/`, DTOs in `dto/`, repository in `repository/`, service in `service/`, controller in `controller/`, and mapper in `mapper/`.
  2. Use Lombok annotations for boilerplate.
  3. Register endpoints in the appropriate controller.
- To add a new REST endpoint:
  - Define DTOs, add method to controller, implement logic in service, handle mapping and exceptions as per existing patterns.

## Key Files
- `GestordeestoqueApplication.java`: Main entry point
- `pom.xml`: Maven dependencies
- `config/SecurityConfig.java`: Security setup
- `config/SwaggerConfig.java`: Swagger setup
- `controller/`: REST API endpoints
- `domain/`, `dto/`, `mapper/`, `repository/`, `service/`: Core layers

---
_Review these instructions for accuracy. If any conventions or workflows are missing or unclear, please provide feedback to improve this guide._
