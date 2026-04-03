# Stack Shop — Backend

API REST para una plataforma e-commerce completa construida con Spring Boot, Spring Security y JWT. Incluye carrito persistente con Redis, notificaciones en tiempo real por WebSocket, subida de imágenes a Cloudinary y documentación interactiva con Swagger.

🔗 **Frontend:** [github.com/Jorge5115/ecommerce-frontend](https://github.com/Jorge5115/ecommerce-frontend)

---

## Funcionalidades

- **Autenticación y autorización** — JWT con roles USER y ADMIN
- **Catálogo de productos** — CRUD completo con búsqueda avanzada y filtros por categoría
- **Carrito de compra** — persistido en Redis, expira automáticamente a los 7 días
- **Sistema de pedidos** — gestión completa con tracking de estado y descuento de stock automático
- **Reseñas y valoraciones** — solo usuarios con compra verificada pueden reseñar
- **Wishlist** — lista de favoritos con opción de mover al carrito
- **Cupones de descuento** — por porcentaje o cantidad fija, con validación de fecha y límite de uso
- **Panel de administración** — dashboard con métricas en tiempo real, gestión de usuarios y productos
- **Notificaciones en tiempo real** — WebSocket con STOMP para alertas de pedidos y stock bajo
- **Subida de imágenes** — integración con Cloudinary

---

## Stack tecnológico

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 21 |
| Framework | Spring Boot · Spring Security |
| Autenticación | JWT |
| Base de datos | MySQL |
| Caché / Sesiones | Redis |
| Tiempo real | WebSockets + STOMP |
| Imágenes | Cloudinary |
| Contenedores | Docker / Docker Compose |
| Documentación | Swagger (SpringDoc OpenAPI) |

---

## Requisitos previos

- Java 21
- Docker y Docker Compose
- Cuenta en [Cloudinary](https://cloudinary.com) (gratuita)

---

## Instalación y arranque

### 1. Clonar el repositorio

```bash
git clone https://github.com/Jorge5115/ecommerce-backend.git
cd ecommerce-backend
```

### 2. Configurar variables de entorno

Crea un archivo `src/main/resources/application-local.properties` con tus credenciales:

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/stackshop
spring.datasource.username=root
spring.datasource.password=tu_password

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT
jwt.secret=tu_clave_secreta_larga
jwt.expiration=86400000

# Cloudinary
cloudinary.cloud-name=tu_cloud_name
cloudinary.api-key=tu_api_key
cloudinary.api-secret=tu_api_secret
```

### 3. Levantar MySQL y Redis con Docker

```bash
docker-compose up -d
```

### 4. Arrancar la aplicación

```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`

---

## Documentación de la API

Con la aplicación corriendo, accede a Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

Desde ahí puedes explorar y probar todos los endpoints con autenticación JWT integrada.

---

## Endpoints principales

| Módulo | Ruta base |
|--------|-----------|
| Auth | `/api/auth` |
| Productos | `/api/products` |
| Categorías | `/api/categories` |
| Carrito | `/api/cart` |
| Pedidos | `/api/orders` |
| Reseñas | `/api/reviews` |
| Wishlist | `/api/wishlist` |
| Cupones | `/api/coupons` |
| Administración | `/api/admin` |
| WebSocket | `/ws` |

---

## Estructura del proyecto

```
src/
└── main/
    └── java/
        └── com/stackshop/
            ├── auth/          # JWT, filtros, seguridad
            ├── cart/          # Carrito con Redis
            ├── category/      # Categorías
            ├── coupon/        # Sistema de cupones
            ├── notification/  # WebSockets
            ├── order/         # Pedidos
            ├── product/       # Catálogo
            ├── review/        # Reseñas
            ├── user/          # Perfil y wishlist
            └── admin/         # Panel de administración
```

---

## Autor

**Jorge Casanova Sánchez**  
[LinkedIn](https://www.linkedin.com/in/jorge-casanova-sánchez/) · [GitHub](https://github.com/Jorge5115)
