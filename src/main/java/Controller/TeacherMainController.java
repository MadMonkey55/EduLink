package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class TeacherMainController {

    @FXML private Label welcomeLabel;
    @FXML private StackPane contentArea;
    @FXML private Button btnCursos;
    @FXML private Button btnMateriales;
    @FXML private Button btnTareas;
    @FXML private Button btnAlumnos;

    private User currentUser;
    private final CursoDAO cursoDAO = new CursoDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final TareaDAO tareaDAO = new TareaDAO();
    private final DetalleCADAO detalleCADAO = new DetalleCADAO();
    private final DetalleTareasDAO detalleTareasDAO = new DetalleTareasDAO();

    private ComboBox<Curso> selectorCursoMateriales;
    private ComboBox<Curso> selectorCursoTareas;
    private ComboBox<Curso> selectorCursoAlumnos;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Prof. " + user.getNombre() + " " + (user.getApellido() != null ? user.getApellido() : ""));
        showCursos();
    }

    private void marcarActivo(Button activo) {
        for (Button b : new Button[]{btnCursos, btnMateriales, btnTareas, btnAlumnos}) {
            b.getStyleClass().removeAll("sidebar-light-btn-active");
            if (!b.getStyleClass().contains("sidebar-light-btn")) b.getStyleClass().add("sidebar-light-btn");
        }
        activo.getStyleClass().remove("sidebar-light-btn");
        if (!activo.getStyleClass().contains("sidebar-light-btn-active")) activo.getStyleClass().add("sidebar-light-btn-active");
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/LoginView.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setTitle("EduLink - Login");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Curso> misCursos() {
        return cursoDAO.getCursosPorMaestro(currentUser.getId());
    }

    // -------------------------------------------------------------- Cursos

    @FXML
    private void showCursos() {
        marcarActivo(btnCursos);
        List<Curso> cursos = misCursos();

        VBox root = new VBox(16);
        Label title = new Label("Mis Cursos");
        title.getStyleClass().add("section-title");
        root.getChildren().add(title);

        if (cursos.isEmpty()) {
            root.getChildren().add(new Label("Todavía no tienes cursos asignados. Pídele al administrador que te asigne uno."));
        }

        FlowPane flow = new FlowPane();
        flow.setHgap(20);
        flow.setVgap(20);
        for (Curso c : cursos) {
            VBox card = new VBox(6);
            card.getStyleClass().add("course-card");
            card.setPrefWidth(260);
            Label nivel = new Label("LEVEL " + c.getNivel());
            nivel.getStyleClass().add("badge");
            Label nombre = new Label(c.getNombre());
            nombre.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
            Label desc = new Label(c.getDescripcion());
            desc.setWrapText(true);
            desc.getStyleClass().add("muted-text");
            Label fechas = new Label(c.getfDeInicio() + "  →  " + c.getfDeFinalizacion());
            fechas.getStyleClass().add("muted-text");
            fechas.setStyle("-fx-font-size: 11px;");
            card.getChildren().addAll(nivel, nombre, desc, fechas);
            flow.getChildren().add(card);
        }
        ScrollPane scroll = new ScrollPane(flow);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        root.getChildren().add(scroll);

        setContent(root);
    }

    // -------------------------------------------------------------- Materiales

    @FXML
    private void showMateriales() {
        marcarActivo(btnMateriales);
        List<Curso> cursos = misCursos();

        VBox root = new VBox(16);
        Label title = new Label("Materiales / Lecciones");
        title.getStyleClass().add("section-title");

        HBox selectorBox = new HBox(10);
        selectorBox.setAlignment(Pos.CENTER_LEFT);
        selectorCursoMateriales = new ComboBox<>();
        selectorCursoMateriales.getItems().addAll(cursos);
        selectorBox.getChildren().addAll(new Label("Curso:"), selectorCursoMateriales);

        VBox listaBox = new VBox(10);
        ScrollPane scroll = new ScrollPane(listaBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        Button agregar = new Button("+ Agregar material");
        agregar.getStyleClass().add("btn-primary");
        agregar.setOnAction(e -> {
            Curso c = selectorCursoMateriales.getValue();
            if (c == null) { new Alert(Alert.AlertType.WARNING, "Selecciona un curso primero.").showAndWait(); return; }
            mostrarDialogoNuevoMaterial(c, () -> refrescarMateriales(listaBox, c));
        });

        selectorCursoMateriales.setOnAction(e -> refrescarMateriales(listaBox, selectorCursoMateriales.getValue()));
        if (!cursos.isEmpty()) {
            selectorCursoMateriales.setValue(cursos.get(0));
            refrescarMateriales(listaBox, cursos.get(0));
        }

        root.getChildren().addAll(title, selectorBox, agregar, scroll);
        setContent(root);
    }

    private void refrescarMateriales(VBox listaBox, Curso curso) {
        listaBox.getChildren().clear();
        if (curso == null) return;
        List<Material> materiales = materialDAO.listarPorCurso(curso.getIdCurso());
        for (Material m : materiales) {
            HBox row = new HBox(14);
            row.getStyleClass().add("card");
            row.setAlignment(Pos.CENTER_LEFT);
            VBox info = new VBox(2);
            Label nombre = new Label("Lección " + m.getLeccion() + ": " + m.getNombre());
            nombre.setStyle("-fx-font-weight: bold;");
            Label desc = new Label(m.getDescripcion());
            desc.getStyleClass().add("muted-text");
            info.getChildren().addAll(nombre, desc);
            HBox.setHgrow(info, Priority.ALWAYS);

            Button eliminar = new Button("Eliminar");
            eliminar.getStyleClass().add("btn-danger");
            eliminar.setOnAction(e -> {
                materialDAO.eliminar(m.getIdMaterial());
                refrescarMateriales(listaBox, curso);
            });
            row.getChildren().addAll(info, eliminar);
            listaBox.getChildren().add(row);
        }
        if (materiales.isEmpty()) {
            listaBox.getChildren().add(new Label("Este curso aún no tiene materiales."));
        }
    }

    private void mostrarDialogoNuevoMaterial(Curso curso, Runnable alGuardar) {
        Stage dialog = new Stage();
        dialog.setTitle("Nuevo material - " + curso.getNombre());

        VBox root = new VBox(12);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("app-bg");

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre / título de la lección");
        TextArea descField = new TextArea();
        descField.setPromptText("Descripción");
        descField.setPrefRowCount(3);
        TextField leccionField = new TextField();
        leccionField.setPromptText("Número de lección (ej. 1)");

        Label archivoLabel = new Label("Ningún archivo seleccionado");
        final File[] archivo = new File[1];
        Button elegir = new Button("Elegir archivo");
        elegir.getStyleClass().add("btn-secondary");
        elegir.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File f = fc.showOpenDialog(dialog);
            if (f != null) { archivo[0] = f; archivoLabel.setText(f.getName()); }
        });

        Button guardar = new Button("Guardar");
        guardar.getStyleClass().add("btn-primary");
        guardar.setOnAction(e -> {
            try {
                if (nombreField.getText().trim().isEmpty() || leccionField.getText().trim().isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Nombre y número de lección son obligatorios.").showAndWait();
                    return;
                }
                Material m = new Material();
                m.setIdCurso(curso.getIdCurso());
                m.setIdMaestro(currentUser.getId());
                m.setNombre(nombreField.getText().trim());
                m.setCurso(curso.getNombre());
                m.setDescripcion(descField.getText());
                m.setLeccion(Integer.parseInt(leccionField.getText().trim()));
                m.setArchivo(archivo[0] != null ? Files.readAllBytes(archivo[0].toPath()) : new byte[0]);

                materialDAO.crear(m);
                dialog.close();
                alGuardar.run();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
            }
        });

        root.getChildren().addAll(new Label("Nombre"), nombreField, new Label("Descripción"), descField,
                new Label("Lección #"), leccionField, archivoLabel, elegir, guardar);

        Scene scene = new Scene(root, 420, 480);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }

    // -------------------------------------------------------------- Tareas

    @FXML
    private void showTareas() {
        marcarActivo(btnTareas);
        List<Curso> cursos = misCursos();

        VBox root = new VBox(16);
        Label title = new Label("Tareas");
        title.getStyleClass().add("section-title");

        HBox selectorBox = new HBox(10);
        selectorBox.setAlignment(Pos.CENTER_LEFT);
        selectorCursoTareas = new ComboBox<>();
        selectorCursoTareas.getItems().addAll(cursos);
        selectorBox.getChildren().addAll(new Label("Curso:"), selectorCursoTareas);

        VBox listaBox = new VBox(10);
        ScrollPane scroll = new ScrollPane(listaBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        Button agregar = new Button("+ Nueva tarea");
        agregar.getStyleClass().add("btn-primary");
        agregar.setOnAction(e -> {
            Curso c = selectorCursoTareas.getValue();
            if (c == null) { new Alert(Alert.AlertType.WARNING, "Selecciona un curso primero.").showAndWait(); return; }
            mostrarDialogoNuevaTarea(c, () -> refrescarTareas(listaBox, c));
        });

        selectorCursoTareas.setOnAction(e -> refrescarTareas(listaBox, selectorCursoTareas.getValue()));
        if (!cursos.isEmpty()) {
            selectorCursoTareas.setValue(cursos.get(0));
            refrescarTareas(listaBox, cursos.get(0));
        }

        root.getChildren().addAll(title, selectorBox, agregar, scroll);
        setContent(root);
    }

    private void refrescarTareas(VBox listaBox, Curso curso) {
        listaBox.getChildren().clear();
        if (curso == null) return;
        List<Tarea> tareas = tareaDAO.listarPorCurso(curso.getIdCurso());
        for (Tarea t : tareas) {
            HBox row = new HBox(14);
            row.getStyleClass().add("card");
            row.setAlignment(Pos.CENTER_LEFT);
            VBox info = new VBox(2);
            Label nombre = new Label(t.getNombre());
            nombre.setStyle("-fx-font-weight: bold;");
            Label fechas = new Label("Límite: " + t.getfLimit());
            fechas.getStyleClass().add("muted-text");
            info.getChildren().addAll(nombre, fechas);
            HBox.setHgrow(info, Priority.ALWAYS);

            Button verEntregas = new Button("Ver entregas");
            verEntregas.getStyleClass().add("btn-secondary");
            verEntregas.setOnAction(e -> mostrarEntregas(t));

            Button eliminar = new Button("Eliminar");
            eliminar.getStyleClass().add("btn-danger");
            eliminar.setOnAction(e -> {
                tareaDAO.eliminar(t.getIdTarea());
                refrescarTareas(listaBox, curso);
            });

            row.getChildren().addAll(info, verEntregas, eliminar);
            listaBox.getChildren().add(row);
        }
        if (tareas.isEmpty()) {
            listaBox.getChildren().add(new Label("Este curso aún no tiene tareas."));
        }
    }

    private void mostrarDialogoNuevaTarea(Curso curso, Runnable alGuardar) {
        Stage dialog = new Stage();
        dialog.setTitle("Nueva tarea - " + curso.getNombre());

        VBox root = new VBox(12);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("app-bg");

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre de la tarea");
        TextArea descField = new TextArea();
        descField.setPromptText("Descripción / instrucciones");
        descField.setPrefRowCount(4);
        DatePicker fHabil = new DatePicker(LocalDate.now());
        DatePicker fLimit = new DatePicker(LocalDate.now().plusDays(7));

        Button guardar = new Button("Guardar");
        guardar.getStyleClass().add("btn-primary");
        guardar.setOnAction(e -> {
            if (nombreField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "El nombre es obligatorio.").showAndWait();
                return;
            }
            Tarea t = new Tarea();
            t.setNombre(nombreField.getText().trim());
            t.setDescripcion(descField.getText());
            t.setfHabil(Date.valueOf(fHabil.getValue()));
            t.setfLimit(Date.valueOf(fLimit.getValue()));
            t.setIdCurso(curso.getIdCurso());
            t.setIdMaestro(currentUser.getId());
            tareaDAO.crear(t);
            dialog.close();
            alGuardar.run();
        });

        root.getChildren().addAll(new Label("Nombre"), nombreField, new Label("Descripción"), descField,
                new Label("Disponible desde"), fHabil, new Label("Fecha límite"), fLimit, guardar);

        Scene scene = new Scene(root, 420, 520);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }

    private void mostrarEntregas(Tarea t) {
        Stage dialog = new Stage();
        dialog.setTitle("Entregas - " + t.getNombre());

        VBox root = new VBox(12);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("app-bg");
        Label title = new Label("Entregas de: " + t.getNombre());
        title.getStyleClass().add("section-title");

        VBox lista = new VBox(10);
        List<DetalleTareas> entregas = detalleTareasDAO.listarPorTarea(t.getIdTarea());
        for (DetalleTareas d : entregas) {
            VBox card = new VBox(6);
            card.getStyleClass().add("card");
            Label nombre = new Label(d.getNombreAlumno());
            nombre.setStyle("-fx-font-weight: bold;");
            Label fecha = new Label("Entregado: " + d.getfDeEntrega());
            fecha.getStyleClass().add("muted-text");
            Label comentarios = new Label("Comentarios: " + (d.getComentarios() == null ? "-" : d.getComentarios()));
            comentarios.setWrapText(true);

            HBox calRow = new HBox(10);
            calRow.setAlignment(Pos.CENTER_LEFT);
            TextField calField = new TextField(d.getCalificacion() == null ? "" : String.valueOf(d.getCalificacion()));
            calField.setPromptText("Calificación");
            calField.setPrefWidth(100);
            Button calificarBtn = new Button("Calificar");
            calificarBtn.getStyleClass().add("btn-primary");
            calificarBtn.setOnAction(e -> {
                try {
                    double cal = Double.parseDouble(calField.getText().trim());
                    detalleTareasDAO.calificar(d.getIdDetalle(), cal, d.getComentarios());
                    new Alert(Alert.AlertType.INFORMATION, "Calificación guardada.").showAndWait();
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.WARNING, "Ingresa una calificación numérica válida.").showAndWait();
                }
            });
            calRow.getChildren().addAll(new Label("Calificación:"), calField, calificarBtn);

            card.getChildren().addAll(nombre, fecha, comentarios, calRow);
            lista.getChildren().add(card);
        }
        if (entregas.isEmpty()) {
            lista.getChildren().add(new Label("Nadie ha entregado esta tarea todavía."));
        }

        ScrollPane scroll = new ScrollPane(lista);
        scroll.setFitToWidth(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        root.getChildren().addAll(title, scroll);

        Scene scene = new Scene(root, 560, 600);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }

    // -------------------------------------------------------------- Alumnos inscritos

    @FXML
    private void showAlumnos() {
        marcarActivo(btnAlumnos);
        List<Curso> cursos = misCursos();

        VBox root = new VBox(16);
        Label title = new Label("Alumnos inscritos");
        title.getStyleClass().add("section-title");

        HBox selectorBox = new HBox(10);
        selectorBox.setAlignment(Pos.CENTER_LEFT);
        selectorCursoAlumnos = new ComboBox<>();
        selectorCursoAlumnos.getItems().addAll(cursos);
        selectorBox.getChildren().addAll(new Label("Curso:"), selectorCursoAlumnos);

        TableView<DetalleCA> tabla = new TableView<>();
        TableColumn<DetalleCA, String> colAlumno = new TableColumn<>("Alumno");
        colAlumno.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreAlumno()));
        colAlumno.setPrefWidth(280);
        TableColumn<DetalleCA, String> colCal = new TableColumn<>("Calificación");
        colCal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCalificacion() == null ? "Sin calificar" : String.valueOf(data.getValue().getCalificacion())));
        colCal.setPrefWidth(140);
        tabla.getColumns().addAll(colAlumno, colCal);
        tabla.setPrefHeight(420);

        selectorCursoAlumnos.setOnAction(e -> {
            Curso c = selectorCursoAlumnos.getValue();
            tabla.getItems().setAll(c != null ? detalleCADAO.listarPorCurso(c.getIdCurso()) : List.of());
        });
        if (!cursos.isEmpty()) {
            selectorCursoAlumnos.setValue(cursos.get(0));
            tabla.getItems().setAll(detalleCADAO.listarPorCurso(cursos.get(0).getIdCurso()));
        }

        root.getChildren().addAll(title, selectorBox, tabla);
        setContent(root);
    }

    private void setContent(javafx.scene.Node node) {
        contentArea.getChildren().setAll(node);
    }
}
