module mm55.id {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires com.github.librepdf.openpdf;
    requires java.desktop;

    exports Controller;
    opens Controller to javafx.fxml;

    exports Model;
    opens View to javafx.fxml;

    exports App;
    opens App to javafx.fxml;
}