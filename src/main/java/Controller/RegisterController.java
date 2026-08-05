package Controller;

import Model.Alumno;
import Model.AlumnoDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private TextField correoField;
    @FXML private TextField matriculaField;
    @FXML private PasswordField passwordField;
    @FXML private TextField edadField;
    @FXML private TextField telefonoField;
    @FXML private TextField domicilioField;
    @FXML private TextField localidadField;
    @FXML private Label errorLabel;

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();

    @FXML
    private void initialize(){
        configurarMatricula();
        configurarEdad();
        configurarTelefono();
    }

    @FXML
    private void handleRegistrar() {
        String nombre = trim(nombreField);
        String apellido = trim(apellidoField);
        String correo = trim(correoField);
        String matricula = trim(matriculaField);
        String password = passwordField.getText() == null ? "" : passwordField.getText();
        String edadStr = trim(edadField);
        String telefono = trim(telefonoField);
        String domicilio = trim(domicilioField);
        String localidad = trim(localidadField);

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            mostrarError("Nombre, apellidos, correo, matrícula y contraseña son obligatorios.");
            return;
        }

        try {
            matricula = matriculaField.getText().trim();
            if (!matricula.matches("\\d{8}")) {
                mostrarError("La matrícula debe ser un número de exactamente 8 dígitos.");
                matriculaField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            mostrarError("La matrícula debe ser un número.");
            return;
        }

        int edad = 0;
        if (!edadStr.isEmpty()) {
            try {
                edad = Integer.parseInt(edadStr);
            } catch (NumberFormatException e) {
                mostrarError("La edad debe ser un número.");
                return;
            }
            if (edad < 1 || edad > 99){
                mostrarError("La edad de be estar entre 1 y 99");
                edadField.requestFocus();
                return;
            }
        }

        try {
            if (alumnoDAO.existeCorreoOMatricula(correo, Integer.parseInt(matricula))) {
                mostrarError("Ya existe un alumno registrado con ese correo o matrícula.");
                return;
            }

            Alumno alumno = new Alumno();
            alumno.setNombre(nombre);
            alumno.setApellido(apellido);
            alumno.setCorreo(correo);
            alumno.setMatricula(Integer.parseInt(matricula));
            alumno.setPassword(password);
            alumno.setDomicilio(domicilio);
            alumno.setLocalidad(localidad);
            alumno.setTelefono(telefono);
            alumno.setEdad(edad);

            boolean ok = alumnoDAO.registrar(alumno);
            if (ok) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro exitoso");
                alert.setHeaderText(null);
                alert.setContentText("¡Cuenta creada correctamente! Ahora puedes iniciar sesión.");
                alert.showAndWait();
                handleCancelar();
            } else {
                mostrarError("No se pudo completar el registro. Intenta de nuevo.");
            }
        } catch (RuntimeException e) {
            mostrarError("Error de conexión con la base de datos: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/LoginView.fxml"));
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EduLink - Login");
            stage.centerOnScreen();
        } catch (Exception e) {
            mostrarError("No se pudo volver al login: " + e.getMessage());
        }
    }

    private String trim(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void configurarMatricula() {
        // Usamos un TextFormatter para filtrar solo dígitos y limitar a 8 caracteres
        TextFormatter<String> matriculaFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Permitir solo dígitos y máximo 8 caracteres
            if (newText.matches("\\d{0,8}")) {
                return change;
            }
            return null;
        });
        matriculaField.setTextFormatter(matriculaFormatter);

        // Listener para validar que sean exactamente 8 dígitos (y no vacío)
        matriculaField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() == 8 && newVal.matches("\\d{8}")) {
                matriculaField.setStyle(null); // borde normal
            } else {
                matriculaField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        });
    }

    private void configurarEdad(){
        TextFormatter<String> edadFormater = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,2}")) {
                return change;
            }
            return null;
        });
        edadField.setTextFormatter(edadFormater);

        edadField.focusedProperty().addListener((obs,wasFocus,isFocus)-> {
            if (!isFocus) {
                String val = edadField.getText();
                if(!val.isEmpty()) {
                    int edad = Integer.parseInt(val);
                    if (edad < 1 || edad > 99) {
                        edadField.setStyle("-fx-border-color:red; -fx-border-width: 2px ");
                    } else {
                        edadField.setStyle(null);
                    }
                }
            }
        });
    }

    private void configurarTelefono(){
        TextFormatter<String> telefonoFormater = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,10}")) {
                return change;
            }
            return null;
        });
        telefonoField.setTextFormatter(telefonoFormater);

        telefonoField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty() || (newVal.length() == 10 && newVal.matches("\\d{10}"))) {
                telefonoField.setStyle(null);
            } else {
                telefonoField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        });
    }
}
