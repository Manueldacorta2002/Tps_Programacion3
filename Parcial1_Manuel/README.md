# Food Store - Sistema de Gestión de Pedidos de Comida

## Descripción

En este trabajo desarrollé una aplicación web llamada Food Store. La idea principal es simular un sistema de pedidos de comida, donde un cliente puede registrarse, iniciar sesión, ver el catálogo de productos, agregar cosas al carrito y confirmar un pedido. El administrador, por otro lado, puede gestionar categorías, productos y pedidos desde un panel propio.

El backend es una API REST hecha con Spring Boot y el frontend es un conjunto de páginas HTML con TypeScript y Vite.

---

## Tecnologías

**Backend:**

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (base de datos en memoria)
- Gradle

**Frontend:**

- TypeScript
- Vite
- HTML
- CSS

---

## Funcionalidades principales

**Cliente:**

- Registro e inicio de sesión
- Catálogo de productos con búsqueda y filtro por categoría
- Carrito de compras (guardado en localStorage)
- Confirmación de pedido con selección de forma de pago
- Historial de pedidos propios

**Administrador:**

- Dashboard con estadísticas generales
- Gestión de categorías (crear, editar, eliminar)
- Gestión de productos (crear, editar, eliminar)
- Gestión de pedidos con cambio de estado

---

## Usuarios de prueba

El proyecto carga datos automáticamente al iniciar. Se pueden usar estas cuentas:

**Administrador:**

- Email: <admin@admin.com>
- Contraseña: 123456

**Cliente:**

- Email: <cliente@foodstore.com>
- Contraseña: 123456

---

## Cómo ejecutar el proyecto

### Backend

Desde la carpeta raíz del proyecto (donde está `gradlew.bat`):

```bash
.\gradlew.bat bootRun
```

El servidor queda corriendo en <http://localhost:8080>

### Frontend

```bash
npm install
npm run dev
```

O si usás pnpm:

```bash
pnpm install
pnpm dev
```

El frontend queda en <http://localhost:5173>

---

## URLs importantes

- Frontend: <http://localhost:5173>
- Backend: <http://localhost:8080>
- Consola H2: <http://localhost:8080/h2-console>
- Swagger UI: <http://localhost:8080/swagger-ui/index.html>

---

## Estructura del proyecto

El backend está organizado por capas: `controllers`, `services`, `repositories`, `entities`, `dtos`, `enums`, `exceptions` y `config`.

El frontend está separado en carpetas por tipo: `pages` (una carpeta por pantalla), `types` (interfaces TypeScript), `utils` (funciones de auth, carrito y navegación) y `api` (cliente HTTP centralizado).

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
    ├── store/               <- catalogo, carrito, pedidos (cliente)
    └── admin/               <- dashboard, categorias, productos, pedidos
```

---

## Decisiones técnicas

- Usé **localStorage** para guardar la sesión y el carrito porque es un proyecto educativo y no requería autenticación real con tokens.
- Usé **DTOs** en el backend para no exponer información sensible como las contraseñas en las respuestas de la API.
- Implementé **soft delete**: los registros no se borran físicamente, sino que se marcan con `eliminado = true` y se filtran en las consultas.
- Separé el backend en capas (controller, service, repository) para que el código quede más ordenado y sea más fácil de mantener.
- Usé **H2** como base de datos en memoria para que el proyecto se pueda correr sin configurar nada extra.

---

## Aclaración de seguridad

Este proyecto no usa seguridad real con JWT ni Spring Security. La autenticación es básica: al hacer login se guarda el usuario en localStorage y cada página verifica el rol antes de cargar. Está pensado así para cumplir con el flujo pedido en el trabajo práctico, no para un entorno de producción.

---

## Video demostrativo

Link del video: pegar acá el enlace

---

## Documentación

Documentación: FoodStore-TPI-Manuel-Da-Corta.pdf

---

## Autor

Manuel Da Corta
