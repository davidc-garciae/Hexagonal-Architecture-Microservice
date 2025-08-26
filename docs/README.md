# 🍕 Sistema de Plazoleta de Comidas - Microservicios

> **Arquitectura Hexagonal | Spring Boot | MySQL | RabbitMQ | Twilio**

[![Java 17](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle&logoColor=white)](https://gradle.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message%20Broker-FF6600?logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-6BA539?logo=openapiinitiative&logoColor=white)](https://www.openapis.org/)
[![Swagger UI](https://img.shields.io/badge/Swagger%20UI-springdoc-85EA2D?logo=swagger&logoColor=black)](https://springdoc.org/)
[![MapStruct](https://img.shields.io/badge/MapStruct-1.x-02569B)](https://mapstruct.org/)
[![Lombok](https://img.shields.io/badge/Lombok-1.18.x-CA0C00)](https://projectlombok.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![ArchUnit](https://img.shields.io/badge/ArchUnit-1.x-1F6FEB)](https://www.archunit.org/)
[![JaCoCo](https://img.shields.io/badge/JaCoCo-coverage-DC143C)](https://www.jacoco.org/jacoco/)
[![Spotless](https://img.shields.io/badge/Spotless-code%20style-0ABF53)](https://github.com/diffplug/spotless)
[![Micrometer](https://img.shields.io/badge/Micrometer-Tracing-0075A8)](https://micrometer.io/)
[![OpenTelemetry](https://img.shields.io/badge/OpenTelemetry-OTLP-FF6F00?logo=opentelemetry&logoColor=white)](https://opentelemetry.io/)
[![Grafana Loki](https://img.shields.io/badge/Grafana-Loki-F46800?logo=grafana&logoColor=white)](https://grafana.com/oss/loki/)
[![Spring Cloud Gateway](https://img.shields.io/badge/Spring%20Cloud-Gateway-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud-gateway)
[![WebFlux](https://img.shields.io/badge/Spring-WebFlux-6DB33F?logo=spring&logoColor=white)](https://docs.spring.io/spring-framework/reference/web/webflux.html)
[![JWT](https://img.shields.io/badge/JWT-Security-000000?logo=jsonwebtokens&logoColor=white)](https://jwt.io/)

Este proyecto implementa un sistema completo para gestionar una plazoleta de comidas con múltiples restaurantes, utilizando arquitectura de microservicios con patrón hexagonal.

## 📋 Índice

1. [Visión General](#-visión-general)
2. [Arquitectura del Sistema](#-arquitectura-del-sistema)
3. [Microservicios](#-microservicios)
4. [Plan de Implementación](#-plan-de-implementación)
5. [Configuración del Entorno](#-configuración-del-entorno)
6. [Desarrollo por Fases](#-desarrollo-por-fases)
7. [Testing y Despliegue](#-testing-y-despliegue)

---

## 🎯 Visión General

### Descripción del Negocio

Sistema que centraliza los pedidos de una plazoleta de comidas con diferentes restaurantes. Permite a los clientes realizar pedidos, a los empleados gestionar la preparación, y a los propietarios administrar sus restaurantes.

### Estados del Pedido

```
PENDIENTE → EN_PREPARACION → LISTO → ENTREGADO
    ↓
 CANCELADO
```

### Roles del Sistema

-   **👨‍💼 Administrador**: Crear restaurantes y cuentas de propietarios
-   **🏪 Propietario**: Gestionar menú y empleados de su restaurante
-   **👨‍🍳 Empleado**: Atender y preparar pedidos
-   **👤 Cliente**: Realizar pedidos y consultar trazabilidad

---

## 🏗️ Arquitectura del Sistema

### Microservicios Principales

```mermaid
graph TB
    subgraph "🌐 Capa de Presentación"
        API_GW["🚪 API Gateway<br/>Spring Cloud Gateway"]
    end

    subgraph "⚙️ Microservicios de Negocio"
        MS_USER["👥 users-service<br/>Port 8081"]
        MS_RESTAURANT["🏪 restaurants-service<br/>Port 8082"]
        MS_ORDER["📋 orders-service<br/>Port 8083"]
        MS_TRACE["📊 traceability-service<br/>Port 8084"]
        MS_MSG["📧 messaging-service<br/>Port 8085"]
    end

    subgraph "🗄️ Bases de Datos PostgreSQL"
        DB_USER[("👥 users_db<br/>PostgreSQL")]
        DB_REST[("🏪 restaurants_db<br/>PostgreSQL")]
        DB_ORDER[("📋 orders_db<br/>PostgreSQL")]
        DB_TRACE[("📊 traceability_db<br/>PostgreSQL")]
        DB_MSG[("📧 messaging_db<br/>PostgreSQL")]
    end

    subgraph "📨 Mensajería"
        RABBIT["🐰 RabbitMQ"]
    end

    subgraph "☁️ Servicios Externos"
        TWILIO["📞 Twilio SMS"]
    end
```

### Tecnologías

-   **Backend**: Spring Boot 3.2+, Java 17
-   **Build Tool**: Gradle 8.0+
-   **Arquitectura**: Hexagonal (Ports & Adapters)
-   **Base de Datos**: PostgreSQL 15+ (solo relacional)
-   **Mensajería**: RabbitMQ (CloudAMQP o local)
-   **SMS**: Twilio API
-   **Documentación**: OpenAPI 3.0
-   **Testing**: JUnit 5, Mockito, TestContainers

### ¿Por qué solo PostgreSQL y no NoSQL?

**PostgreSQL es perfecto para este proyecto porque:**

✅ **Datos altamente relacionales**: Usuarios → Restaurantes → Platos → Pedidos → Detalles  
✅ **Transacciones ACID necesarias**: Los pedidos requieren consistencia absoluta  
✅ **Consultas complejas**: Reportes, métricas, joins entre múltiples entidades  
✅ **Integridad referencial**: Claves foráneas y constraints son esenciales  
✅ **Excelente rendimiento**: PostgreSQL maneja millones de registros sin problemas  
✅ **Extensiones avanzadas**: Full-text search, JSON, funciones de ventana

**NoSQL sería innecesario porque:**
❌ No hay documentos sin estructura fija  
❌ No necesitamos escalabilidad masiva (millones de usuarios concurrentes)  
❌ No tenemos datos no relacionales  
❌ Añadiría complejidad sin beneficios

### Modelos de Datos Principales

#### Usuario

```sql
- id: BIGINT (PK)
- nombre: VARCHAR(50) NOT NULL
- apellido: VARCHAR(50) NOT NULL
- documento_identidad: VARCHAR(20) NOT NULL UNIQUE (solo números)
- celular: VARCHAR(13) NOT NULL (máx 13 chars, puede incluir +)
- fecha_nacimiento: DATE NOT NULL (mayor edad)
- correo: VARCHAR(100) NOT NULL UNIQUE (formato email válido)
- clave: VARCHAR(255) NOT NULL (BCrypt)
- rol: ENUM('ADMINISTRADOR', 'PROPIETARIO', 'EMPLEADO', 'CLIENTE')
- activo: BOOLEAN DEFAULT true
- restaurante_id: BIGINT (FK - solo para EMPLEADO)
```

#### Restaurante

```sql
- id: BIGINT (PK)
- nombre: VARCHAR(100) NOT NULL (puede incluir números, no solo números)
- nit: VARCHAR(20) NOT NULL UNIQUE (solo números)
- direccion: VARCHAR(200) NOT NULL
- telefono: VARCHAR(13) NOT NULL (solo números, máx 13 chars, puede incluir +)
- url_logo: VARCHAR(255) NOT NULL
- propietario_id: BIGINT NOT NULL (FK Usuario con rol PROPIETARIO)
```

#### Plato

```sql
- id: BIGINT (PK)
- nombre: VARCHAR(100) NOT NULL
- precio: DECIMAL(10,2) NOT NULL (entero positivo > 0)
- descripcion: TEXT NOT NULL
- url_imagen: VARCHAR(255) NOT NULL
- categoria: ENUM('ENTRADA', 'PRINCIPAL', 'POSTRE', 'BEBIDA') NOT NULL
- activo: BOOLEAN DEFAULT true
- restaurante_id: BIGINT NOT NULL (FK)
```

#### Pedido

```sql
- id: BIGINT (PK)
- cliente_id: BIGINT NOT NULL (FK Usuario con rol CLIENTE)
- restaurante_id: BIGINT NOT NULL (FK)
- empleado_id: BIGINT (FK Usuario con rol EMPLEADO - al asignarse)
- estado: ENUM('PENDIENTE', 'EN_PREPARACION', 'LISTO', 'ENTREGADO', 'CANCELADO')
- fecha_creacion: TIMESTAMP DEFAULT NOW()
- fecha_actualizacion: TIMESTAMP
- pin: VARCHAR(6) (generado cuando estado = LISTO)
```

#### DetallePedido

```sql
- id: BIGINT (PK)
- pedido_id: BIGINT NOT NULL (FK)
- plato_id: BIGINT NOT NULL (FK)
- cantidad: INT NOT NULL (> 0)
- precio_unitario: DECIMAL(10,2) NOT NULL
```

#### TrazabilidadPedido

```sql
- id: BIGINT (PK)
- pedido_id: BIGINT NOT NULL (FK)
- estado_anterior: VARCHAR(20)
- estado_nuevo: VARCHAR(20) NOT NULL
- fecha_cambio: TIMESTAMP DEFAULT NOW()
- empleado_id: BIGINT (FK - quien hizo el cambio)
```

### Reglas de Negocio Críticas

#### Estados de Pedido (Máquina de Estados)

```
PENDIENTE → EN_PREPARACION → LISTO → ENTREGADO
    ↓
CANCELADO (solo desde PENDIENTE)
```

#### Restricciones de Usuario

-   **Cliente**: Solo un pedido activo (PENDIENTE, EN_PREPARACION, LISTO)
-   **Empleado**: Solo maneja pedidos de su restaurante asignado
-   **Propietario**: Solo gestiona platos/empleados de su restaurante
-   **Admin**: Acceso global, crea propietarios y restaurantes

#### Validaciones de Campos

-   **Email**: Regex para formato válido
-   **Teléfono**: Máximo 13 caracteres, opcional símbolo +
-   **Documento**: Solo números, único por usuario
-   **Edad**: Mayor de 18 años (fecha_nacimiento)
-   **NIT**: Solo números, único por restaurante
-   **Precio**: Números enteros positivos > 0
-   **PIN**: 6 dígitos, único por pedido, generado automáticamente

---

## 🔧 Microservicios

### 1. 👥 users-service

**Responsabilidad**: Gestión de usuarios, autenticación y autorización

**Entidades**:

-   `Usuario` (id, nombre, apellido, documento, celular, email, clave, rol)
-   `Rol` (ADMIN, PROPIETARIO, EMPLEADO, CLIENTE)

**Funcionalidades**:

-   Crear usuarios por rol
-   Autenticación JWT
-   Validación de permisos
-   Gestión de empleados por restaurante

### 2. 🏪 restaurants-service

**Responsabilidad**: Gestión de restaurantes y platos

**Entidades**:

-   `Restaurante` (id, nombre, direccion, telefono, urlLogo, nit, propietarioId)
-   `Plato` (id, nombre, descripcion, precio, urlImagen, categoria, activo, restauranteId)
-   `Categoria` (ENTRADA, PRINCIPAL, POSTRE, BEBIDA)

**Funcionalidades**:

-   CRUD de restaurantes
-   CRUD de platos
-   Listar platos por restaurante y categoría
-   Validaciones de negocio

### 3. 📋 orders-service

**Responsabilidad**: Gestión del ciclo de vida de pedidos

**Entidades**:

-   `Pedido` (id, clienteId, restauranteId, fecha, estado, empleadoId, pin)
-   `DetallePedido` (id, pedidoId, platoId, cantidad)
-   `EstadoPedido` (PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO)

**Funcionalidades**:

-   Crear pedidos
-   Cambiar estados de pedidos
-   Asignar empleados
-   Generar PIN de seguridad
-   Validar un pedido activo por cliente

### 4. 📊 traceability-service

**Responsabilidad**: Auditoría y trazabilidad de pedidos

**Entidades**:

-   `TrazabilidadPedido` (id, pedidoId, estadoAnterior, estadoNuevo, fecha, empleadoId)
-   `MetricaPedido` (tiempoEnEstado, tiempoTotal, fechas)

**Funcionalidades**:

-   Registrar cambios de estado
-   Consultar trazabilidad por pedido
-   Generar métricas de tiempo
-   Reportes para propietarios

### 5. 📧 messaging-service

**Responsabilidad**: Notificaciones SMS y comunicaciones

**Entidades**:

-   `Notificacion` (id, destinatario, mensaje, estado, fechaEnvio, tipoNotificacion)
-   `PlantillaMensaje` (id, tipo, plantilla, variables)

**Funcionalidades**:

-   Envío de SMS vía Twilio
-   Gestión de plantillas
-   Log de notificaciones
-   Procesamiento asíncrono de eventos

---

## 📅 Plan de Implementación

### Fase 1: Configuración Base (Semana 1)

```bash
# 1.1 Configuración del entorno
├── Configurar Docker Compose (MySQL, RabbitMQ)
├── Crear repositorios Git por microservicio
├── Configurar arquetipo base para cada servicio
└── Configurar herramientas de desarrollo

# 1.2 Infraestructura común
├── Configurar Spring Cloud Config Server
├── Configurar Eureka Service Discovery
├── Configurar API Gateway básico
└── Documentación OpenAPI base
```

### Fase 2: users-service (Semana 1)

```bash
# HU-001: Crear cuenta Propietario (por Admin)
├── Modelo de dominio Usuario/Rol
├── Casos de uso de creación
├── Validaciones: email, teléfono, documento, mayor edad
├── Adaptadores JPA
├── API REST endpoints con autorización ADMIN
└── Tests unitarios e integración

# HU-005: Autenticación Sistema
├── Login con email/clave
├── Generación JWT con roles
├── Validaciones de permisos por endpoint
├── Configuración Spring Security
└── Tests de autenticación

# HU-006: Crear cuenta Empleado (por Propietario)
├── Validaciones de rol Propietario
├── Asignación a restaurante del propietario
├── Endpoint creación empleado
└── Tests específicos

# HU-008: Crear cuenta Cliente (autoregistro)
├── Validaciones campos obligatorios
├── Rol CLIENTE automático
├── Encriptación BCrypt
└── Documentación OpenAPI
```

### Fase 3: restaurants-service (Semana 1)

```bash
# HU-002: Crear restaurante (por Admin)
├── Modelo Restaurante
├── Validaciones: NIT, teléfono, nombre
├── Validación propietario existe con Feign
├── Integración con usuarios-service
└── Tests

# HU-003: Crear plato (por Propietario)
├── Modelo Plato y Categoría (ENTRADA, PRINCIPAL, POSTRE, BEBIDA)
├── Validaciones: precio > 0, propietario del restaurante
├── Estado activo = true por defecto
└── Tests

# HU-004: Modificar plato (por Propietario)
├── Solo precio y descripción modificables
├── Validación propietario del restaurante
└── Tests

# HU-007: Habilitar/Deshabilitar plato
├── Toggle estado activo/inactivo
├── Solo propietario del restaurante
└── Tests

# HU-009: Listar restaurantes (Cliente)
├── Paginación y ordenamiento alfabético
├── Solo campos: Nombre, UrlLogo
└── Endpoint público

# HU-010: Listar platos por restaurante (Cliente)
├── Filtro por categoría opcional
├── Paginación configurable
├── Solo platos activos
└── Endpoint público
```

### Fase 4: orders-service (Semana 2)

```bash
# HU-011: Realizar pedido (Cliente)
├── Modelo Pedido, DetallePedido, EstadoPedido
├── Validaciones: un pedido activo por cliente
├── Estado inicial: PENDIENTE
├── Integración con plazoleta-service (validar platos)
├── Integración con usuarios-service (validar cliente)
└── Tests de flujo completo

# HU-012: Lista pedidos por estado (Empleado)
├── Filtro por estado del pedido
├── Paginación configurable
├── Solo pedidos del restaurante del empleado
├── Validación empleado pertenece a restaurante
└── Tests con diferentes estados
```

### Fase 5: orders-service - Gestión Estados (Semana 3)

```bash
# HU-013: Asignarse a pedido (Empleado)
├── Cambio PENDIENTE → EN_PREPARACION
├── Asignación empleadoId al pedido
├── Validación empleado del restaurante
└── Tests de transiciones

# HU-014: Notificar pedido listo (Empleado)
├── Cambio EN_PREPARACION → LISTO
├── Generación PIN único
├── Envío evento para SMS (RabbitMQ)
└── Tests de notificación

# HU-015: Entregar pedido (Empleado)
├── Cambio LISTO → ENTREGADO
├── Validación PIN correcto
├── Restricción: ENTREGADO es final
└── Tests de validación PIN

# HU-016: Cancelar pedido (Cliente)
├── Solo PENDIENTE → CANCELADO
├── Mensaje error si no es PENDIENTE
├── Solo cliente propietario
└── Tests de restricciones
```

### Fase 6: traceability-service (Semana 3)

```bash
# HU-017: Consultar trazabilidad (Cliente)
├── Modelo TrazabilidadPedido
├── Registro cada cambio de estado
├── Eventos automáticos desde RabbitMQ
├── Solo trazabilidad de pedidos propios
└── Tests de eventos

# HU-018: Consultar eficiencia (Propietario)
├── Métricas tiempo por pedido
├── Ranking empleados por tiempo promedio
├── Cálculos tiempo por estado
├── Solo datos del restaurante propio
└── Tests de métricas
```

### Fase 7: messaging-service (Semana 3)

```bash
# SMS cuando pedido está listo
├── Consumer RabbitMQ para pedido LISTO
├── Integración con Twilio
├── Plantilla: "Pedido #{id} listo. PIN: {pin}"
├── Log de notificaciones enviadas
└── Tests con mocks de Twilio
```

### Fase 8: Integración Final (Semana 3)

```bash
# Integración completa
├── Configurar API Gateway completo
├── Tests de integración E2E
├── Documentación final
├── Optimización de rendimiento
└── Despliegue con Docker Compose
```

---

## ⚙️ Configuración del Entorno

### Prerrequisitos

```bash
# Software requerido
├── Java 17+ (OpenJDK recomendado)
├── Gradle 8.0+
├── PostgreSQL 15+ (instalado localmente)
├── RabbitMQ (CloudAMQP recomendado para desarrollo)
├── Git
└── IDE (IntelliJ IDEA recomendado)
```

### 1. Configuración de Infraestructura

#### 1.1 Configuración PostgreSQL Local

```bash
# 1. Instalar PostgreSQL 15+
# Windows: Descargar desde https://www.postgresql.org/download/windows/
# macOS: brew install postgresql@15
# Linux: sudo apt install postgresql-15 postgresql-contrib

# 2. Iniciar servicio PostgreSQL
# Windows: Automático después de instalación
# macOS: brew services start postgresql@15
# Linux: sudo systemctl start postgresql

# 3. Crear usuario y bases de datos
psql -U postgres -c "CREATE USER plazoleta_admin WITH PASSWORD 'dev123';"
psql -U postgres -c "ALTER USER plazoleta_admin CREATEDB;"
```

#### 1.2 Script de Inicialización de BD

```sql
-- scripts/init-databases.sql
-- Ejecutar como usuario postgres o plazoleta_admin

-- Crear bases de datos para cada microservicio
CREATE DATABASE usuarios_db WITH OWNER = plazoleta_admin ENCODING = 'UTF8';
CREATE DATABASE plazoleta_db WITH OWNER = plazoleta_admin ENCODING = 'UTF8';
CREATE DATABASE pedidos_db WITH OWNER = plazoleta_admin ENCODING = 'UTF8';
CREATE DATABASE trazabilidad_db WITH OWNER = plazoleta_admin ENCODING = 'UTF8';
CREATE DATABASE mensajeria_db WITH OWNER = plazoleta_admin ENCODING = 'UTF8';

-- Dar permisos completos al usuario
GRANT ALL PRIVILEGES ON DATABASE usuarios_db TO plazoleta_admin;
GRANT ALL PRIVILEGES ON DATABASE plazoleta_db TO plazoleta_admin;
GRANT ALL PRIVILEGES ON DATABASE pedidos_db TO plazoleta_admin;
GRANT ALL PRIVILEGES ON DATABASE trazabilidad_db TO plazoleta_admin;
GRANT ALL PRIVILEGES ON DATABASE mensajeria_db TO plazoleta_admin;
```

#### 1.3 Configuración RabbitMQ

**Opción 1: CloudAMQP (Recomendado para desarrollo)**

```bash
# 1. Ir a https://www.cloudamqp.com/
# 2. Crear cuenta gratuita (plan Little Lemur - gratis)
# 3. Crear instancia
# 4. Copiar URL de conexión (formato: amqps://user:pass@host/vhost)
```

**Opción 2: RabbitMQ Local**

```bash
# Windows:
# 1. Instalar Erlang desde https://www.erlang.org/downloads
# 2. Instalar RabbitMQ desde https://www.rabbitmq.com/download.html
# 3. Habilitar management plugin:
rabbitmq-plugins enable rabbitmq_management

# macOS:
brew install rabbitmq
brew services start rabbitmq
rabbitmq-plugins enable rabbitmq_management

# Linux:
sudo apt install rabbitmq-server
sudo systemctl start rabbitmq-server
sudo rabbitmq-plugins enable rabbitmq_management

# Acceder a management UI: http://localhost:15672 (guest/guest)
```

### 2. Estructura de Proyecto por Microservicio

#### 2.1 Creación desde Arquetipo

```bash
# Para cada microservicio, partir del arquetipo base
# Ejemplo: usuarios-service

# 1. Clonar arquetipo
git clone <url-arquetipo> usuarios-service
cd usuarios-service

# 2. Personalizar configuración Gradle
# Actualizar build.gradle con Java 17
# Configurar dependencias Spring Boot 3.2+
# Configurar puerto específico en application.yml

# 3. Configurar base de datos PostgreSQL
# Actualizar connection string
# Configurar entidades JPA específicas

# 4. Implementar lógica de negocio específica
# Modelos de dominio
# Casos de uso
# Adaptadores
```

#### 2.2 Configuración build.gradle Base

```gradle
// usuarios-service/build.gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
}

group = 'com.pragma.powerup'
version = '1.0.0'

java {
    sourceCompatibility = '17'
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-amqp'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'

    // Base de datos
    runtimeOnly 'org.postgresql:postgresql'

    // JWT
    implementation 'io.jsonwebtoken:jjwt-api:0.12.3'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.3'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.3'

    // MapStruct
    implementation 'org.mapstruct:mapstruct:1.5.5.Final'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'

    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // OpenAPI Documentation
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
    testImplementation 'org.testcontainers:junit-jupiter'
    testImplementation 'org.testcontainers:postgresql'
    testImplementation 'org.testcontainers:rabbitmq'
}

dependencyManagement {
    imports {
        mavenBom 'org.testcontainers:testcontainers-bom:1.19.0'
    }
}

tasks.named('test') {
    useJUnitPlatform()
}
```

#### 2.3 Configuración application.yml Base

```yaml
# usuarios-service/src/main/resources/application.yml
server:
    port: 8081

spring:
    application:
        name: usuarios-service

    datasource:
        url: jdbc:postgresql://localhost:5432/usuarios_db
        username: plazoleta_admin
        password: dev123
        driver-class-name: org.postgresql.Driver

    jpa:
        hibernate:
            ddl-auto: update
        show-sql: true
        properties:
            hibernate:
                dialect: org.hibernate.dialect.PostgreSQLDialect
                format_sql: true
        open-in-view: false

    rabbitmq:
        # Para CloudAMQP (recomendado)
        uri: ${RABBITMQ_URL:amqps://usuario:password@host/vhost}
        # Para RabbitMQ local (comentar la línea uri y descomentar estas)
        # host: localhost
        # port: 5672
        # username: guest
        # password: guest

    security:
        jwt:
            secret: ${JWT_SECRET:mi-secreto-super-seguro-para-jwt-tokens-java17}
            expiration: 86400000 # 24 horas

# Configuración específica del servicio
usuarios:
    admin:
        email: admin@plazoleta.com
        password: admin123
        create-on-startup: true

# OpenAPI Documentation
springdoc:
    api-docs:
        path: /api-docs
    swagger-ui:
        path: /swagger-ui.html

logging:
    level:
        com.pragma.powerup: DEBUG
        org.springframework.security: DEBUG

management:
    endpoints:
        web:
            exposure:
                include: health,info,metrics
```

### 3. Comandos Gradle Útiles

#### 3.1 Desarrollo Local

```bash
# Construir el proyecto
./gradlew build

# Ejecutar tests
./gradlew test

# Ejecutar aplicación
./gradlew bootRun

# Ejecutar con perfil específico
./gradlew bootRun --args='--spring.profiles.active=dev'

# Limpiar build
./gradlew clean

# Ver dependencias
./gradlew dependencies

# Generar reporte de tests
./gradlew test jacocoTestReport
```

---

## 🔐 Seguridad y Autorización

### Arquitectura de Seguridad

El sistema implementa **seguridad centralizada en API Gateway** con **autorización distribuida** en cada microservicio:

```
🔑 usuarios-service → Genera JWT con roles
🚪 API Gateway → Valida JWT + agrega headers
⚙️ Microservicios → Leen headers + autorizan endpoints específicos
```

### Flujo de Seguridad

1. **Login**: Cliente → Gateway → usuarios-service → JWT Token
2. **Request**: Cliente + JWT → Gateway (valida) → Microservicio + Headers
3. **Autorización**: Microservicio lee headers y autoriza según rol y lógica específica

### Estructura de Seguridad por Microservicio

#### usuarios-service (Autenticación)

```
src/main/java/com/pragma/powerup/
├── domain/
│   ├── model/
│   │   ├── UsuarioModel.java              ← Entidad con roles
│   │   └── RolEnum.java                   ← ADMIN, OWNER, EMPLOYEE, CUSTOMER
│   └── usecase/
│       └── AutenticacionUseCase.java      ← Lógica de login
├── application/
│   ├── dto/
│   │   ├── LoginRequestDto.java
│   │   └── AuthResponseDto.java
│   └── handler/
│       └── AuthHandler.java               ← Orquestador de auth
└── infrastructure/
    ├── input/rest/
    │   └── AuthController.java            ← POST /auth/login
    ├── security/
    │   ├── JwtService.java                ← Generar/validar tokens
    │   ├── SecurityConfig.java            ← Config Spring Security
    │   └── PasswordEncoder.java           ← BCrypt encoder
    └── out/jpa/
        └── UsuarioJpaAdapter.java         ← Persistencia usuarios
```

#### API Gateway (Validación JWT)

```
src/main/java/com/pragma/powerup/gateway/
├── filter/
│   └── JwtAuthenticationFilter.java      ← Valida JWT + agrega headers
├── security/
│   ├── JwtService.java                   ← Validar tokens
│   └── RouteAuthConfig.java              ← Permisos por ruta
└── config/
    └── GatewayConfig.java                ← Configuración de rutas
```

#### Microservicios (Autorización)

```
src/main/java/com/pragma/powerup/
├── infrastructure/
│   ├── input/rest/
│   │   └── [Entity]Controller.java       ← @PreAuthorize por endpoint
│   ├── security/
│   │   ├── SecurityConfig.java           ← Config mínima
│   │   ├── HeaderAuthFilter.java         ← Lee headers del Gateway
│   │   └── RoleConstants.java            ← Constantes de roles
│   └── out/feign/
│       └── UsuarioServiceClient.java     ← Validaciones complejas
├── application/
│   ├── handler/
│   │   └── [Entity]Handler.java          ← Validaciones adicionales
│   └── security/
│       └── AuthorizationService.java     ← Lógica de autorización
└── domain/
    └── usecase/
        └── [Entity]UseCase.java          ← Lógica pura (sin autorización)
```

### Implementaciones de Seguridad

#### 1. RolEnum (Domain - usuarios-service)

```java
// domain/model/RolEnum.java
public enum RolEnum {
    ADMINISTRADOR("ADMIN"),
    PROPIETARIO("OWNER"),
    EMPLEADO("EMPLOYEE"),
    CLIENTE("CUSTOMER");

    private final String authority;

    RolEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
```

#### 2. JwtService (Infrastructure - usuarios-service)

```java
// infrastructure/security/JwtService.java
@Component
public class JwtService {

    @Value("${spring.security.jwt.secret}")
    private String secretKey;

    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    public String generarToken(UsuarioModel usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId());
        claims.put("role", usuario.getRol().getAuthority());
        claims.put("email", usuario.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean esTokenValido(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public RolEnum extraerRol(String token) {
        String role = extraerClaim(token, claims -> claims.get("role", String.class));
        return Arrays.stream(RolEnum.values())
                .filter(r -> r.getAuthority().equals(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rol inválido: " + role));
    }

    public Long extraerUserId(String token) {
        return extraerClaim(token, claims -> claims.get("userId", Long.class));
    }
}
```

#### 3. JwtAuthenticationFilter (API Gateway)

```java
// gateway/filter/JwtAuthenticationFilter.java
@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    private final List<String> excludedPaths = List.of(
        "/api/v1/auth/login",
        "/api/v1/usuarios/register-cliente",
        "/swagger-ui", "/api-docs"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (excludedPaths.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Token no encontrado", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtService.esTokenValido(token)) {
                return onError(exchange, "Token inválido", HttpStatus.UNAUTHORIZED);
            }

            String email = jwtService.extraerEmail(token);
            RolEnum rol = jwtService.extraerRol(token);
            Long userId = jwtService.extraerUserId(token);

            if (!tienePermisoParaRuta(path, rol)) {
                return onError(exchange, "Acceso denegado", HttpStatus.FORBIDDEN);
            }

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId.toString())
                    .header("X-User-Email", email)
                    .header("X-User-Role", rol.getAuthority())
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            return onError(exchange, "Error de autenticación", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean tienePermisoParaRuta(String path, RolEnum rol) {
        if (path.startsWith("/api/v1/usuarios") && path.contains("/admin")) {
            return rol == RolEnum.ADMINISTRADOR;
        }

        if (path.startsWith("/api/v1/restaurantes") &&
            (path.contains("/create") || path.contains("/update"))) {
            return rol == RolEnum.ADMINISTRADOR || rol == RolEnum.PROPIETARIO;
        }

        if (path.startsWith("/api/v1/platos")) {
            return rol == RolEnum.PROPIETARIO || rol == RolEnum.ADMINISTRADOR;
        }

        return true;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
```

#### 4. RoleConstants (Infrastructure - cada microservicio)

```java
// infrastructure/security/RoleConstants.java
public final class RoleConstants {
    public static final String ADMIN = "ADMIN";
    public static final String OWNER = "OWNER";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String CUSTOMER = "CUSTOMER";

    public static final String ADMIN_OR_OWNER = "ADMIN or OWNER";
    public static final String OWNER_OR_EMPLOYEE = "OWNER or EMPLOYEE";
    public static final String ALL_ROLES = "ADMIN or OWNER or EMPLOYEE or CUSTOMER";

    private RoleConstants() {}
}
```

#### 5. SecurityConfig (Infrastructure - cada microservicio)

```java
// infrastructure/security/SecurityConfig.java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/swagger-ui/**", "/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new HeaderAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

#### 6. HeaderAuthenticationFilter (Infrastructure - cada microservicio)

```java
// infrastructure/security/HeaderAuthenticationFilter.java
@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String userEmail = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");

        if (userId != null && userEmail != null && userRole != null) {
            UserAuthenticationToken authentication = new UserAuthenticationToken(
                Long.valueOf(userId), userEmail, userRole
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
```

#### 7. Controller con Autorización (Infrastructure - ejemplo orders-service)

```java
// infrastructure/input/rest/PedidoController.java
@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final IPedidoHandler pedidoHandler;

    @PostMapping
    @PreAuthorize("hasRole('" + RoleConstants.CUSTOMER + "')")
    public ResponseEntity<PedidoResponseDto> crearPedido(
            @Valid @RequestBody PedidoRequestDto request,
            HttpServletRequest httpRequest) {

        Long userId = extractUserId(httpRequest);

        if (!request.getClienteId().equals(userId)) {
            throw new AccesoDenegadoException("No puedes crear pedidos para otro cliente");
        }

        var response = pedidoHandler.crearPedido(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/restaurante/{restauranteId}/pendientes")
    @PreAuthorize("hasRole('" + RoleConstants.OWNER_OR_EMPLOYEE + "')")
    public ResponseEntity<List<PedidoResponseDto>> obtenerPedidosPendientes(
            @PathVariable Long restauranteId,
            HttpServletRequest httpRequest) {

        Long userId = extractUserId(httpRequest);
        String userRole = extractUserRole(httpRequest);

        var pedidos = pedidoHandler.obtenerPedidosPendientes(restauranteId, userId, userRole);
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{pedidoId}/asignar")
    @PreAuthorize("hasRole('" + RoleConstants.EMPLOYEE + "')")
    public ResponseEntity<Void> asignarseAPedido(
            @PathVariable Long pedidoId,
            HttpServletRequest httpRequest) {

        Long empleadoId = extractUserId(httpRequest);
        pedidoHandler.asignarEmpleadoAPedido(pedidoId, empleadoId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{pedidoId}/cancelar")
    @PreAuthorize("hasRole('" + RoleConstants.CUSTOMER + "')")
    public ResponseEntity<Void> cancelarPedido(
            @PathVariable Long pedidoId,
            HttpServletRequest httpRequest) {

        Long clienteId = extractUserId(httpRequest);
        pedidoHandler.cancelarPedido(pedidoId, clienteId);
        return ResponseEntity.ok().build();
    }

    private Long extractUserId(HttpServletRequest request) {
        return Long.valueOf(request.getHeader("X-User-Id"));
    }

    private String extractUserRole(HttpServletRequest request) {
        return request.getHeader("X-User-Role");
    }
}
```

#### 8. AuthorizationService (Application - validaciones complejas)

```java
// application/security/AuthorizationService.java
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UsuarioServiceClient usuarioServiceClient;
    private final IPedidoPersistencePort pedidoPersistencePort;

    public boolean puedeAccederAPedidosDelRestaurante(Long userId, String userRole, Long restauranteId) {
        if ("ADMIN".equals(userRole)) {
            return true; // Admin puede acceder a todo
        }

        if ("OWNER".equals(userRole)) {
            // Validar que sea propietario del restaurante
            return usuarioServiceClient.esPropietarioDelRestaurante(userId, restauranteId);
        }

        if ("EMPLOYEE".equals(userRole)) {
            // Validar que sea empleado del restaurante
            return usuarioServiceClient.esEmpleadoDelRestaurante(userId, restauranteId);
        }

        return false;
    }

    public boolean puedeModificarPedido(Long userId, String userRole, Long pedidoId) {
        PedidoModel pedido = pedidoPersistencePort.obtenerPorId(pedidoId);

        if ("CUSTOMER".equals(userRole)) {
            // Cliente solo puede modificar sus propios pedidos
            return pedido.getClienteId().equals(userId) && pedido.puedeSerCancelado();
        }

        if ("EMPLOYEE".equals(userRole)) {
            // Empleado puede modificar pedidos de su restaurante
            return usuarioServiceClient.esEmpleadoDelRestaurante(userId, pedido.getRestauranteId());
        }

        return "ADMIN".equals(userRole); // Admin puede modificar cualquier pedido
    }
}
```

#### Activación rápida de seguridad (plantilla)

-   Microservicios (Spring MVC):

    1. Dependencia: `org.springframework.boot:spring-boot-starter-security` (ya presente en esta plantilla).
    2. Habilitar configuración incluida:
        - `infrastructure/security/RoleConstants.java`
        - `infrastructure/security/HeaderAuthenticationFilter.java`
        - `infrastructure/security/SecurityConfiguration.java`
    3. Flujo: El API Gateway valida JWT y agrega headers `X-User-Id`, `X-User-Email`, `X-User-Role`. El filtro los convierte en `Authentication`. Usa `@PreAuthorize` con roles de `RoleConstants`.
    4. Endpoints abiertos por defecto: `/actuator/**`, `/swagger-ui/**`, `/v3/api-docs/**`.

-   API Gateway (Spring WebFlux):
    1. Dependencias: `spring-cloud-starter-gateway`, `springdoc-openapi-starter-webflux-ui`.
    2. Implementar el filtro de JWT (ver ejemplo en esta misma sección: JwtAuthenticationFilter (API Gateway)).
    3. Al validar el token, inyectar `X-User-*` y reenviar a los microservicios.

### Configuración application.yml para cada microservicio

#### usuarios-service

```yaml
spring:
    security:
        jwt:
            secret: ${JWT_SECRET:mi-secreto-super-seguro-para-jwt-tokens-java17-usuarios}
            expiration: 86400000 # 24 horas

usuarios:
    admin:
        email: admin@plazoleta.com
        password: admin123
        create-on-startup: true
```

#### API Gateway

```yaml
spring:
    cloud:
        gateway:
            routes:
                - id: auth-service
                  uri: http://users-service:8081
                  predicates:
                      - Path=/api/v1/auth/**

                - id: users-service
                  uri: http://users-service:8081
                  predicates:
                      - Path=/api/v1/usuarios/**

                - id: restaurants-service
                  uri: http://restaurants-service:8082
                  predicates:
                      - Path=/api/v1/restaurantes/**, /api/v1/platos/**

                - id: orders-service
                  uri: http://orders-service:8083
                  predicates:
                      - Path=/api/v1/pedidos/**

                - id: traceability-service
                  uri: http://traceability-service:8084
                  predicates:
                      - Path=/api/v1/trazabilidad/**

                - id: messaging-service
                  uri: http://messaging-service:8085
                  predicates:
                      - Path=/api/v1/notificaciones/**

    security:
        jwt:
            secret: ${JWT_SECRET:mi-secreto-super-seguro-para-jwt-tokens-java17-usuarios}
```

#### Documentación centralizada (Swagger) en API Gateway

Para visualizar la documentación de todos los microservicios desde un único puerto (el del Gateway), añade Swagger UI al Gateway y proxyea los `v3/api-docs` de cada servicio.

Gradle del Gateway:

```gradle
implementation "org.springdoc:springdoc-openapi-starter-webflux-ui:2.3.0"
```

application.yml del Gateway (rutas para proxyear los docs):

```yaml
spring:
    cloud:
        gateway:
            routes:
                - id: users-docs
                  uri: http://localhost:8081
                  predicates:
                      - Path=/docs/users
                  filters:
                      - SetPath=/v3/api-docs

                - id: restaurants-docs
                  uri: http://localhost:8082
                  predicates:
                      - Path=/docs/restaurants
                  filters:
                      - SetPath=/v3/api-docs

                - id: orders-docs
                  uri: http://localhost:8083
                  predicates:
                      - Path=/docs/orders
                  filters:
                      - SetPath=/v3/api-docs

                - id: traceability-docs
                  uri: http://localhost:8084
                  predicates:
                      - Path=/docs/traceability
                  filters:
                      - SetPath=/v3/api-docs

                - id: messaging-docs
                  uri: http://localhost:8085
                  predicates:
                      - Path=/docs/messaging
                  filters:
                      - SetPath=/v3/api-docs

springdoc:
    swagger-ui:
        path: /swagger-ui.html
        urls:
            - name: users-service
              url: /docs/users
            - name: restaurants-service
              url: /docs/restaurants
            - name: orders-service
              url: /docs/orders
            - name: traceability-service
              url: /docs/traceability
            - name: messaging-service
              url: /docs/messaging
```

Acceso:

-   UI centralizada: `http://localhost:8080/swagger-ui.html`
-   JSON de cada servicio (proxyeado): `http://localhost:8080/docs/{service}`

Si utilizas Eureka/Service Discovery, reemplaza `uri: http://localhost:PORT` por `uri: lb://{service-name}`.

### Matriz de Permisos por Endpoint

| **Endpoint**                               | **ADMIN** | **OWNER** | **EMPLOYEE** | **CUSTOMER** |
| ------------------------------------------ | --------- | --------- | ------------ | ------------ |
| `POST /auth/login`                         | ✅        | ✅        | ✅           | ✅           |
| `POST /usuarios/admin`                     | ✅        | ❌        | ❌           | ❌           |
| `POST /usuarios/propietario`               | ✅        | ❌        | ❌           | ❌           |
| `POST /usuarios/empleado`                  | ❌        | ✅        | ❌           | ❌           |
| `POST /usuarios/cliente`                   | ✅        | ✅        | ✅           | ✅           |
| `POST /restaurantes`                       | ✅        | ❌        | ❌           | ❌           |
| `POST /platos`                             | ✅        | ✅\*      | ❌           | ❌           |
| `PUT /platos/{id}`                         | ✅        | ✅\*      | ❌           | ❌           |
| `GET /platos/restaurante/{id}`             | ✅        | ✅        | ✅           | ✅           |
| `POST /pedidos`                            | ❌        | ❌        | ❌           | ✅\*         |
| `GET /pedidos/restaurante/{id}/pendientes` | ✅        | ✅\*      | ✅\*         | ❌           |
| `PUT /pedidos/{id}/asignar`                | ❌        | ❌        | ✅\*         | ❌           |
| `PUT /pedidos/{id}/cancelar`               | ❌        | ❌        | ❌           | ✅\*         |
| `PUT /pedidos/{id}/estado`                 | ❌        | ❌        | ✅\*         | ❌           |
| `GET /trazabilidad/pedido/{id}`            | ✅        | ✅\*      | ✅\*         | ✅\*         |

**Leyenda:**

-   ✅ = Permitido siempre
-   ✅\* = Permitido con validaciones adicionales (ej: solo sus propios recursos)
-   ❌ = Denegado

---

## 🔗 Comunicación entre Microservicios

### RabbitMQ (Comunicación Asíncrona)

#### Configuración por Microservicio

```gradle
// build.gradle - Agregar en todos los microservicios que usen RabbitMQ
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-amqp'
}
```

#### Configuración de Colas y Exchanges

```java
// infrastructure/messaging/RabbitMQConfig.java
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // Exchanges
    public static final String PEDIDOS_EXCHANGE = "pedidos.exchange";
    public static final String USUARIOS_EXCHANGE = "usuarios.exchange";

    // Routing Keys
    public static final String PEDIDO_CREADO_KEY = "pedido.creado";
    public static final String PEDIDO_ESTADO_CAMBIADO_KEY = "pedido.estado.cambiado";
    public static final String USUARIO_REGISTRADO_KEY = "usuario.registrado";

    // Queues
    public static final String TRAZABILIDAD_PEDIDO_CREADO_QUEUE = "trazabilidad.pedido.creado";
    public static final String MENSAJERIA_PEDIDO_CREADO_QUEUE = "mensajeria.pedido.creado";
    public static final String MENSAJERIA_PEDIDO_ESTADO_QUEUE = "mensajeria.pedido.estado";

    @Bean
    public TopicExchange pedidosExchange() {
        return new TopicExchange(PEDIDOS_EXCHANGE);
    }

    @Bean
    public Queue trazabilidadPedidoCreadoQueue() {
        return QueueBuilder.durable(TRAZABILIDAD_PEDIDO_CREADO_QUEUE).build();
    }

    @Bean
    public Queue mensajeriaPedidoCreadoQueue() {
        return QueueBuilder.durable(MENSAJERIA_PEDIDO_CREADO_QUEUE).build();
    }

    @Bean
    public Binding trazabilidadPedidoCreadoBinding() {
        return BindingBuilder
                .bind(trazabilidadPedidoCreadoQueue())
                .to(pedidosExchange())
                .with(PEDIDO_CREADO_KEY);
    }

    @Bean
    public Binding mensajeriaPedidoCreadoBinding() {
        return BindingBuilder
                .bind(mensajeriaPedidoCreadoQueue())
                .to(pedidosExchange())
                .with(PEDIDO_CREADO_KEY);
    }
}
```

#### Event DTOs (Shared entre microservicios)

```java
// events/PedidoCreadoEvent.java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoCreadoEvent {
    private Long pedidoId;
    private Long clienteId;
    private Long restauranteId;
    private LocalDateTime timestamp;
    private List<DetallePedidoEvent> detalles;
}

// events/PedidoEstadoCambiadoEvent.java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoEstadoCambiadoEvent {
    private Long pedidoId;
    private Long clienteId;
    private Long restauranteId;
    private String estadoAnterior;
    private String estadoNuevo;
    private Long empleadoId;
    private LocalDateTime timestamp;
    private String pin; // Solo cuando estado = LISTO
}
```

#### Publisher (pedidos-service)

```java
// infrastructure/messaging/PedidoEventPublisher.java
@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarPedidoCreado(PedidoModel pedido) {
        var evento = PedidoCreadoEvent.builder()
                .pedidoId(pedido.getId())
                .clienteId(pedido.getClienteId())
                .restauranteId(pedido.getRestauranteId())
                .timestamp(LocalDateTime.now())
                .detalles(mapearDetalles(pedido.getDetalles()))
                .build();

        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.PEDIDOS_EXCHANGE,
                RabbitMQConfig.PEDIDO_CREADO_KEY,
                evento
            );
            log.info("Evento pedido creado publicado: {}", evento.getPedidoId());
        } catch (Exception e) {
            log.error("Error publicando evento pedido creado: {}", e.getMessage());
            // Aquí podrías implementar retry o dead letter queue
        }
    }

    public void publicarCambioEstado(PedidoModel pedido, EstadoPedido estadoAnterior, Long empleadoId) {
        var evento = PedidoEstadoCambiadoEvent.builder()
                .pedidoId(pedido.getId())
                .clienteId(pedido.getClienteId())
                .restauranteId(pedido.getRestauranteId())
                .estadoAnterior(estadoAnterior.name())
                .estadoNuevo(pedido.getEstado().name())
                .empleadoId(empleadoId)
                .timestamp(LocalDateTime.now())
                .pin(pedido.getEstado() == EstadoPedido.LISTO ? pedido.getPin() : null)
                .build();

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.PEDIDOS_EXCHANGE,
            RabbitMQConfig.PEDIDO_ESTADO_CAMBIADO_KEY,
            evento
        );
    }
}
```

#### Consumer (trazabilidad-service)

```java
// infrastructure/messaging/TrazabilidadEventConsumer.java
@Component
@RequiredArgsConstructor
@Slf4j
public class TrazabilidadEventConsumer {

    private final ITrazabilidadHandler trazabilidadHandler;

    @RabbitListener(queues = RabbitMQConfig.TRAZABILIDAD_PEDIDO_CREADO_QUEUE)
    public void procesarPedidoCreado(PedidoCreadoEvent evento) {
        try {
            log.info("Procesando evento pedido creado: {}", evento.getPedidoId());

            var registro = TrazabilidadRegistroDto.builder()
                    .pedidoId(evento.getPedidoId())
                    .estadoAnterior(null)
                    .estadoNuevo("PENDIENTE")
                    .timestamp(evento.getTimestamp())
                    .build();

            trazabilidadHandler.registrarCambioEstado(registro);

        } catch (Exception e) {
            log.error("Error procesando evento pedido creado: {}", e.getMessage());
            throw new RuntimeException("Error procesando evento", e); // Para reintento
        }
    }

    @RabbitListener(queues = "trazabilidad.pedido.estado")
    public void procesarCambioEstado(PedidoEstadoCambiadoEvent evento) {
        try {
            var registro = TrazabilidadRegistroDto.builder()
                    .pedidoId(evento.getPedidoId())
                    .estadoAnterior(evento.getEstadoAnterior())
                    .estadoNuevo(evento.getEstadoNuevo())
                    .empleadoId(evento.getEmpleadoId())
                    .timestamp(evento.getTimestamp())
                    .build();

            trazabilidadHandler.registrarCambioEstado(registro);

        } catch (Exception e) {
            log.error("Error procesando cambio de estado: {}", e.getMessage());
            throw new RuntimeException("Error procesando evento", e);
        }
    }
}
```

#### Consumer (mensajeria-service)

```java
// infrastructure/messaging/MensajeriaEventConsumer.java
@Component
@RequiredArgsConstructor
@Slf4j
public class MensajeriaEventConsumer {

    private final INotificacionHandler notificacionHandler;

    @RabbitListener(queues = RabbitMQConfig.MENSAJERIA_PEDIDO_CREADO_QUEUE)
    public void procesarPedidoCreado(PedidoCreadoEvent evento) {
        try {
            var notificacion = NotificacionRequestDto.builder()
                    .tipoEvento("PEDIDO_CREADO")
                    .pedidoId(evento.getPedidoId())
                    .clienteId(evento.getClienteId())
                    .restauranteId(evento.getRestauranteId())
                    .parametros(Map.of("pedidoId", evento.getPedidoId().toString()))
                    .build();

            notificacionHandler.procesarNotificacion(notificacion);

        } catch (Exception e) {
            log.error("Error enviando notificación pedido creado: {}", e.getMessage());
            throw new RuntimeException("Error procesando notificación", e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.MENSAJERIA_PEDIDO_ESTADO_QUEUE)
    public void procesarCambioEstado(PedidoEstadoCambiadoEvent evento) {
        try {
            if ("LISTO".equals(evento.getEstadoNuevo())) {
                // Enviar SMS con PIN
                var smsDto = SMSRequestDto.builder()
                        .clienteId(evento.getClienteId())
                        .mensaje(String.format("Su pedido #%d está listo. PIN: %s",
                                              evento.getPedidoId(), evento.getPin()))
                        .build();

                notificacionHandler.enviarSMS(smsDto);
            }

        } catch (Exception e) {
            log.error("Error enviando SMS pedido listo: {}", e.getMessage());
        }
    }
}
```

### Feign Client (Comunicación Síncrona)

#### Configuración

```gradle
// build.gradle - Microservicios que necesiten llamar a otros
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
}
```

#### Clientes Feign

```java
// infrastructure/out/feign/UsuarioServiceClient.java
@FeignClient(name = "users-service", url = "${microservices.users.url:http://localhost:8081}")
public interface UsuarioServiceClient {

    @GetMapping("/api/v1/usuarios/{id}")
    UsuarioResponseDto obtenerUsuario(@PathVariable Long id);

    @GetMapping("/api/v1/usuarios/{id}/activo")
    Boolean esUsuarioActivo(@PathVariable Long id);

    @GetMapping("/api/v1/usuarios/validar-empleado/{empleadoId}/restaurante/{restauranteId}")
    Boolean esEmpleadoDelRestaurante(@PathVariable Long empleadoId, @PathVariable Long restauranteId);

    @GetMapping("/api/v1/usuarios/validar-propietario/{propietarioId}/restaurante/{restauranteId}")
    Boolean esPropietarioDelRestaurante(@PathVariable Long propietarioId, @PathVariable Long restauranteId);
}

// infrastructure/out/feign/PlazaletaServiceClient.java
@FeignClient(name = "restaurants-service", url = "${microservices.restaurants.url:http://localhost:8082}")
public interface PlazaletaServiceClient {

    @GetMapping("/api/v1/platos/{id}")
    PlatoResponseDto obtenerPlato(@PathVariable Long id);

    @GetMapping("/api/v1/platos/{id}/activo")
    Boolean esPlatoActivo(@PathVariable Long id);

    @GetMapping("/api/v1/restaurantes/{id}")
    RestauranteResponseDto obtenerRestaurante(@PathVariable Long id);

    @PostMapping("/api/v1/platos/validar-disponibilidad")
    Boolean validarDisponibilidadPlatos(@RequestBody List<Long> platosIds);
}
```

#### Adaptadores Feign

```java
// infrastructure/out/feign/adapter/UsuarioServiceAdapter.java
@Component
@RequiredArgsConstructor
public class UsuarioServiceAdapter implements IUsuarioServicePort {

    private final UsuarioServiceClient usuarioServiceClient;

    @Override
    public boolean esClienteActivo(Long clienteId) {
        try {
            UsuarioResponseDto usuario = usuarioServiceClient.obtenerUsuario(clienteId);
            return usuario != null && usuario.isActivo() && "CUSTOMER".equals(usuario.getRol());
        } catch (FeignException.NotFound e) {
            log.warn("Usuario no encontrado: {}", clienteId);
            return false;
        } catch (Exception e) {
            log.error("Error validando cliente: {}", e.getMessage());
            throw new IntegracionException("Error comunicándose con usuarios-service");
        }
    }

    @Override
    public boolean esEmpleadoDelRestaurante(Long empleadoId, Long restauranteId) {
        try {
            return usuarioServiceClient.esEmpleadoDelRestaurante(empleadoId, restauranteId);
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            log.error("Error validando empleado del restaurante: {}", e.getMessage());
            return false; // Fallar de forma segura
        }
    }
}
```

#### Configuración de Timeouts y Retry

```yaml
# application.yml - Para microservicios que usen Feign
feign:
    client:
        config:
            default:
                connectTimeout: 5000
                readTimeout: 10000
                loggerLevel: basic
            users-service:
                connectTimeout: 3000
                readTimeout: 5000
            restaurants-service:
                connectTimeout: 3000
                readTimeout: 5000

# URLs de microservicios
microservices:
    users:
        url: http://localhost:8081
    restaurants:
        url: http://localhost:8082
    orders:
        url: http://localhost:8083
    traceability:
        url: http://localhost:8084
    messaging:
        url: http://localhost:8085
```

### Cuándo usar cada tipo de comunicación

| **Escenario**                       | **Tipo**  | **Tecnología** | **Ejemplo**                                  |
| ----------------------------------- | --------- | -------------- | -------------------------------------------- |
| **Validación en tiempo real**       | Síncrono  | Feign          | Validar usuario existe antes de crear pedido |
| **Datos necesarios para continuar** | Síncrono  | Feign          | Obtener datos del plato para calcular precio |
| **Notificaciones**                  | Asíncrono | RabbitMQ       | Enviar SMS cuando pedido está listo          |
| **Auditoría/Logs**                  | Asíncrono | RabbitMQ       | Registrar cambios de estado en trazabilidad  |
| **Procesos que pueden fallar**      | Asíncrono | RabbitMQ       | Envío de emails promocionales                |
| **Operaciones que no bloquean**     | Asíncrono | RabbitMQ       | Actualizar métricas y reportes               |

---

## 🚀 Desarrollo por Fases

### Metodología de Trabajo

#### 1. Flujo Git por Historia de Usuario

```bash
# Para cada HU (Historia de Usuario)
git checkout -b feature/HU-001-crear-cuenta-administrador

# Desarrollo siguiendo TDD
├── 1. Escribir tests que fallen
├── 2. Implementar código mínimo para que pasen
├── 3. Refactorizar manteniendo tests verdes
└── 4. Documentar en OpenAPI

# Al finalizar HU
git add .
git commit -m "feat(HU-001): implementar creación de cuenta administrador"
git push origin feature/HU-001-crear-cuenta-administrador

# Crear Pull Request con:
├── Descripción de la funcionalidad
├── Tests de cobertura >80%
├── Documentación OpenAPI actualizada
└── Screenshots de Postman/Swagger
```

#### 2. Estructura de Tests

```java
// Ejemplo estructura de test
@SpringBootTest
@Testcontainers
class CrearUsuarioAdministradorUseCaseTest {

         @Container
     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
             .withDatabaseName("test_db")
             .withUsername("test")
             .withPassword("test");

    @Test
    @DisplayName("Debe crear administrador cuando datos son válidos")
    void debeCrearAdministradorCuandoDatosSonValidos() {
        // Given
        var requestDto = UsuarioRequestDto.builder()
                .nombre("Admin")
                .apellido("Sistema")
                .email("admin@test.com")
                .build();

        // When
        var resultado = crearUsuarioUseCase.crearAdministrador(requestDto);

        // Then
        assertThat(resultado.getEmail()).isEqualTo("admin@test.com");
        assertThat(resultado.getRol()).isEqualTo(RolEnum.ADMINISTRADOR);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando email ya existe")
    void debeLanzarExcepcionCuandoEmailYaExiste() {
        // Given - usuario existente
        // When & Then
        assertThatThrownBy(() -> crearUsuarioUseCase.crearAdministrador(requestDto))
                .isInstanceOf(UsuarioYaExisteException.class)
                .hasMessage("El email ya está registrado en el sistema");
    }
}
```

### 3. Comunicación entre Microservicios

#### 3.1 Eventos de Dominio (RabbitMQ)

```java
// Ejemplo: Cuando se crea un pedido
@Component
public class PedidoCreadoEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publicarPedidoCreado(Pedido pedido) {
        var evento = PedidoCreadoEvent.builder()
                .pedidoId(pedido.getId())
                .clienteId(pedido.getClienteId())
                .restauranteId(pedido.getRestauranteId())
                .timestamp(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend("pedidos.exchange",
                                     "pedido.creado",
                                     evento);
    }
}

// Consumidor en trazabilidad-service
@RabbitListener(queues = "trazabilidad.pedido.creado")
public void procesarPedidoCreado(PedidoCreadoEvent evento) {
    var trazabilidad = new TrazabilidadPedido(
        evento.getPedidoId(),
        null,
        EstadoPedido.PENDIENTE,
        evento.getTimestamp()
    );

    trazabilidadRepository.save(trazabilidad);
}
```

#### 3.2 Comunicación Síncrona (Feign Client)

```java
// Cliente para comunicarse con usuarios-service
@FeignClient(name = "usuarios-service", path = "/api/v1/usuarios")
public interface UsuarioServiceClient {

    @GetMapping("/{id}")
    UsuarioResponseDto obtenerUsuarioPorId(@PathVariable Long id);

    @GetMapping("/validar-empleado/{empleadoId}/restaurante/{restauranteId}")
    Boolean validarEmpleadoRestaurante(@PathVariable Long empleadoId,
                                     @PathVariable Long restauranteId);
}
```

---

## 🧪 Testing y Despliegue

### Estrategia de Testing

#### 1. Pirámide de Testing

```
                    🔺 E2E Tests (5%)
                   /               \
                  /  Integration     \
                 /   Tests (25%)      \
                /                      \
               /_______________________\
              Unit Tests (70%)
```

#### 2. Tests Unitarios (70%)

-   **Casos de Uso**: Lógica de negocio pura
-   **Mappers**: Transformaciones entre capas
-   **Validadores**: Reglas de negocio
-   **Utilidades**: Funciones helper

#### 3. Tests de Integración (25%)

-   **Repositorios**: Con TestContainers
-   **API REST**: Con MockMvc
-   **Mensajería**: Con RabbitMQ embebido
-   **Servicios externos**: Con WireMock

#### 4. Tests E2E (5%)

-   **Flujos completos**: Usuario a base de datos
-   **Integración entre microservicios**
-   **Tests de contrato**: Con Pact

### Pipeline CI/CD

### Reglas de Arquitectura (ArchUnit)

-   Objetivo: reforzar arquitectura hexagonal.
-   Reglas incluidas:
    -   `domain` no depende de `application` ni `infrastructure`.
    -   `application` no depende de `infrastructure`.
    -   No hay ciclos entre paquetes.
-   Ubicación: `src/test/java/com/pragma/powerup/architecture/HexagonalArchitectureTest.java`.
-   Ejecución: `./gradlew test` (se ejecuta junto a los demás tests).

#### 1. GitHub Actions Base

```yaml
# .github/workflows/ci.yml
name: CI Pipeline

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest

         services:
       postgres:
         image: postgres:15
         env:
           POSTGRES_PASSWORD: postgres
           POSTGRES_USER: postgres
           POSTGRES_DB: test_db
         options: >-
           --health-cmd="pg_isready -U postgres"
           --health-interval=10s
           --health-timeout=5s
           --health-retries=3

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: "17"
          distribution: "temurin"

      - name: Cache Gradle packages
        uses: actions/cache@v3
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}

      - name: Run tests
        run: ./gradlew test

      - name: Generate test report
        uses: dorny/test-reporter@v1
        if: success() || failure()
        with:
          name: Gradle Tests
          path: build/test-results/test/*.xml
          reporter: java-junit

      - name: Upload coverage reports
        uses: codecov/codecov-action@v3
```

### Estilo y Cobertura

-   Formato: Spotless con Google Java Format (`./gradlew spotlessApply` para auto-fix, `spotlessCheck` en CI).
-   Cobertura: JaCoCo con umbral mínimo del 80% (tarea `jacocoCoverageVerification` integrada a `check`).
-   Validación OpenAPI: `./gradlew openApiValidateAll` (también en CI).

### Despliegue

#### 1. Docker por Microservicio

```dockerfile
# Dockerfile para cada microservicio
FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

#### 2. Script de Despliegue Local (sin Docker)

```bash
# deploy-local.sh
#!/bin/bash

echo "🚀 Iniciando despliegue local de microservicios..."

# Verificar PostgreSQL
echo "📊 Verificando PostgreSQL..."
pg_isready -h localhost -p 5432 -U plazoleta_admin || {
    echo "❌ PostgreSQL no está disponible"
    exit 1
}

# Crear bases de datos si no existen
echo "🗄️ Inicializando bases de datos..."
psql -U postgres -f scripts/init-databases.sql

# Construir todos los servicios
echo "🔨 Construyendo microservicios..."
for service in users-service restaurants-service orders-service traceability-service messaging-service; do
    echo "  📦 Construyendo $service..."
    cd $service && ./gradlew build -x test && cd ..
done

# Ejecutar servicios en segundo plano
echo "🎬 Iniciando microservicios..."

cd users-service && nohup ./gradlew bootRun > logs/users.log 2>&1 & echo $! > users.pid && cd ..
sleep 10

cd restaurants-service && nohup ./gradlew bootRun > logs/restaurants.log 2>&1 & echo $! > restaurants.pid && cd ..
sleep 10

cd orders-service && nohup ./gradlew bootRun > logs/orders.log 2>&1 & echo $! > orders.pid && cd ..
sleep 10

cd traceability-service && nohup ./gradlew bootRun > logs/traceability.log 2>&1 & echo $! > traceability.pid && cd ..
sleep 10

cd messaging-service && nohup ./gradlew bootRun > logs/messaging.log 2>&1 & echo $! > messaging.pid && cd ..

echo "✅ Todos los microservicios iniciados!"
echo "📚 Documentación disponible en:"
echo "  - users-service: http://localhost:8081/swagger-ui.html"
echo "  - restaurants-service: http://localhost:8082/swagger-ui.html"
echo "  - orders-service: http://localhost:8083/swagger-ui.html"
echo "  - traceability-service: http://localhost:8084/swagger-ui.html"
echo "  - messaging-service: http://localhost:8085/swagger-ui.html"
```

#### 3. Script de Parada

```bash
# stop-services.sh
#!/bin/bash

echo "🛑 Deteniendo microservicios..."

# Leer PIDs y detener servicios
for service in usuarios plazoleta pedidos trazabilidad mensajeria; do
    if [ -f "${service}.pid" ]; then
        pid=$(cat ${service}.pid)
        echo "  🔴 Deteniendo ${service}-service (PID: $pid)..."
        kill $pid 2>/dev/null
        rm ${service}.pid
    fi
done

echo "✅ Todos los microservicios detenidos!"
```

---

## 📚 Recursos y Referencias

### Documentación Técnica

-   **OpenAPI**: Cada microservicio expone documentación en `/swagger-ui.html`
-   **Postman Collections**: Colecciones por microservicio en `/docs/postman/`
-   **Diagramas**: Arquitectura y flujos en `/docs/diagrams/`

### Comandos Útiles

#### Desarrollo Local

```bash
# Inicializar base de datos PostgreSQL
psql -U postgres -f scripts/init-databases.sql

# Ejecutar tests
./gradlew test

# Ejecutar microservicio específico
cd usuarios-service && ./gradlew bootRun

# Ejecutar con perfil dev
./gradlew bootRun --args='--spring.profiles.active=dev'

# Ver documentación OpenAPI
# http://localhost:8081/swagger-ui.html
```

#### Desarrollo con Múltiples Servicios

```bash
# Terminal 1: users-service
cd users-service && ./gradlew bootRun

# Terminal 2: restaurants-service
cd restaurants-service && ./gradlew bootRun

# Terminal 3: orders-service
cd orders-service && ./gradlew bootRun

# Terminal 4: traceability-service
cd traceability-service && ./gradlew bootRun

# Terminal 5: messaging-service
cd messaging-service && ./gradlew bootRun
```

### Monitoreo y Observabilidad

-   **Health Checks**: `/actuator/health` en cada servicio
-   **Métricas**: `/actuator/metrics` con Micrometer
-   **Logs estructurados (JSON)**: salida por consola con `logback-spring.xml` y campos MDC (`service`, `traceId`, `spanId`, `requestId`, `userId`, `orderId`).
-   **Tracing distribuido (OTel)**: Micrometer Tracing + OTLP. Configurable vía `OTEL_EXPORTER_OTLP_ENDPOINT`.
-   **Stack recomendado**: Grafana Loki + Promtail (logs) y Tempo (traces) con Grafana para visualización y correlación logs↔traces.

---

## 🎯 Próximos Pasos

1. **✅ Configurar entorno local** siguiendo la sección de configuración
2. **🔧 Crear primer microservicio** (usuarios-service) desde el arquetipo
3. **📝 Implementar primera HU** siguiendo TDD
4. **🧪 Configurar pipeline CI/CD** con GitHub Actions
5. **🚀 Iterar** implementando una HU por rama hasta completar el sistema

---

## 📦 Template Usage (multi-repo)

Este repositorio es plantilla para microservicios (no monorepo). Para crear un nuevo proyecto:

1. Clonar como base

```
git clone <este-repo> my-service
cd my-service
```

2. Renombrar identidad del servicio

-   Cambia paquete `com.pragma.powerup` a `com.pragma.<service>`
-   Edita `spring.application.name` y `PORT` en `.env`
-   Ajusta `DB_URL/DB_USERNAME/DB_PASSWORD`

3. Documentación y contrato

-   Duplica/edita `docs/openapi/<service>.yaml`
-   Ejecuta `./gradlew openApiValidateAll`

4. Verificación

-   `./gradlew test` (incluye ArchUnit)
-   Arranca y verifica `http://localhost:${PORT}/swagger-ui.html`

Plantillas de build disponibles en `docs/templates/`:

-   `build.gradle.microservice` (Spring MVC + JPA + MapStruct + springdoc)
-   `build.gradle.gateway` (Spring WebFlux + Spring Cloud Gateway + springdoc-webflux)

---

**¿Necesitas las Historias de Usuario detalladas para comenzar? 📋**

¡El plan está listo para ejecutar! Este README te guiará paso a paso desde la configuración inicial hasta el despliegue completo del sistema.

---

## 📘 Documentación por Microservicio

Para especificaciones detalladas de cada microservicio (endpoints, modelos, casos de uso, eventos, seguridad, pruebas) y del API Gateway, consulta:

-   docs/README.microservicios.md

---

## 🔐 Seguridad (Plantilla)

-   Microservicios: `HeaderAuthenticationFilter`, `SecurityConfiguration`, `RoleConstants` (incluidos) leen `X-User-*` y permiten usar `@PreAuthorize`.
-   Gateway: validar JWT y propagar `X-User-*` (ver ejemplo de filtro en esta guía arriba).

## 🧰 Contribución

Consulta `docs/CONTRIBUTING.md` para convención de ramas/commits, formato, pruebas y validación OpenAPI.
