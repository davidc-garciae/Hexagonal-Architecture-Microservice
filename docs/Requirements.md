# 📋 Historias de Usuario - Sistema de Plazoleta de Comidas

## 📅 Distribución por Semanas

### **Semana 1 - Gestión Inicial**

-   HU-001: Crear Propietario (Administrador)
-   HU-002: Crear Restaurante (Administrador)
-   HU-003: Crear Plato (Propietario)
-   HU-004: Modificar Plato (Propietario)
-   HU-005: Agregar Autenticación al Sistema (Todos los roles)

### **Semana 2 - Gestión Avanzada**

-   HU-006: Crear cuenta empleado (Propietario)
-   HU-007: Habilitar/Deshabilitar Plato (Propietario)
-   HU-008: Crear cuenta Cliente (Cliente)
-   HU-009: Listar los restaurantes (Cliente)
-   HU-010: Listar platos por restaurante (Cliente)
-   HU-011: Realizar pedido (Cliente)
-   HU-012: Obtener lista de pedidos por estado (Empleado)

### **Semana 3 - Flujo de Pedidos**

-   HU-013: Asignarse a pedido y cambiar a "EN_PREPARACION" (Empleado)
-   HU-014: Notificar pedido listo (Empleado)
-   HU-015: Entregar pedido (Empleado)
-   HU-016: Cancelar pedido (Cliente)
-   HU-017: Consultar trazabilidad (Cliente)
-   HU-018: Consultar eficiencia de pedidos (Propietario)

---

## 📝 Historias de Usuario Detalladas

### **HU-001: Crear Propietario**

**Como:** Administrador  
**Quiero:** Crear cuenta para un propietario  
**Para:** Poder crear un restaurante a cargo de un propietario

**Criterios de Aceptación:**

1. **Campos obligatorios:** Nombre, Apellido, DocumentoDeIdentidad, celular, fechaNacimiento, correo, clave
2. **Validaciones:**
    - Email con estructura válida
    - Teléfono máximo 13 caracteres, puede contener símbolo + (ej: +573005698325)
    - Documento de identidad únicamente numérico
    - Usuario debe ser mayor de edad
3. **Resultado:** Usuario queda con rol PROPIETARIO
4. **Seguridad:** Clave encriptada con BCrypt

**Microservicio:** usuarios-service  
**Endpoint:** `POST /api/v1/usuarios/propietario`

---

### **HU-002: Crear Restaurante**

**Como:** Administrador  
**Quiero:** Crear restaurantes en el sistema  
**Para:** Brindar al cliente posibilidad de escoger dónde pedir

**Criterios de Aceptación:**

1. **Campos obligatorios:** Nombre, NIT, Direccion, Telefono, UrlLogo, idPropietario
2. **Validaciones:**
    - Validar que idPropietario corresponda a usuario con rol PROPIETARIO
    - NIT y Telefono únicamente numéricos
    - Telefono máximo 13 caracteres, puede contener + (ej: +573005698325)
    - Nombre puede contener números, pero no solo números

**Microservicio:** plazoleta-service  
**Endpoint:** `POST /api/v1/restaurantes`

---

### **HU-003: Crear Plato**

**Como:** Propietario  
**Quiero:** Crear platos para mi restaurante  
**Para:** Brindar diferentes opciones al cliente

**Criterios de Aceptación:**

1. **Autorización:** Solo el propietario del restaurante puede crear platos
2. **Campos obligatorios:** Nombre, precio, descripción, urlImagen, categoria
3. **Validaciones:**
    - Precio en números enteros positivos mayor a 0
    - Plato debe estar asociado a un restaurante
4. **Estado inicial:** activo = true por defecto

**Microservicio:** plazoleta-service  
**Endpoint:** `POST /api/v1/platos`

---

### **HU-004: Modificar Plato**

**Como:** Propietario  
**Quiero:** Actualizar información de platos  
**Para:** Corregir valores o actualizar precios

**Criterios de Aceptación:**

1. **Campos modificables:** Solo precio y descripción
2. **Autorización:** Solo propietario del restaurante del plato

**Microservicio:** plazoleta-service  
**Endpoint:** `PUT /api/v1/platos/{id}`

---

### **HU-005: Agregar Autenticación**

