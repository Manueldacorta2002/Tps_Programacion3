# Food Store - Sistema de Gestión de Pedidos de Comida

## Descripción

En este trabajo desarrollé una aplicación web llamada Food Store. La idea principal es simular un sistema de pedidos de comida, donde un cliente puede registrarse, iniciar sesión, ver el catálogo de productos, agregar cosas al carrito y confirmar un pedido. El administrador, por otro lado, puede gestionar categorías, productos y pedidos desde un panel propio.

El backend es una API REST hecha con Spring Boot y el frontend es un conjunto de páginas HTML con TypeScript y Vite.

---

## Tecnologías

**Backend:**

* Java 17
* Spring Boot
* Spring Data JPA
* H2 Database
* Gradle

**Frontend:**

* TypeScript
* Vite
* HTML
* CSS

---

## Funcionalidades principales

**Cliente:**

* Registro e inicio de sesión
* Catálogo de productos con búsqueda y filtro por categoría
* Carrito de compras guardado en localStorage
* Confirmación de pedido con selección de forma de pago
* Historial de pedidos propios

**Administrador:**

* Dashboard con estadísticas generales
* Gestión de categorías: crear, editar y eliminar
* Gestión de productos: crear, editar y eliminar
* Gestión de pedidos con cambio de estado

---

## Usuarios de prueba

El proyecto carga datos automáticamente al iniciar. Se pueden usar estas cuentas:

**Administrador:**

* Email: [admin@admin.com](mailto:admin@admin.com)
* Contraseña: 123456

**Cliente:**

* Email: [cliente@foodstore.com](mailto:cliente@foodstore.com)
* Contraseña: 123456

---

## Cómo ejecutar el proyecto

### Backend

Desde la carpeta raíz del proyecto, donde está el archivo `gradlew.bat`, ejecutar:

```bash
.\gradlew.bat bootRun
```

El servidor queda corriendo en:

```text
http://localhost:8080
```

### Frontend

En otra terminal, desde la misma carpeta del proyecto, ejecutar:

```bash
npm install
npm run dev
```

El frontend queda corriendo en:

```text
http://localhost:5173
```

---

## URLs importantes

* Frontend: http://localhost:5173
* Backend: http://localhost:8080
* Consola H2: http://localhost:8080/h2-console
* Swagger UI: http://localhost:8080/swagger-ui/index.html

---

## Estructura del proyecto

El backend está organizado por capas: `controllers`, `services`, `repositories`, `entities`, `dtos`, `enums`, `exceptions` y `config`.

El frontend está separado en carpetas por tipo: `pages` para las pantallas, `types` para interfaces TypeScript, `utils` para funciones de autenticación, carrito y navegación, y `api` para el cliente HTTP centralizado.

```text
src/
├── main/java/com/utn/       <- Backend
│   ├── controllers/
│   ├── services/
│   ├── entities/
│   ├── dtos/
│   ├── repositories/
│   └── config/
├── api/                     <- Cliente HTTP
├── types/                   <- Interfaces TS
├── utils/                   <- auth, cart, localStorage
└── pages/
    ├── auth/                <- login, registro
    ├── store/               <- catálogo, carrito, pedidos del cliente
    └── admin/               <- dashboard, categorías, productos, pedidos
```

---

## Decisiones técnicas

* Usé `localStorage` para guardar la sesión y el carrito porque es un proyecto educativo y no requería autenticación real con tokens.
* Usé DTOs en el backend para no exponer información sensible como las contraseñas en las respuestas de la API.
* Implementé soft delete: los registros no se borran físicamente, sino que se marcan con `eliminado = true` y se filtran en las consultas.
* Separé el backend en capas: controller, service y repository, para que el código quede más ordenado.
* Usé H2 como base de datos para que el proyecto se pueda correr sin configurar una base externa.

---

## Aclaración de seguridad

Este proyecto no usa seguridad real con JWT ni Spring Security. La autenticación es básica: al hacer login se guarda el usuario en localStorage y cada página verifica el rol antes de cargar. Está pensado así para cumplir con el flujo pedido en el trabajo práctico, no para un entorno de producción.

