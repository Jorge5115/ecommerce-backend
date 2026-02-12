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