**Como:** Administrador/Cliente/Propietario/Empleado  
**Quiero:** Autenticación en el sistema  
**Para:** Acceder a funcionalidades según mi rol

**Criterios de Aceptación:**

1. **Login:** Correo y clave
2. **Validación:** Usuario y contraseña correcta
3. **Intentos:** Ilimitados
4. **Permisos:** Cada usuario accede solo a sus funcionalidades de rol
5. **Endpoints protegidos:**
    - Crear propietario → Solo ADMIN
    - Crear empleado → Solo PROPIETARIO
    - Crear restaurante → Solo ADMIN
    - Crear/modificar plato → Solo PROPIETARIO del restaurante
6. **Nota:** No contempla recuperación de contraseña

**Microservicio:** usuarios-service + API Gateway  
**Endpoint:** `POST /api/v1/auth/login`

---

### **HU-006: Crear Cuenta Empleado**

**Como:** Propietario  
**Quiero:** Crear cuentas para empleados  
**Para:** Que administren los pedidos de mi restaurante

**Criterios de Aceptación:**

1. **Autorización:** Solo propietario puede crear empleados para su empresa
2. **Campos obligatorios:** Nombre, Apellido, DocumentoDeIdentidad, celular, correo, idRol, clave
3. **Resultado:** Usuario queda con rol EMPLEADO
4. **Seguridad:** Clave encriptada con BCrypt

**Microservicio:** usuarios-service  
**Endpoint:** `POST /api/v1/usuarios/empleado`

---

### **HU-007: Habilitar/Deshabilitar Plato**

**Como:** Propietario  
**Quiero:** Activar/desactivar platos  
**Para:** Dejar de ofrecer productos en el menú

**Criterios de Aceptación:**

1. **Autorización:** Solo propietario puede habilitar/deshabilitar platos
2. **Restricción:** No modificar platos de otros restaurantes

**Microservicio:** plazoleta-service  
**Endpoint:** `PATCH /api/v1/platos/{id}/estado`

---

### **HU-008: Crear Cuenta Cliente**

**Como:** Cliente  
**Quiero:** Crear mi cuenta  
**Para:** Acceder al sistema y realizar pedidos

**Criterios de Aceptación:**

1. **Campos obligatorios:** Nombre, Apellido, DocumentoDeIdentidad, celular, correo, idRol, clave
2. **Resultado:** Usuario queda con rol CLIENTE
3. **Seguridad:** Clave encriptada con BCrypt

**Microservicio:** usuarios-service  
**Endpoint:** `POST /api/v1/usuarios/cliente`

---

### **HU-009: Listar Restaurantes**

**Como:** Cliente  
**Quiero:** Ver restaurantes disponibles  
**Para:** Elegir dónde ordenar

**Criterios de Aceptación:**

1. **Ordenamiento:** Alfabético
2. **Paginación:** Especificar elementos por página
3. **Campos devueltos:** Nombre, UrlLogo

**Microservicio:** plazoleta-service  
**Endpoint:** `GET /api/v1/restaurantes?page=0&size=10`

---

### **HU-010: Listar Platos por Restaurante**

**Como:** Cliente  
**Quiero:** Ver menú del restaurante  
**Para:** Solicitar plato de mi preferencia

**Criterios de Aceptación:**

1. **Paginación:** Elementos por página configurables
2. **Filtro:** Por categoría
3. **Mostrar:** Solo platos activos

**Microservicio:** plazoleta-service  
**Endpoint:** `GET /api/v1/platos/restaurante/{id}?categoria=PRINCIPAL&page=0&size=10`

---

### **HU-011: Realizar Pedido**

**Como:** Cliente  
**Quiero:** Solicitar platos  
**Para:** Que los preparen

**Criterios de Aceptación:**

1. **Composición:** Lista de platos de un mismo restaurante
2. **Datos requeridos:** Restaurante, platos, cantidad de cada plato
3. **Estado inicial:** PENDIENTE
4. **Restricción:** Cliente solo puede tener un pedido activo (PENDIENTE, EN_PREPARACION, LISTO)

**Microservicio:** pedidos-service  
**Endpoint:** `POST /api/v1/pedidos`

---

### **HU-012: Lista de Pedidos por Estado**

