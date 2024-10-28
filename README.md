# Direct Health
Direct Health es una aplicación móvil diseñada para el área de la salud, que busca introducir un nuevo método de comunicación entre los médicos y sus pacientes. Es una plataforma que integra varias funcionalidades para facilitar la gestión de la atención médica:
- Creación de citas
- Gestión de recetas
- Notificaciones y recordatorios
- Visualización del historial médico
- Búsqueda de perfiles

## Servicios
Se utilizará la Direct Health API, un servicio que desarrollaremos nosotros como parte de este proyecto. Esta será la encargada de crear usuarios, recetas y citas, así como de almacenarlos y leerlos para su uso dentro de la aplicación.

## Librerías
1. **Navigation:** Encargada de la navegación *type-safe* entre pantallas utilizando serializables.
2. **SplashScreen:** Utilizada para personalizar la splash screen al abrir la aplicación mientras se realizan las validaciones iniciales.
3. **Room:** Almacena localmente la información proveniente de la API.
4. **Ktor:** Permite acceder a nuestro servicio externo para obtener la información de los usuarios.

**Autores:** Victor Pérez y Juan Solís
