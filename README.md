# Inventario de Productos

## Descripción

CRUD de inventario con dos módulos: **productos** y **categorías** (ambos con listar, crear, editar y eliminar). Los productos además se pueden buscar por nombre, filtrar por categoría y muestran el valor total del inventario visible.

## Stack

| Capa | Tecnología |
|---|---|
| Frontend | Vue 3 (Composition API, `<script setup>`), Vue Router, Tailwind CSS, Vite |
| Backend | Java 21, Spring Boot, Spring Data JPA, Maven |
| Base de datos | MySQL |

## Arquitectura

```
┌────────────┐   HTTP/JSON   ┌───────────────────────────────┐   JPA   ┌───────┐
│  Navegador │ ────────────► │  Backend Spring Boot (:8080)  │ ──────► │ MySQL │
│  Vue 3     │ ◄──────────── │  controller → service → repo  │ ◄────── │       │
└────────────┘               └───────────────────────────────┘         └───────┘
```

**Backend (por capas):** `controllers` reciben la petición HTTP, `services` tienen las reglas de negocio, `repositories` hablan con la base de datos y `entities` son las tablas.

**Frontend (por capas):**

```
frontend/src/
├── pages/        pantallas (productos y categorías); crear/editar se hace en un modal
├── components/   piezas reutilizables (navbar, footer, alerta de error, modal)
├── services/     único lugar que habla con el backend (fetch)
├── router/       rutas
└── App.vue       layout general
```

Flujo: `page` → `service` → `http.js` → backend. Las pantallas nunca hacen `fetch` directo.

## Cómo correr el proyecto

### Base de datos

Crear la base `crud_prueba` en MySQL local. Las tablas las crea Hibernate (`ddl-auto=update`).

### Backend

Las credenciales de la BD van en variables de entorno (no están en el código). Copia `.env.example` a `.env` en la raíz, completa `DB_PASSWORD` y arranca así:

```bash
cp .env.example .env
cd backend/products
set -a && . ../../.env && set +a    # carga el .env en la terminal
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
cp .env.example .env     # URL del backend
npm install
npm run dev              # http://localhost:5173
```

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/products` | Listar productos |
| GET | `/products/{id}` | Obtener uno |
| POST | `/products` | Crear |
| PUT | `/products/{id}` | Actualizar |
| DELETE | `/products/{id}` | Eliminar |
| GET/POST/PUT/DELETE | `/categories`, `/categories/{id}` | Igual para categorías |

## Despliegue

Pendiente.