**Como:** Empleado  
**Quiero:** Ver pedidos filtrados por estado  
**Para:** Seleccionar pedido a gestionar

**Criterios de Aceptación:**

1. **Filtro:** Por estado del pedido
2. **Paginación:** Elementos por página configurables
3. **Datos:** Todos los campos del pedido
4. **Restricción:** Solo pedidos del restaurante del empleado

**Microservicio:** pedidos-service  
**Endpoint:** `GET /api/v1/pedidos?estado=PENDIENTE&restauranteId={id}&page=0&size=10`

---

### **HU-013: Asignarse a Pedido**

**Como:** Empleado  
**Quiero:** Asignarme a pedido y cambiar estado  
**Para:** Informar avance al cliente

**Criterios de Aceptación:**

1. **Acción:** Cambiar estado PENDIENTE → EN_PREPARACION
2. **Asignación:** Campo empleadoAsignado = id del empleado
3. **Restricción:** Solo pedidos del restaurante del empleado
4. **Listado:** Paginado con filtro por estado

**Microservicio:** pedidos-service  
**Endpoint:** `PUT /api/v1/pedidos/{id}/asignar`

---

### **HU-014: Notificar Pedido Listo**

**Como:** Empleado  
**Quiero:** Notificar que pedido está listo  
**Para:** Cliente sepa cuándo recoger

**Criterios de Aceptación:**

1. **Acción:** Cambiar estado EN_PREPARACION → LISTO
2. **Notificación:** SMS al teléfono del cliente
3. **Contenido SMS:** Pedido listo + PIN de seguridad para reclamar
4. **PIN:** Generado automáticamente y único por pedido

**Microservicio:** pedidos-service + mensajeria-service  
**Endpoint:** `PUT /api/v1/pedidos/{id}/listo`

---

### **HU-015: Entregar Pedido**

**Como:** Empleado  
**Quiero:** Marcar pedido como entregado  
**Para:** Cerrar ciclo y concentrarme en otros

**Criterios de Aceptación:**

1. **Transición válida:** Solo LISTO → ENTREGADO
2. **Restricción:** ENTREGADO no puede cambiar a otro estado
3. **Validación:** Empleado debe ingresar PIN correcto del cliente
4. **Autorización:** Solo empleado del restaurante

**Microservicio:** pedidos-service  
**Endpoint:** `PUT /api/v1/pedidos/{id}/entregar`

---

### **HU-016: Cancelar Pedido**

**Como:** Cliente  
**Quiero:** Cancelar mi pedido  
**Para:** Retractarme por cualquier motivo

**Criterios de Aceptación:**

1. **Estado válido:** Solo PENDIENTE → CANCELADO
2. **Mensaje de error:** "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse"
3. **Autorización:** Solo el cliente propietario del pedido

**Microservicio:** pedidos-service  
**Endpoint:** `PUT /api/v1/pedidos/{id}/cancelar`

---

### **HU-017: Consultar Trazabilidad**

**Como:** Cliente  
**Quiero:** Ver cambios de estado de mi pedido  
**Para:** Conocer rapidez del servicio

**Criterios de Aceptación:**

1. **Registro:** Cada cambio de estado con timestamp
2. **Autorización:** Cliente solo ve trazabilidad de sus pedidos
3. **Información:** Estado anterior, estado nuevo, fecha/hora, empleado (si aplica)

**Microservicio:** trazabilidad-service  
**Endpoint:** `GET /api/v1/trazabilidad/pedido/{id}`

---

### **HU-018: Consultar Eficiencia**

**Como:** Propietario  
**Quiero:** Ver eficiencia de pedidos  
**Para:** Dar mejor experiencia a clientes

**Criterios de Aceptación:**

1. **Métrica por pedido:** Tiempo total desde inicio hasta entrega
2. **Ranking empleados:** Tiempo medio por empleado
3. **Cálculos:**
    - Tiempo por estado
    - Tiempo total del pedido
    - Comparativa entre empleados
4. **Autorización:** Solo propietario del restaurante

**Microservicio:** trazabilidad-service  
**Endpoint:** `GET /api/v1/trazabilidad/eficiencia/restaurante/{id}`

---

## 🔍 Validaciones Generales

### **Campos de Usuario**

