# EduLink English Academy — App de escritorio (JavaFX)

Aplicación de escritorio para la academia **EduLink English Academy**, construida en
**Java 22 + JavaFX 22 + Maven**, conectada a una base de datos **MySQL/MariaDB**.

Incluye 3 roles con su propia interfaz:

- **Alumno**: ve sus cursos, se inscribe, consulta lecciones (Lesson), entrega tareas (Task) y ve sus calificaciones.
- **Profesor**: ve sus cursos asignados, sube materiales/lecciones, crea tareas, revisa entregas y califica, y ve la lista de alumnos inscritos.
- **Administrador**: dashboard con estadísticas, CRUD de estudiantes y profesores, CRUD de cursos (con asignación de profesor), gestión de inscripciones y un módulo de reportes con exportación a PDF.

## 1. Requisitos

- JDK 22 (o superior compatible con `--release 22`)
- Maven 3.9+
- MySQL o MariaDB corriendo en `localhost:3306`

## 2. Base de datos

1. Crea la base de datos e importa el script incluido en `database/edulink.sql`
   (es el mismo dump que ya tenías, con las tablas `administrador`, `alumno`, `maestro`,
   `curso`, `material`, `tarea`, `detalleCA`, `detalleTareas`).

   Desde phpMyAdmin: **Importar** → selecciona `database/edulink.sql`.

   O desde la terminal:
   ```bash
   mysql -u root -p < database/edulink.sql
   ```

2. La conexión está configurada en `src/main/java/Model/ConexionBD.java`:
   ```java
   URL  = jdbc:mysql://localhost:3306/edulink
   user = root
   pass = "" (vacío)
   ```
   Si tu MySQL usa otro usuario/contraseña, edita esos 3 valores.

## 3. Ejecutar la aplicación

```bash
mvn clean javafx:run
```

También puedes generar un ejecutable empaquetado con jlink:
```bash
mvn clean javafx:jlink
```
(el resultado queda en `target/app`).

## 4. Usuarios de prueba (ya vienen en el dump)

| Rol         | Usuario (correo)                 | Contraseña |
|-------------|-----------------------------------|------------|
| Administrador | admin12345@gmail.com            | admin12345 |
| Profesor      | laura.gonzalez@edulink.com      | 123456     |
| Alumno        | dariofernandezrubio@gmail.com   | dario123   |

En la pantalla de **Login** también puedes ingresar como alumno usando tu **matrícula**
en vez del correo (por ejemplo `25325005`).

## 5. Registro de nuevos alumnos

Desde el login, el enlace **Register** abre el formulario de alta de alumnos
(inserta directamente en la tabla `alumno`).

## 6. Estructura del proyecto

```
src/main/java/
  App/Main.java                 -> arranque de la aplicación (carga LoginView)
  Controller/                   -> controladores JavaFX (uno por vista/rol)
  Model/                        -> entidades (Alumno, Maestro, Curso, Tarea, Material,
                                    DetalleCA, DetalleTareas) + DAOs de acceso a datos
  module-info.java

src/main/resources/
  View/*.fxml                   -> pantallas (Login, Register, y los 3 paneles principales)
  css/styles.css                -> estilos globales (paleta azul marino / rojo EduLink)
  img/g2278.png                 -> logo EduLink English Academy

database/edulink.sql            -> script de la base de datos
```

## 7. Notas importantes

- Los archivos de materiales y las entregas de tareas se guardan como `BLOB` directamente
  en la base de datos (columnas `longblob`), tal como estaba definido en el esquema original.
  Al abrir un material, la app lo escribe a un archivo temporal y lo abre con la aplicación
  predeterminada del sistema operativo.
- El reporte de calificaciones por curso (rol Administrador → Reportes) se puede exportar
  a PDF con el botón **Export PDF** (usa la librería OpenPDF, ya incluida en el `pom.xml`).
- Un alumno puede inscribirse a un curso simplemente entrando a **Courses → START**; esto
  crea el registro correspondiente en `detalleCA`.
