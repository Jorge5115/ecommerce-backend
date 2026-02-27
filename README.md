# 🛒 E-commerce Full-Stack (BACK)

Plataforma de e-commerce completa con Spring Boot + React + MySQL + Redis

## 🚀 Características

- Autenticación JWT con roles (USER/ADMIN)
- Carrito de compra con Redis
- Sistema de pedidos completo
- Reseñas y valoraciones
- Wishlist
- Sistema de cupones
- Panel de administración
- Notificaciones WebSocket en tiempo real
- Subida de imágenes a Cloudinary
- Documentación Swagger

## 🛠️ Tecnologías

**Backend:**
- Spring Boot 4 + Java 21 
- Spring Security con JWT 
- MySQL para persistencia 
- Redis para caché y sesiones 
- WebSockets para tiempo real 
- Cloudinary para gestión de imágenes 
- Docker para contenedores

### Día 1: Setup inicial del proyecto
- [x] Proyecto Spring Boot creado
- [x] PostgreSQL corriendo en Docker
- [x] Redis corriendo en Docker
- [x] Estructura de carpetas creada
- [x] application.properties configurado
- [x] Flyway setup
- [x] Entity User creada

### Día 2: Entidades y relaciones 
- [x] Enums creados (UserRole, OrderStatus, PaymentMethod, PaymentStatus)
- [x] Todas las entidades creadas
- [x] Relaciones configuradas
- [x] JPA Auditing habilitado
- [x] Repositories creados

### Día 3: Autenticación JWT
- [x] Dependencias JWT añadidas
- [x] DTOs creados (LoginRequest, RegisterRequest, AuthResponse)
- [x] JwtUtil creado
- [x] CustomUserDetailsService implementado
- [x] JwtAuthenticationFilter creado
- [x] SecurityConfig configurado
- [x] AuthService creado
- [x] AuthController creado

### Día 4: CRUD de Productos (+ Cambio de BB.DD)

- [x] DTOs creados (ProductDTO, CreateProductDTO, UpdateProductDTO)
- [x] Custom Exceptions y GlobalExceptionHandler
- [x] ProductService con lógica CRUD
- [x] ProductController con endpoints REST
- [x] Method Security habilitado
- [x] Migrado de PostgreSQL a MySQL

### Día 5: Categorías & Búsqueda Avanzada

- [x] DTOs de Category creados
- [x] CategoryRepository actualizado
- [x] CategoryService creado
- [x] CategoryController creado
- [x] ProductRepository con búsqueda avanzada
- [x] ProductService con método searchProducts
- [x] ProductController con endpoint de búsqueda

### Día 6: Redis Cache

- [x] RedisConfig creado con serialización JSON
- [x] Cache habilitado con @EnableCaching
- [x] CategoryService con @Cacheable y @CacheEvict
- [x] ProductService con @CacheEvict en mutaciones
- [x] DTOs implementan Serializable
- [x] Redis corriendo en Docker

### Día 7: Testing Backend

- [x] H2 configurado para tests
- [x] application.properties de test creado
- [x] AuthServiceTest con 4 tests
- [x] ProductServiceTest con 6 tests
- [x] AuthControllerTest con 3 tests
- [x] EcommerceApplicationTests pasando

### Día 8: Carrito con Redis

- [x] DTOs del carrito creados (CartDTO, CartItemDTO, AddToCartDTO)
- [x] CartService con lógica completa en Redis
- [x] CartController con todos los endpoints
- [x] Carrito expira automáticamente en 7 días
- [x] Validación de stock al añadir productos

### Día 9: Sistema de Pedidos

- [x] DTOs de pedidos creados (OrderDTO, OrderItemDTO, CreateOrderDTO)
- [x] OrderRepository actualizado
- [x] OrderService con lógica completa
- [x] OrderController con endpoints de usuario y admin
- [x] Descuento de stock automático al crear pedido
- [x] Devolución de stock al cancelar pedido
- [x] Soporte para cupones de descuento
- [x] Cálculo automático de impuestos (21%)

### Día 10: Sistema de Reseñas y Valoraciones

- [x] DTOs de reseñas creados (ReviewDTO, CreateReviewDTO)
- [x] ReviewRepository actualizado con queries personalizadas
- [x] ReviewService con lógica completa
- [x] ReviewController con endpoints REST
- [x] Verificación de compra confirmada automática
- [x] Actualización automática de rating del producto
- [x] Un usuario solo puede reseñar un producto una vez

### Día 11: Wishlist y Perfil de Usuario

- [x] DTOs creados (WishlistDTO, UserDTO, UpdateProfileDTO)
- [x] WishlistRepository actualizado
- [x] WishlistService con lógica completa
- [x] UserService con perfil y actualización
- [x] WishlistController con todos los endpoints
- [x] UserController con perfil
- [x] Funcionalidad mover de wishlist a carrito
- [x] Cambio de contraseña con verificación

### Día 12: Sistema de Cupones

- [x] DTOs creados (CouponDTO, CreateCouponDTO, ValidateCouponDTO)
- [x] CouponRepository actualizado
- [x] CouponService con lógica completa
- [x] CouponController con endpoints REST
- [x] Validación completa de cupones (fecha, límite, mínimo)
- [x] Cupones por porcentaje y por cantidad fija
- [x] Integrado con sistema de pedidos

### Día 13: Panel de Administración

- [x] DTOs de estadísticas creados (DashboardStatsDTO, TopProductDTO)
- [x] Queries personalizadas en repositories
- [x] AdminService con estadísticas completas
- [x] AdminController con endpoints de administración
- [x] Dashboard con métricas en tiempo real
- [x] Gestión de usuarios desde admin
- [x] Top productos más vendidos

### Día 14: WebSockets - Notificaciones en Tiempo Real

- [x] WebSocketConfig configurado con STOMP
- [x] NotificationDTO creado
- [x] NotificationService con notificaciones a usuarios y admins
- [x] Notificaciones automáticas al crear pedidos
- [x] Notificaciones de cambio de estado de pedido
- [x] Notificaciones de stock bajo
- [x] NotificationController para broadcast manual
- [x] Integrado con OrderService
- [x] SecurityConfig actualizado para WebSocket

### Día 15: Documentación con Swagger

- [x] Dependencia springdoc-openapi añadida
- [x] SwaggerConfig creado con JWT auth
- [x] SecurityConfig actualizado para Swagger
- [x] Todos los controllers documentados con @Tag y @Operation
- [x] Swagger UI accesible en /swagger-ui/index.html
- [x] Autenticación JWT integrada en Swagger