-   **Email:** Formato válido (regex)
-   **Teléfono:** Máximo 13 caracteres, puede incluir +
-   **Documento:** Solo números
-   **Fecha Nacimiento:** Mayor de edad (18+)
-   **Contraseña:** Encriptada con BCrypt

### **Estados de Pedido**

```
PENDIENTE → EN_PREPARACION → LISTO → ENTREGADO
    ↓
CANCELADO
```

### **Roles del Sistema**

-   **ADMINISTRADOR:** Gestión global
-   **PROPIETARIO:** Gestión de restaurante
-   **EMPLEADO:** Gestión de pedidos
-   **CLIENTE:** Realizar pedidos

### **Restricciones de Negocio**

-   Un cliente solo puede tener un pedido activo
-   Empleados solo gestionan pedidos de su restaurante
-   Propietarios solo gestionan su restaurante
-   PIN único por pedido para entrega
-   Trazabilidad completa de cada pedido

### **Categorías de Platos**

-   ENTRADA
-   PRINCIPAL
-   POSTRE
-   BEBIDA

---

## 🎯 Distribución por Microservicios

### **usuarios-service**

-   HU-001: Crear Propietario
-   HU-005: Autenticación
-   HU-006: Crear Empleado
-   HU-008: Crear Cliente

### **plazoleta-service**

-   HU-002: Crear Restaurante
-   HU-003: Crear Plato
-   HU-004: Modificar Plato
-   HU-007: Habilitar/Deshabilitar Plato
-   HU-009: Listar Restaurantes
-   HU-010: Listar Platos

### **pedidos-service**

-   HU-011: Realizar Pedido
-   HU-012: Lista Pedidos por Estado
-   HU-013: Asignarse a Pedido
-   HU-014: Notificar Pedido Listo
-   HU-015: Entregar Pedido
-   HU-016: Cancelar Pedido

### **trazabilidad-service**

-   HU-017: Consultar Trazabilidad
-   HU-018: Consultar Eficiencia

### **mensajeria-service**

-   HU-014: Envío SMS (trigger desde pedidos-service)

---

## 📋 Checklist de Implementación

### **Por Historia de Usuario**

-   [ ] Modelo de dominio definido
-   [ ] Caso de uso implementado
-   [ ] Validaciones de negocio
-   [ ] Endpoint REST documentado
-   [ ] Autorización configurada
-   [ ] Tests unitarios (>80% cobertura)
-   [ ] Tests de integración
-   [ ] Documentación OpenAPI

### **Por Microservicio**

-   [ ] Arquitectura hexagonal implementada
-   [ ] Configuración de seguridad
-   [ ] Integración con otros servicios
-   [ ] Manejo de eventos (RabbitMQ)
-   [ ] Manejo de errores
-   [ ] Logging configurado
-   [ ] Health checks activos

---

## 🧭 Orden de Implementación Sugerido (para trazabilidad)

1.  HU-001: Crear Propietario (usuarios-service)
2.  HU-005: Agregar Autenticación (usuarios-service + API Gateway)
3.  HU-006: Crear cuenta Empleado (usuarios-service)
4.  HU-008: Crear cuenta Cliente (usuarios-service)
5.  HU-002: Crear Restaurante (plazoleta-service)
6.  HU-003: Crear Plato (plazoleta-service)
7.  HU-004: Modificar Plato (plazoleta-service)
8.  HU-007: Habilitar/Deshabilitar Plato (plazoleta-service)
9.  HU-009: Listar Restaurantes (plazoleta-service)
10. HU-010: Listar Platos por restaurante (plazoleta-service)
11. HU-011: Realizar Pedido (pedidos-service)
12. HU-012: Lista de Pedidos por Estado (pedidos-service)
13. HU-013: Asignarse a Pedido (pedidos-service)
14. HU-014: Notificar Pedido Listo (pedidos-service + mensajeria-service)
15. HU-015: Entregar Pedido (pedidos-service)
16. HU-016: Cancelar Pedido (pedidos-service)
17. HU-017: Consultar Trazabilidad (trazabilidad-service)
18. HU-018: Consultar Eficiencia (trazabilidad-service)

Nota: El orden prioriza dependencias lógicas: autenticación antes de operaciones de negocio; catálogo (plazoleta) antes de pedidos; pedidos antes de trazabilidad y mensajería.
