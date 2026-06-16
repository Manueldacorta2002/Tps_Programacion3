# Ejemplos JSON para Postman — Food Store API

Base URL: `http://localhost:8080`

---

## 1. Crear usuario

**POST** `/api/auth/register`

```json
{
  "nombre": "Laura",
  "apellido": "Gomez",
  "mail": "laura@example.com",
  "celular": "2614455667",
  "contrasenia": "password123"
}
```

Respuesta esperada: `201 Created`

```json
{
  "id": 3,
  "nombre": "Laura",
  "apellido": "Gomez",
  "mail": "laura@example.com",
  "celular": "2614455667",
  "rol": "USUARIO"
}
```

---

## 2. Crear categoría

**POST** `/api/categories`

```json
{
  "nombre": "Ensaladas",
  "descripcion": "Opciones frescas y saludables"
}
```

Respuesta esperada: `201 Created`

```json
{
  "id": 5,
  "nombre": "Ensaladas",
  "descripcion": "Opciones frescas y saludables"
}
```

---

## 3. Crear producto

**POST** `/api/products`

> Reemplazar `categoriaId` por el id real de la categoría. Las categorías iniciales son: Pizzas (1), Hamburguesas (2), Bebidas (3), Postres (4).

```json
{
  "nombre": "Ensalada César",
  "precio": 850.0,
  "descripcion": "Lechuga romana, crutones, parmesano y aderezo César",
  "stock": 20,
  "imagen": "ensalada_cesar.jpg",
  "disponible": true,
  "categoriaId": 1
}
```

Respuesta esperada: `201 Created`

```json
{
  "id": 12,
  "nombre": "Ensalada César",
  "precio": 850.0,
  "descripcion": "Lechuga romana, crutones, parmesano y aderezo César",
  "stock": 20,
  "imagen": "ensalada_cesar.jpg",
  "disponible": true,
  "categoriaId": 1,
  "categoriaNombre": "Pizzas"
}
```

---

## 4. Crear pedido

**POST** `/api/orders`

> Reemplazar `usuarioId` y `productoId` por ids reales. El usuario cliente inicial tiene id 2.

```json
{
  "formaPago": "EFECTIVO",
  "usuarioId": 2,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 7,
      "cantidad": 1
    }
  ]
}
```

Valores válidos para `formaPago`: `EFECTIVO`, `TARJETA`, `TRANSFERENCIA`

Respuesta esperada: `201 Created`

```json
{
  "id": 4,
  "fecha": "2026-06-08",
  "estado": "PENDIENTE",
  "formaPago": "EFECTIVO",
  "total": 2750.0,
  "usuarioId": 2,
  "detalles": [
    { "id": 7, "cantidad": 2, "subtotal": 2400.0, "productoId": 1 },
    { "id": 8, "cantidad": 1, "subtotal": 350.0, "productoId": 7 }
  ]
}
```

---

## 5. Actualizar categoría

**PUT** `/api/categories/{id}`

Ejemplo: `PUT /api/categories/1`

```json
{
  "nombre": "Pizzas Artesanales",
  "descripcion": "Las mejores pizzas artesanales con masa madre"
}
```

Respuesta esperada: `200 OK`

```json
{
  "id": 1,
  "nombre": "Pizzas Artesanales",
  "descripcion": "Las mejores pizzas artesanales con masa madre"
}
```

---

## 6. Buscar usuario por ID

**GET** `/api/users/{id}`

Ejemplo: `GET /api/users/1`

No requiere body.

Respuesta esperada: `200 OK`

```json
{
  "id": 1,
  "nombre": "Admin",
  "apellido": "Food Store",
  "mail": "admin@admin.com",
  "celular": "2610000000",
  "rol": "ADMIN"
}
```

---

## 7. Buscar usuario por mail

**GET** `/api/users/search?mail={email}`

Ejemplo: `GET /api/users/search?mail=cliente@foodstore.com`

No requiere body.

Respuesta esperada: `200 OK`

```json
{
  "id": 2,
  "nombre": "Cliente",
  "apellido": "Prueba",
  "mail": "cliente@foodstore.com",
  "celular": "2610000001",
  "rol": "USUARIO"
}
```

---

## Respuestas de error

### 404 Not Found

```json
{
  "timestamp": "2026-06-08T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Usuario no encontrado con id 99",
  "path": "/api/users/99"
}
```

### 400 Bad Request (validación)

```json
{
  "timestamp": "2026-06-08T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validacion",
  "path": "/api/categories",
  "validations": {
    "nombre": "El nombre es obligatorio"
  }
}
```

### 400 Bad Request (negocio)

```json
{
  "timestamp": "2026-06-08T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe una categoria con ese nombre",
  "path": "/api/categories"
}
```
