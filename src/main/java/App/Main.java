package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 560);
        scene.getStylesheets().add(Main.class.getResource("/css/styles.css").toExternalForm());
        stage.setTitle("EduLink English Academy");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
