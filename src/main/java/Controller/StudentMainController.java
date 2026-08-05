package Controller;

import Model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.List;

public class StudentMainController {

    @FXML private Label welcomeLabel;
    @FXML private StackPane contentArea;
    @FXML private Button btnCourses;
    @FXML private Button btnLesson;
    @FXML private Button btnTask;
    @FXML private Button btnGrades;

    private User currentUser;
    private final CursoDAO cursoDAO = new CursoDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final TareaDAO tareaDAO = new TareaDAO();
    private final DetalleCADAO detalleCADAO = new DetalleCADAO();
    private final DetalleTareasDAO detalleTareasDAO = new DetalleTareasDAO();

    private Curso cursoActual;          // curso seleccionado por el alumno (contexto de Lesson/Task)
    private List<Material> materialesActuales;
    private int indiceLeccionActual = 0;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Hola, " + user.getNombre() + " " + (user.getApellido() != null ? user.getApellido() : ""));
        showCourses();
    }

    // ---------------------------------------------------------------- Sidebar

    private void marcarActivo(Button activo) {
        for (Button b : new Button[]{btnCourses, btnLesson, btnTask, btnGrades}) {
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

    // ---------------------------------------------------------------- Courses

    @FXML
    private void showCourses() {
        marcarActivo(btnCourses);
        List<Curso> cursos = cursoDAO.getAllCourses();

        FlowPane flow = new FlowPane();
        flow.setHgap(20);
        flow.setVgap(20);

        for (Curso c : cursos) {
            flow.getChildren().add(crearTarjetaCurso(c));
        }

        ScrollPane scroll = new ScrollPane(flow);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        VBox root = new VBox(16);
        Label title = new Label("COURSES");
        title.getStyleClass().add("section-title");
        root.getChildren().addAll(title, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        setContent(root);
    }

    private VBox crearTarjetaCurso(Curso c) {
        VBox card = new VBox(8);
        card.getStyleClass().add("course-card");
        card.setPrefWidth(260);

        Label nivel = new Label("LEVEL " + c.getNivel());
        nivel.getStyleClass().add("badge");

        Label nombre = new Label(c.getNombre());
        nombre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        nombre.setWrapText(true);

        Label desc = new Label(c.getDescripcion() == null ? "" : c.getDescripcion());
        desc.setWrapText(true);
        desc.getStyleClass().add("muted-text");

        Label maestro = new Label("Profesor: " + (c.getNombreMaestro() != null ? c.getNombreMaestro() : "Sin asignar"));
        maestro.getStyleClass().add("muted-text");
        maestro.setStyle("-fx-font-size: 11px;");

        Button start = new Button("START");
        start.getStyleClass().add("btn-primary");
        start.setMaxWidth(Double.MAX_VALUE);
        start.setOnAction(e -> {
            cursoActual = c;
            inscribirSiNoLoEsta(c);
            showLesson();
        });

        card.getChildren().addAll(nivel, nombre, desc, maestro, start);
        return card;
    }

    private void inscribirSiNoLoEsta(Curso c) {
        try {
            detalleCADAO.inscribir(c.getIdCurso(), currentUser.getId());
        } catch (RuntimeException ignored) {
            // si ya esta inscrito o falla, no bloquea la navegacion
        }
    }

    // ---------------------------------------------------------------- Lesson

    @FXML
    private void showLesson() {
        marcarActivo(btnLesson);

        if (cursoActual == null) {
            setContent(mensajeSeleccionaCurso());
            return;
        }

        materialesActuales = materialDAO.listarPorCurso(cursoActual.getIdCurso());
        if (indiceLeccionActual >= materialesActuales.size()) indiceLeccionActual = 0;

        VBox root = new VBox(16);
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label levelLabel = new Label("LEVEL " + cursoActual.getNivel() + " · " + cursoActual.getNombre());
        levelLabel.getStyleClass().add("section-title");
        header.getChildren().add(levelLabel);

        if (materialesActuales.isEmpty()) {
            root.getChildren().addAll(header, new Label("Este curso aún no tiene lecciones publicadas."));
            setContent(root);
            return;
        }

        Material m = materialesActuales.get(indiceLeccionActual);

        VBox card = new VBox(14);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(700);

        Label leccionTitle = new Label("Lección " + m.getLeccion() + ": " + m.getNombre());
        leccionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label desc = new Label(m.getDescripcion());
        desc.setWrapText(true);
        desc.getStyleClass().add("muted-text");

        Button abrir = new Button("Abrir material");
        abrir.getStyleClass().add("btn-primary");
        abrir.setOnAction(e -> abrirArchivo(m.getArchivo(), m.getNombre()));

        card.getChildren().addAll(leccionTitle, desc, abrir);

        HBox nav = new HBox(16);
        nav.setAlignment(Pos.CENTER);
        Button former = new Button("FORMER");
        former.getStyleClass().add("btn-secondary");
        former.setDisable(indiceLeccionActual == 0);
        former.setOnAction(e -> { indiceLeccionActual--; showLesson(); });

        Button following = new Button("FOLLOWING");
        following.getStyleClass().add("btn-danger");
        following.setDisable(indiceLeccionActual >= materialesActuales.size() - 1);
        following.setOnAction(e -> { indiceLeccionActual++; showLesson(); });

        nav.getChildren().addAll(former, following);

        VBox center = new VBox(20, card, nav);
        center.setAlignment(Pos.TOP_CENTER);

        root.getChildren().addAll(header, center);
        setContent(root);
    }

    // ---------------------------------------------------------------- Task

    @FXML
    private void showTask() {
        marcarActivo(btnTask);

        if (cursoActual == null) {
            setContent(mensajeSeleccionaCurso());
            return;
        }

        List<Tarea> tareas = tareaDAO.listarPorCurso(cursoActual.getIdCurso());

        VBox root = new VBox(16);
        Label title = new Label("TASKS · " + cursoActual.getNombre());
        title.getStyleClass().add("section-title");
        root.getChildren().add(title);

        if (tareas.isEmpty()) {
            root.getChildren().add(new Label("No hay tareas asignadas para este curso."));
            setContent(root);
            return;
        }

        VBox lista = new VBox(10);
        for (Tarea t : tareas) {
            lista.getChildren().add(crearFilaTarea(t));
        }
        ScrollPane scroll = new ScrollPane(lista);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        root.getChildren().add(scroll);

        setContent(root);
    }

    private HBox crearFilaTarea(Tarea t) {
        HBox row = new HBox(14);
        row.getStyleClass().add("card");
        row.setAlignment(Pos.CENTER_LEFT);

        VBox info = new VBox(4);
        Label nombre = new Label(t.getNombre());
        nombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label fechas = new Label("Disponible: " + t.getfHabil() + "   Límite: " + t.getfLimit());
        fechas.getStyleClass().add("muted-text");
        info.getChildren().addAll(nombre, fechas);
        HBox.setHgrow(info, Priority.ALWAYS);

        DetalleTareas entrega = detalleTareasDAO.buscarEntrega(t.getIdTarea(), currentUser.getId());
        Label estado = new Label(entrega == null ? "Pendiente" : (entrega.getCalificacion() != null ? "Calificada: " + entrega.getCalificacion() : "Entregada"));
        estado.getStyleClass().add(entrega == null ? "badge-red" : "badge");

        Button verBtn = new Button("Ver / Entregar");
        verBtn.getStyleClass().add("btn-primary");
        verBtn.setOnAction(e -> mostrarDetalleTarea(t));

        row.getChildren().addAll(info, estado, verBtn);
        return row;
    }

    private void mostrarDetalleTarea(Tarea t) {
        Stage dialog = new Stage();
        dialog.setTitle(t.getNombre());

        VBox root = new VBox(14);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("app-bg");

        Label title = new Label(t.getNombre());
        title.getStyleClass().add("section-title");

        Label desc = new Label(t.getDescripcion());
        desc.setWrapText(true);

        Label fechas = new Label("Disponible desde: " + t.getfHabil() + "   |   Fecha límite: " + t.getfLimit());
        fechas.getStyleClass().add("muted-text");

        Label archivoLabel = new Label("Ningún archivo seleccionado");
        archivoLabel.getStyleClass().add("muted-text");
        final File[] archivoElegido = new File[1];

        Button elegirArchivo = new Button("Elegir archivo");
        elegirArchivo.getStyleClass().add("btn-secondary");
        elegirArchivo.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File f = fc.showOpenDialog(dialog);
            if (f != null) {
                archivoElegido[0] = f;
                archivoLabel.setText(f.getName());
            }
        });

        TextArea comentarios = new TextArea();
        comentarios.setPromptText("Comentarios (opcional)...");
        comentarios.setPrefRowCount(4);

        DetalleTareas entregaExistente = detalleTareasDAO.buscarEntrega(t.getIdTarea(), currentUser.getId());
        if (entregaExistente != null && entregaExistente.getComentarios() != null) {
            comentarios.setText(entregaExistente.getComentarios());
        }

        Label calificacionLabel = new Label();
        if (entregaExistente != null && entregaExistente.getCalificacion() != null) {
            calificacionLabel.setText("Calificación obtenida: " + entregaExistente.getCalificacion());
            calificacionLabel.setStyle("-fx-font-weight: bold;");
        }

        Button enviar = new Button("SEND");
        enviar.getStyleClass().add("btn-danger");
        enviar.setOnAction(e -> {
            try {
                byte[] bytes = archivoElegido[0] != null ? Files.readAllBytes(archivoElegido[0].toPath()) :
                        (entregaExistente != null ? entregaExistente.getArchivoEntrega() : null);
                boolean ok = detalleTareasDAO.entregar(t.getIdTarea(), currentUser.getId(), bytes, comentarios.getText());
                if (ok) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Tarea entregada correctamente.");
                    alert.showAndWait();
                    dialog.close();
                    showTask();
                } else {
                    new Alert(Alert.AlertType.ERROR, "No se pudo enviar la tarea.").showAndWait();
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error al leer el archivo: " + ex.getMessage()).showAndWait();
            }
        });

        root.getChildren().addAll(title, desc, fechas, new Separator(), archivoLabel, elegirArchivo, comentarios, calificacionLabel, enviar);

        Scene scene = new Scene(root, 500, 560);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }

    // ---------------------------------------------------------------- Grades

    @FXML
    private void showGrades() {
        marcarActivo(btnGrades);
        List<DetalleCA> inscripciones = detalleCADAO.listarPorAlumno(currentUser.getId());

        VBox root = new VBox(16);
        Label title = new Label("MY GRADES");
        title.getStyleClass().add("section-title");
        root.getChildren().add(title);

        TableView<DetalleCA> tabla = new TableView<>();
        TableColumn<DetalleCA, String> colCurso = new TableColumn<>("Curso");
        colCurso.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreCurso()));
        colCurso.setPrefWidth(320);

        TableColumn<DetalleCA, String> colCal = new TableColumn<>("Calificación");
        colCal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCalificacion() == null ? "Sin calificar" : String.valueOf(data.getValue().getCalificacion())));
        colCal.setPrefWidth(160);

        tabla.getColumns().addAll(colCurso, colCal);
        tabla.getItems().addAll(inscripciones);
        tabla.setPrefHeight(400);

        root.getChildren().add(tabla);
        setContent(root);
    }

    // ---------------------------------------------------------------- Utils

    private VBox mensajeSeleccionaCurso() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        Label l = new Label("Primero selecciona un curso desde \"Courses\" para ver este contenido.");
        l.getStyleClass().add("muted-text");
        Button ir = new Button("Ir a Courses");
        ir.getStyleClass().add("btn-primary");
        ir.setOnAction(e -> showCourses());
        box.getChildren().addAll(l, ir);
        return box;
    }

    private void abrirArchivo(byte[] datos, String nombreSugerido) {
        if (datos == null || datos.length == 0) {
            new Alert(Alert.AlertType.WARNING, "Este material no tiene un archivo adjunto.").showAndWait();
            return;
        }
        try {
            File temp = File.createTempFile("edulink_", "_" + nombreSugerido.replaceAll("[^a-zA-Z0-9._-]", "_"));
            try (FileOutputStream fos = new FileOutputStream(temp)) {
                fos.write(datos);
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(temp);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir el archivo: " + e.getMessage()).showAndWait();
        }
    }

    private void setContent(javafx.scene.Node node) {
        contentArea.getChildren().setAll(node);
    }
}
