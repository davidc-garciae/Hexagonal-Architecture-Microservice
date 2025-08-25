# 📘 Especificaciones por Microservicio y API Gateway

Este documento complementa el `README.md` principal y detalla, por cada microservicio (con arquitectura hexagonal), los módulos, endpoints, reglas de negocio, modelos de datos, eventos, mappers, seguridad y pruebas. También incluye la especificación del API Gateway.

Índice

-   1. Convenciones generales
-   2. usuarios-service
-   3. plazoleta-service
-   4. orders-service
-   5. traceability-service
-   6. mensajeria-service (Twilio)
-   6. API Gateway
-   7. Matriz de dependencias y eventos
-   8. Estrategia de pruebas por capa

---

## 1) Convenciones generales

-   Arquitectura: Hexagonal (Domain, Application, Infrastructure)
-   Base de datos: PostgreSQL 15+ (aplica a todos excepto mensajería que también usa relacional para logs)
-   Mensajería: RabbitMQ (CloudAMQP o local)
-   Seguridad: JWT validado en el Gateway; los servicios usan headers `X-User-*`
-   Estándar de rutas: `/api/v1/{recurso}`
-   Estándar de respuesta de error: `{ timestamp, path, message, code }`

Estructura por servicio (carpetas):

```
src/main/java/com/pragma/powerup/
  domain/{model, api, spi, usecase}
  application/{dto, mapper, handler}
  infrastructure/
    input/{rest, messaging}
    out/{jpa, feign, messaging, external}
    configuration
    security
```

Plantillas Gradle de referencia:

-   Microservicio MVC: `docs/templates/build.gradle.microservice`
-   API Gateway WebFlux: `docs/templates/build.gradle.gateway`

---

## 2) usuarios-service

Responsabilidad: gestión de usuarios, roles y autenticación (emite JWT).

### Domain

-   Modelos: `UsuarioModel`, `RolEnum`
-   Use cases: `CrearPropietarioUseCase`, `CrearEmpleadoUseCase`, `CrearClienteUseCase`, `AutenticacionUseCase`
-   Puertos IN: `IUsuarioServicePort`, `IAutenticacionServicePort`
-   Puertos OUT: `IUsuarioPersistencePort`, `IPasswordEncoder`, `IJwtService`

### Application

-   DTOs request/response por recurso
-   Mappers: MapStruct entre DTOs ↔ Modelos
-   Handlers: orquestan casos de uso

### Infrastructure

-   JPA: entidades `UsuarioEntity`, repos `IUsuarioRepository`
-   Security: `JwtService` (generación/validación token), `SecurityConfig`
-   REST: `AuthController`, `UsuarioController`

### Endpoints

-   `POST /api/v1/auth/login` → devuelve JWT { token, userId, role, expiresIn }
-   `POST /api/v1/usuarios/propietario` (ADMIN)
-   `POST /api/v1/usuarios/empleado` (OWNER)
-   `POST /api/v1/usuarios/cliente` (público o autenticado)
-   `GET /api/v1/usuarios/{id}` (según rol y caso de uso)
-   Validaciones de apoyo para otros servicios (vía Feign):
    -   `GET /api/v1/usuarios/{id}/activo`
    -   `GET /api/v1/usuarios/validar-empleado/{empleadoId}/restaurante/{restauranteId}`
    -   `GET /api/v1/usuarios/validar-propietario/{propietarioId}/restaurante/{restauranteId}`

### Reglas clave

-   Email válido, documento numérico, teléfono máx 13 (+ opcional), mayoría de edad
-   Contraseña encriptada con BCrypt

---

## 3) plazoleta-service

Responsabilidad: restaurantes y platos.

### Domain

-   Modelos: `RestauranteModel`, `PlatoModel`, `CategoriaEnum`
-   Use cases: `CrearRestauranteUseCase`, `CrearPlatoUseCase`, `ModificarPlatoUseCase`, `TogglePlatoUseCase`, `ListarRestaurantesUseCase`, `ListarPlatosUseCase`
-   Puertos IN: `IRestauranteServicePort`, `IPlatoServicePort`
-   Puertos OUT: `IRestaurantePersistencePort`, `IPlatoPersistencePort`, `IUsuarioServicePort` (Feign a usuarios)

### Endpoints

-   `POST /api/v1/restaurantes` (ADMIN)
-   `GET  /api/v1/restaurantes?page=&size=` (público)
-   `POST /api/v1/platos` (OWNER del restaurante)
-   `PUT  /api/v1/platos/{id}` (OWNER)
-   `PATCH /api/v1/platos/{id}/estado` (OWNER)
-   `GET  /api/v1/platos/restaurante/{id}?categoria=&page=&size=` (público)

### Validaciones

-   Restaurante: NIT numérico único, teléfono válido, nombre no solo números
-   Plato: precio > 0, asociado a restaurante, solo OWNER del restaurante puede crearlo/modificarlo

---

## 4) orders-service

Responsabilidad: gestión del ciclo de vida de pedidos.

### Domain