---



## Documentación

Documentación: FoodStore-TPI-Manuel-Da-Corta.pdf

---

## Fundamentos de Spring aplicados

### Application Context y Beans

Spring Boot levanta un Application Context al iniciar la aplicación. Este contexto es el contenedor que crea, configura y gestiona el ciclo de vida de todos los beans. En este proyecto, todos los controllers, services, repositories, el `DataInitializer` y `CorsConfig` son beans gestionados por Spring: no se instancian manualmente con `new`.

### Inyección de dependencias por constructor

Todos los services y controllers usan inyección por constructor en lugar de `@Autowired` en campos. Por ejemplo, `PedidoService` recibe `PedidoRepository`, `UsuarioService` y `ProductoService` como parámetros del constructor. Esto hace que las dependencias sean explícitas y facilita el testing.

### Estereotipos

- `@RestController`: usado en los 5 controllers (`AuthController`, `UsuarioController`, `ProductoController`, `CategoriaController`, `PedidoController`). Combina `@Controller` y `@ResponseBody`.
- `@Service`: usado en los 5 services (`AuthService`, `UsuarioService`, `ProductoService`, `CategoriaService`, `PedidoService`).
- `@Repository`: usado en los 5 repositories de `com.utn.repositories`. Spring Data JPA los implementa automáticamente en tiempo de ejecución.
- `@Component`: usado en `DataInitializer`, que implementa `CommandLineRunner` para ejecutar lógica al iniciar la app.

### DTOs

Se usan DTOs (Data Transfer Objects) como records de Java para todas las operaciones de la API:

- `*Create`: reciben datos para crear un nuevo registro, con validaciones (`@NotBlank`, `@NotNull`, etc.).
- `*Edit`: reciben datos para actualizar un registro existente.
- `*Dto`: se usan como respuesta al frontend, sin exponer campos sensibles (por ejemplo, `UsuarioDto` no incluye la contraseña).

### H2 y application.properties

- La base de datos H2 corre en memoria (`jdbc:h2:mem`), sin necesidad de instalar nada externo.
- Hibernate genera el esquema automáticamente (`ddl-auto=update`).
- La consola H2 está disponible en `http://localhost:8080/h2-console` para inspeccionar los datos en tiempo de ejecución.
- El puerto está definido explícitamente como `server.port=8080`.

### Datos iniciales

`DataInitializer` implementa `CommandLineRunner` y carga datos de prueba al iniciar la aplicación. Verifica existencia antes de insertar para no duplicar datos en cada reinicio:

- 2 usuarios (1 admin, 1 cliente)
- 4 categorías y 11 productos
- 3 pedidos del cliente con 2 detalles cada uno (estados: TERMINADO, CONFIRMADO, PENDIENTE)

---

## TP API REST aplicado

### API REST stateless

La API no guarda estado del cliente entre requests. Cada petición es independiente: el servidor no recuerda llamadas anteriores. El frontend mantiene la sesión en `localStorage` y envía los datos necesarios en cada request.

### Métodos HTTP usados

| Método | Uso |
|---|---|
| GET | Listar recursos, buscar por id, buscar por mail |
| POST | Crear recursos (usuarios, categorías, productos, pedidos) |
| PUT | Actualizar un recurso completo (categoría, producto, usuario) |
| PATCH | Actualizar parcialmente (estado de un pedido) |
| DELETE | Eliminación lógica (soft delete, marca `eliminado = true`) |

### Arquitectura Controller → Service → Repository

- **Controller**: recibe el HTTP request, valida con `@Valid`, delega al service y devuelve la respuesta HTTP. Sin lógica de negocio.
- **Service**: contiene la lógica de negocio. Verifica duplicados, valida reglas, transforma DTOs ↔ entidades.
- **Repository**: extiende `JpaRepository`. Spring Data JPA genera la implementación automáticamente en tiempo de ejecución.

### DTOs

Se usan records de Java como DTOs para desacoplar la API de las entidades JPA:

