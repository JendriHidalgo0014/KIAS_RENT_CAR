🚗 Kia's Rent Car - Sistema de Alquiler de Vehículos

📱 Descripción del Proyecto
Kia's Rent Car es una aplicación móvil Android desarrollada en Kotlin que permite a los usuarios alquilar vehículos de la marca KIA de manera fácil, rápida y segura. El sistema cuenta con dos tipos de usuarios: Clientes y Administradores, cada uno con funcionalidades específicas para gestionar reservas y vehículos.
La aplicación implementa arquitectura Clean Architecture con patrón MVVM, utilizando las mejores prácticas de desarrollo Android moderno con Jetpack Compose, garantizando una experiencia de usuario fluida, intuitiva y profesional.

✨ Características Principales

👤 Funcionalidades para Clientes:

✅ Autenticación segura (Login y Registro)
🔍 Búsqueda y filtrado de vehículos disponibles
📋 Catálogo completo de vehículos KIA con detalles
📅 Sistema de reservas con selección de fechas
💳 Pago simulado con tarjeta de crédito
💰 Cálculo automático de precio total según días de alquiler
📊 Historial de reservas (Pendientes, Confirmadas, Completadas, Canceladas)
❌ Cancelación de reservas activas
👨‍💼 Gestión de perfil con foto personalizada

🛠️ Funcionalidades para Administradores:

📊 Dashboard con métricas del sistema
➕ CRUD completo de vehículos (Crear, Leer, Actualizar, Eliminar)
💵 Gestión de precios por día de cada vehículo
👥 Visualización de usuarios registrados
📈 Estadísticas de vehículos más rentados
🔄 Gestión de reservas (cambiar estados)
👨‍💼 Perfil personalizado con foto


🏗️ Arquitectura y Tecnologías
Arquitectura:

Clean Architecture (Capas: Presentación, Dominio, Datos)
MVVM (Model-View-ViewModel)
Repository Pattern
Use Cases (Casos de Uso)

Tecnologías Utilizadas:

Kotlin - Lenguaje de programación principal
Jetpack Compose - UI moderna y declarativa
Room Database - Base de datos local SQLite
Retrofit - Cliente HTTP para API REST
Hilt - Inyección de dependencias
Coroutines & Flow - Programación asíncrona
Navigation Compose - Navegación entre pantallas
Coil - Carga de imágenes
DataStore - Almacenamiento de preferencias (sesión)
Material Design 3 - Diseño moderno

🎯 Casos de Uso Principales
Clientes:

Registrarse e iniciar sesión
Explorar catálogo de vehículos KIA
Buscar y filtrar vehículos por modelo, tipo, precio
Ver detalles completos de cada vehículo
Crear reserva seleccionando fechas
Realizar pago simulado con tarjeta
Ver historial y estado de reservas
Cancelar reservas activas
Gestionar perfil personal

Administradores:

Agregar nuevos vehículos al sistema
Editar información y precios de vehículos
Eliminar vehículos del catálogo
Ver lista completa de usuarios registrados
Consultar estadísticas de vehículos rentados
Gestionar estados de reservas
Monitorear dashboard del sistema

👨‍💻 Autor
Jendri Hidalgo - Estudiante de Ingenieria en Sistemas
GitHub: @tu-usuario
Email: tu-email@ejemplo.com