-   Modelos: `PedidoModel`, `DetallePedidoModel`, `EstadoPedido`
-   Use cases: `CrearPedidoUseCase`, `ListarPedidosPorEstadoUseCase`, `AsignarPedidoUseCase`, `MarcarListoUseCase`, `EntregarPedidoUseCase`, `CancelarPedidoUseCase`
-   Puertos IN: `IPedidoServicePort`
-   Puertos OUT: `IPedidoPersistencePort`, `IUsuarioServicePort` (Feign), `IPlazoletaServicePort` (Feign), `INotificationPort` (RabbitMQ publisher), `IAuditoriaPort` (RabbitMQ publisher)

Endpoints:

-   `POST /api/v1/pedidos` (CUSTOMER, 1 pedido activo)
-   `GET  /api/v1/pedidos?estado=&restauranteId=&page=&size=` (EMPLOYEE/OWNER)
-   `PUT  /api/v1/pedidos/{id}/asignar` (EMPLOYEE)
-   `PUT  /api/v1/pedidos/{id}/listo` (EMPLOYEE) → genera PIN y evento
-   `PUT  /api/v1/pedidos/{id}/entregar` (EMPLOYEE, valida PIN)
-   `PUT  /api/v1/pedidos/{id}/cancelar` (CUSTOMER, solo PENDIENTE)

Eventos publicados (RabbitMQ):

-   `pedido.creado`
-   `pedido.estado.cambiado` (incluye estadoAnterior, estadoNuevo, pin si LISTO)

## 5) traceability-service

-   Modelos: `TrazabilidadPedidoModel`
-   Use cases: `RegistrarCambioEstadoUseCase`, `ConsultarTrazabilidadUseCase`, `ConsultarEficienciaUseCase`
-   Puertos OUT: `ITrazabilidadPersistencePort`
-   Consumers RabbitMQ: `pedido.creado`, `pedido.estado.cambiado`
-   Endpoints:
    -   `GET /api/v1/trazabilidad/pedido/{id}` (CUSTOMER dueño del pedido)
    -   `GET /api/v1/trazabilidad/eficiencia/restaurante/{id}` (OWNER)

---

## 5) mensajeria-service (Twilio)

Responsabilidad: envío de SMS y logging de notificaciones.

### Domain

-   Modelos: `NotificacionModel`, `TemplateModel`, `SMSResult`
-   Use cases: `EnviarSMSUseCase`, `ProcesarEventoPedidoUseCase`, `GestionarTemplatesUseCase`
-   Puertos OUT: `INotificacionPersistencePort`, `ISMSProviderPort` (Twilio), `IUsuarioServicePort` (para obtener celular)

### Infrastructure

-   Consumers RabbitMQ: `pedido.creado`, `pedido.estado.cambiado`
-   Adaptador Twilio: `TwilioAdapter` (`ISMSProviderPort`)
-   Endpoints (opcionales): `POST /api/v1/notificaciones/test-sms`

### Eventos → Acciones

-   `pedido.creado` → SMS opcional de confirmación
-   `pedido.estado.cambiado` con `LISTO` → SMS con PIN

---

## 6) API Gateway

-   Filtro global JWT: valida token y añade headers `X-User-Id`, `X-User-Role`, `X-User-Email`
-   Enrutamiento:
    -   `/api/v1/auth/**` → usuarios-service:8081
    -   `/api/v1/usuarios/**` → usuarios-service:8081
    -   `/api/v1/restaurantes/**` → plazoleta-service:8082
    -   `/api/v1/platos/**` → plazoleta-service:8082
    -   `/api/v1/pedidos/**` → pedidos-service:8083
    -   `/api/v1/trazabilidad/**` → trazabilidad-service:8084
    -   `/api/v1/notificaciones/**` → mensajeria-service:8085

Permisos básicos en Gateway (puede ampliarse en cada servicio):

-   Público: Swagger/OpenAPI, health
-   Autenticación requerida para el resto; reglas finas con `@PreAuthorize` en cada servicio

---

## 7) Matriz de dependencias y eventos

Dependencias síncronas (Feign):

-   `pedidos-service` → `usuarios-service`, `plazoleta-service`
-   `plazoleta-service` → `usuarios-service` (validaciones de propietario)
-   `mensajeria-service` → `usuarios-service` (consultar teléfono)

Dependencias asíncronas (RabbitMQ):

-   `pedidos-service` → publish → `pedido.creado`, `pedido.estado.cambiado`
-   `trazabilidad-service` → consume ambos
-   `mensajeria-service` → consume ambos

---

## 8) Estrategia de pruebas por capa

-   Domain (use cases): pruebas unitarias puras (sin Spring), >85% cobertura
-   Application (handlers, mappers): pruebas unitarias con Mockito
-   Infrastructure:
    -   JPA: pruebas de integración con Testcontainers (PostgreSQL)
    -   Feign: tests con WireMock o MockWebServer
    -   RabbitMQ: tests de integración con Testcontainers RabbitMQ
-   End-to-End: escenarios críticos (crear pedido → SMS → trazabilidad)