- `*Create`: datos para crear un recurso. Tienen validaciones de entrada.
- `*Edit`: datos para actualizar un recurso. Tienen validaciones de entrada.
- `*Dto`: respuesta al cliente. Nunca incluye contraseñas ni campos sensibles.

### Validaciones con @Valid

Los DTOs tienen anotaciones de validación de Bean Validation (`jakarta.validation`):

- `@NotBlank`: campos de texto obligatorios
- `@NotNull`: campos de cualquier tipo obligatorios
- `@Email`: formato de email
- `@Size`: longitud mínima/máxima
- `@DecimalMin` / `@Min`: valores numéricos mínimos

Los controllers activan las validaciones con `@Valid` en el parámetro `@RequestBody`. Si falla una validación, el `GlobalExceptionHandler` intercepta la excepción y devuelve `400 Bad Request` con el detalle del campo inválido.

### Manejo global de errores con @ControllerAdvice

`GlobalExceptionHandler` centraliza el manejo de excepciones:

- `ResourceNotFoundException` → `404 Not Found`
- `BusinessException` → `400 Bad Request` (reglas de negocio como email duplicado)
- `MethodArgumentNotValidException` → `400 Bad Request` con mapa de campos inválidos
- `Exception` genérica → `500 Internal Server Error`

Las respuestas de error nunca exponen el stacktrace. Devuelven un JSON estructurado con `timestamp`, `status`, `error`, `message` y `path`.

### Swagger / OpenAPI

Swagger está configurado con `springdoc-openapi-starter-webmvc-ui:2.5.0`. Al levantar el servidor, la documentación interactiva queda disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

Desde ahí se pueden ver y probar todos los endpoints sin necesidad de Postman.

### H2 y persistencia con Spring Data JPA

- H2 corre en memoria (`jdbc:h2:mem`). No requiere instalación externa.
- Hibernate genera el esquema automáticamente con `ddl-auto=update`.
- `DataInitializer` carga datos de prueba al iniciar: 2 usuarios, 4 categorías, 11 productos y 3 pedidos con 2 detalles cada uno.
- Consola H2 disponible en `http://localhost:8080/h2-console` para inspeccionar los datos.

### Endpoints REST disponibles

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/register` | Registrar usuario |
| POST | `/api/auth/login` | Login |
| GET | `/api/users` | Listar usuarios |
| GET | `/api/users/{id}` | Buscar usuario por id |
| GET | `/api/users/search?mail=` | Buscar usuario por mail |
| PUT | `/api/users/{id}` | Actualizar usuario |
| DELETE | `/api/users/{id}` | Eliminar usuario |
| GET | `/api/categories` | Listar categorías |
| GET | `/api/categories/{id}` | Buscar categoría por id |
| POST | `/api/categories` | Crear categoría |
| PUT | `/api/categories/{id}` | Actualizar categoría |
| DELETE | `/api/categories/{id}` | Eliminar categoría |
| GET | `/api/products` | Listar productos |
| GET | `/api/products/{id}` | Buscar producto por id |
| GET | `/api/products/category/{id}` | Listar por categoría |
| POST | `/api/products` | Crear producto |
| PUT | `/api/products/{id}` | Actualizar producto |
| DELETE | `/api/products/{id}` | Eliminar producto |
| GET | `/api/orders` | Listar pedidos |
| GET | `/api/orders/{id}` | Buscar pedido por id |
| GET | `/api/orders/user/{userId}` | Pedidos de un usuario |
| POST | `/api/orders` | Crear pedido |
| PATCH | `/api/orders/{id}/status` | Actualizar estado |
| DELETE | `/api/orders/{id}` | Eliminar pedido |

Ver ejemplos JSON en [`docs/postman-ejemplos.md`](docs/postman-ejemplos.md).

---

## Video demostrativo / Integrador

Link del video integrador: https://drive.google.com/file/d/18uWguE9NnCJPFes-e4pbQCgISxj5nYW5/view?usp=sharing

## Autor

Manuel Da Corta
