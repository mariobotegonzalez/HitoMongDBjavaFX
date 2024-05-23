**Aplicación JavaFX con MongoDB**
Este proyecto es una aplicación JavaFX que se integra con una base de datos MongoDB para gestionar información de clientes. La aplicación proporciona una interfaz de usuario amigable para crear, leer, actualizar y eliminar registros de clientes. También incluye funciones de autenticación de usuarios.

**Características**
Autenticación de Usuarios: Los usuarios pueden registrarse e iniciar sesión en la aplicación.
Gestión de Clientes: Los usuarios pueden crear, leer, actualizar y eliminar registros de clientes.
Búsqueda en Tiempo Real: Buscar clientes por nombre en tiempo real.
Menú Contextual: Editar y eliminar clientes usando un menú contextual en la vista de tabla.
Interfaz de Usuario Responsiva: La aplicación cuenta con una interfaz de usuario responsiva e intuitiva.
**Tecnologías Utilizadas**
**JavaFX**: Para construir la interfaz gráfica de usuario.
**MongoDB**: Como base de datos para almacenar la información de clientes y usuarios.
**Maven**: Para gestionar las dependencias del proyecto.
**Comenzando**
**Prerrequisitos**
Java Development Kit (JDK) 8 o superior
Maven
Servidor MongoDB o cuenta en MongoDB Atlas

**Instalación**
Clonar el repositorio:
git clone https://github.com/tuusuario/tu-repo.git
cd tu-repo

**Configurar MongoDB:**
Asegúrate de tener un servidor MongoDB en funcionamiento o configura MongoDB Atlas.
Actualiza la cadena de conexión de MongoDB en el método conectarBaseDatos en HelloController.java y LoginController.java para que coincida con tu instancia de MongoDB.

**Construir el proyecto:**
mvn clean install

**Ejecutar la aplicación:**
mvn javafx:run

**Estructura del Proyecto**
src/main/java/com/empresa/hitojavafxmongo/
**HelloApplication.java:** Clase principal de la aplicación.
**HelloController.java:** Controlador para la vista principal.
**LoginController.java:** Controlador para la vista de inicio de sesión.
**RegisterController.java:** Controlador para la vista de registro.
**Cliente.java:** Clase modelo para los datos del cliente.
src/main/resources/com/empresa/hitojavafxmongo/
**hello-view.fxml:** Archivo FXML para la vista principal.
**login-view.fxml:** Archivo FXML para la vista de inicio de sesión.
**register-view.fxml:** Archivo FXML para la vista de registro.
**styles.css:** Archivo CSS para estilizar la aplicación.

**Uso**
Autenticación de Usuarios

**Registrar un nuevo usuario:**
Abre la aplicación.
Haz clic en "Crear Usuario".
Rellena los campos requeridos y haz clic en "Registrar".

**Iniciar sesión con un usuario existente:**
Abre la aplicación.
Ingresa tu nombre de usuario y contraseña.
Haz clic en "Login".
Gestión de Clientes

**Crear un nuevo cliente:**
Inicia sesión en la aplicación.
Haz clic en el botón "Crear".
Rellena los detalles del cliente y haz clic en "Guardar".

**Ver detalles de los clientes:**
Inicia sesión en la aplicación.
La vista de tabla muestra todos los clientes.

**Buscar un cliente:**
Inicia sesión en la aplicación.
Ingresa el nombre del cliente en el campo de búsqueda para filtrar los resultados.

**Editar un cliente:**
Haz clic derecho en un cliente en la vista de tabla.
Selecciona "Editar" del menú contextual.
Actualiza los detalles del cliente y haz clic en "Guardar".

**Eliminar un cliente:**
Haz clic derecho en un cliente en la vista de tabla.
Selecciona "Eliminar" del menú contextual.

Contribuciones
¡Las contribuciones son bienvenidas! Por favor, bifurca este repositorio y envía un pull request para cualquier mejora o corrección de errores.

Licencia
Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo LICENSE para más detalles.

Agradecimientos
MongoDB Java Driver
Documentación de JavaFX
