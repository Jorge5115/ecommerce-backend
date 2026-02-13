# 🛒 E-commerce Full-Stack

Plataforma de e-commerce completa con Spring Boot + React + PostgreSQL + Redis

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
- [x] Migrado a MySQL

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
