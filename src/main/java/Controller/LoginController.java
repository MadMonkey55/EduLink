package Controller;

import Model.AuthDAO;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthDAO authDAO = new AuthDAO();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor, completa ambos campos.");
            return;
        }

        try {
            User user = authDAO.login(username, password);
            if (user != null) {
                abrirVentanaSegunRol(user);
            } else {
                mostrarError("Usuario o contraseña incorrectos.");
            }
        } catch (RuntimeException e) {
            mostrarError("No se pudo conectar a la base de datos.\n" + e.getMessage());
        }
    }

    @FXML
    private void handleGoToRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/RegisterView.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EduLink - Registro");
            stage.centerOnScreen();
        } catch (Exception e) {
            mostrarError("No se pudo abrir el registro: " + e.getMessage());
        }
    }

    private void abrirVentanaSegunRol(User user) {
        try {
            String vista;
            Object controller;

            switch (user.getRol()) {
                case AuthDAO.ROL_ADMIN:
                    vista = "/View/AdminMainView.fxml";
                    break;
                case AuthDAO.ROL_MAESTRO:
                    vista = "/View/TeacherMainView.fxml";
                    break;
                default:
                    vista = "/View/StudentMainView.fxml";
                    break;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(vista));
            Parent root = loader.load();
            controller = loader.getController();

            if (controller instanceof AdminMainController admin) {
                admin.setCurrentUser(user);
            } else if (controller instanceof TeacherMainController teacher) {
                teacher.setCurrentUser(user);
            } else if (controller instanceof StudentMainController student) {
                student.setCurrentUser(user);
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EduLink English Academy - " + user.getNombre());
            stage.setMaximized(true);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("No se pudo abrir la aplicación: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
