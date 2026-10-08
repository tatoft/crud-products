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

```bash
cd backend/products
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

## Decisiones técnicas

- **Arquitectura en capas simple** (controller → service → repository) y no clean architecture: para un CRUD el costo de más capas no se justifica.
- **Sin Lombok ni interfaces para los services:** menos magia y menos archivos; el código se lee tal cual.
- **Un solo punto de acceso HTTP en el front** (`services/http.js`): URL base, JSON y errores centralizados. Si cambia el backend, se toca un archivo.
- **URL del backend en variable de entorno** (`VITE_API_URL`): el mismo código sirve en local y en la nube.
- **Manejo de errores en el front:** cada pantalla muestra tres estados (cargando, error, datos/vacío) y mensajes claros si el backend está caído.
- **Validación en el formulario y también en el backend:** el front da respuesta rápida, pero no es de fiar.
- **Sin Pinia:** no hay estado compartido entre pantallas; cada vista pide lo que necesita.
- **Filtros en el navegador:** el volumen es pequeño; si crece, se pasan al backend con paginación.
- **Monorepo:** un solo repositorio con `backend/` y `frontend/` facilita revisar y desplegar.

## Uso de IA

Documentación de cómo usé IA (Claude Code) como mentor durante la prueba.

| Qué hice | Prompt usado | Qué aprendí |
|---|---|---|
| Revisión del backend: lista de hallazgos por gravedad | "Eres mi mentor, no mi programador. Lee el backend y dame hallazgos como preguntas o pistas, ordenados por gravedad" | Que `.orElse(null)` y las excepciones genéricas terminan en 500 y no en 404/400; la necesidad de un `@ControllerAdvice` y de DTOs |
| Crear el frontend con Vue + Vite | "Empecemos con el front, ¿cómo hago?" | Qué hace `create-vue` y por qué elegir proyecto en blanco, y que el proyecto debe quedar directo en `frontend/` |
| Instalar Tailwind v4 con Vite | "Quiero instalarle Tailwind según su documentación" | Que v4 usa el plugin `@tailwindcss/vite` y un `@import "tailwindcss"` en el CSS, sin `tailwind.config.js` |
| Estructura del repositorio | "Se crearon 2 repos, ¿está bien?" | Un monorepo exige borrar los `.git` internos o Git los trata como repos anidados |
| Armar el frontend según los lineamientos de la prueba | "Hazlo tú, simple, cumpliendo los lineamientos del correo" (IA escribió el código; lo estudio después para poder explicarlo) | Separación en capas en el front, estados de carga/error, variables de entorno y filtros del lado del cliente |
| Agregar el CRUD de categorías al front y simplificar la interfaz | "Esto no cubre todo el backend, categorías también tiene CRUD; dame una UI de Tailwind muy básica" (IA escribió el código; lo estudio después) | Reutilizar un solo formulario para crear y editar, y que el estilo se pueda mantener mínimo con pocas clases de Tailwind |
| Formulario propio para categorías, navbar, footer y diseño minimalista | "El form debe estar en un formulario, cumple la arquitectura, ponle navbar y footer, minimalista y muy simple" (IA escribió el código; lo estudio después) | Extraer clases repetidas de Tailwind a `main.css` (`@layer components`) y que lista y formulario sean páginas separadas por ruta |
| Simplificar: formularios de crear/editar en un modal | "El formulario debe ir en un modal, más simple, es un CRUD simple" (IA escribió el código; lo estudio después) | Un componente modal reutilizable con slot, y un solo estado `editingId` para saber si se crea o se edita |
| Arreglar el 500 del POST /products sin categoría | "Arréglalo" (IA escribió el código; lo estudio después) | Validar en el service y traducir `IllegalArgumentException` a 400 con un `@RestControllerAdvice`; sin ese manejador, la excepción sigue siendo 500 |
