package Controller;

import Model.*;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
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

import java.io.FileOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AdminMainController {

    @FXML private Label welcomeLabel;
    @FXML private Label screenTitleLabel;
    @FXML private StackPane contentArea;
    @FXML private Button btnDashboard;
    @FXML private Button btnAlumnos;
    @FXML private Button btnMaestros;
    @FXML private Button btnCursos;
    @FXML private Button btnInscripciones;
    @FXML private Button btnReportes;

    private User currentUser;
    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private final MaestroDAO maestroDAO = new MaestroDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final TareaDAO tareaDAO = new TareaDAO();
    private final DetalleCADAO detalleCADAO = new DetalleCADAO();
    private final DetalleTareasDAO detalleTareasDAO = new DetalleTareasDAO();

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText(user.getNombre());
        showDashboard();
    }

    private void marcarActivo(Button activo) {
        for (Button b : new Button[]{btnDashboard, btnAlumnos, btnMaestros, btnCursos, btnInscripciones, btnReportes}) {
            b.getStyleClass().removeAll("sidebar-btn-active");
            if (!b.getStyleClass().contains("sidebar-btn")) b.getStyleClass().add("sidebar-btn");
        }
        activo.getStyleClass().remove("sidebar-btn");
        if (!activo.getStyleClass().contains("sidebar-btn-active")) activo.getStyleClass().add("sidebar-btn-active");
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

    // ================================================================ DASHBOARD

    @FXML
    private void showDashboard() {
        marcarActivo(btnDashboard);
        screenTitleLabel.setText("System Overview");

        VBox root = new VBox(20);
        Label title = new Label("ADMIN DASHBOARD");
        title.getStyleClass().add("section-title");

        HBox stats = new HBox(20);
        stats.getChildren().addAll(
                statCard(String.valueOf(alumnoDAO.contarAlumnos()), "Students"),
                statCard(String.valueOf(maestroDAO.contarMaestros()), "Teachers"),
                statCard(String.valueOf(cursoDAO.contarCursos()), "Courses"),
                statCard(String.valueOf(materialDAO.contarMateriales()), "Materials"),
                statCard(String.valueOf(detalleTareasDAO.contarPendientesPorCalificar()), "Sin calificar")
        );

        root.getChildren().addAll(title, stats);
        setContent(root);
    }

    private VBox statCard(String value, String label) {
        VBox card = new VBox(6);
        card.getStyleClass().add("card");
        card.setPrefWidth(190);
        Label v = new Label(value);
        v.getStyleClass().add("stat-card-value");
        Label l = new Label(label.toUpperCase());
        l.getStyleClass().add("stat-card-label");
        card.getChildren().addAll(v, l);
        return card;
    }

    // ================================================================ ESTUDIANTES

    @FXML
    private void showAlumnos() {
        marcarActivo(btnAlumnos);
        screenTitleLabel.setText("Student Management");

        VBox root = new VBox(16);
        Label title = new Label("Estudiantes");
        title.getStyleClass().add("section-title");

        TableView<Alumno> tabla = new TableView<>();
        tabla.setPrefHeight(500);
        agregarColumna(tabla, "Matrícula", d -> String.valueOf(d.getMatricula()), 100);
        agregarColumna(tabla, "Nombre", Alumno::getNombreCompleto, 220);
        agregarColumna(tabla, "Correo", Alumno::getCorreo, 220);
        agregarColumna(tabla, "Localidad", Alumno::getLocalidad, 150);
        agregarColumna(tabla, "Teléfono", Alumno::getTelefono, 120);
        tabla.getItems().addAll(alumnoDAO.listarTodos());

        HBox botones = new HBox(10);
        Button agregar = new Button("+ Agregar estudiante");
        agregar.getStyleClass().add("btn-primary");
        agregar.setOnAction(e -> mostrarDialogoAlumno(null, () -> refrescarTabla(tabla, alumnoDAO.listarTodos())));

        Button editar = new Button("Editar");
        editar.getStyleClass().add("btn-secondary");
        editar.setOnAction(e -> {
            Alumno sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { avisar("Selecciona un estudiante de la tabla."); return; }
            mostrarDialogoAlumno(sel, () -> refrescarTabla(tabla, alumnoDAO.listarTodos()));
        });

        Button eliminar = new Button("Eliminar");
        eliminar.getStyleClass().add("btn-danger");
        eliminar.setOnAction(e -> {
            Alumno sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { avisar("Selecciona un estudiante de la tabla."); return; }
            if (confirmar("¿Eliminar al estudiante " + sel.getNombreCompleto() + "?")) {
                alumnoDAO.eliminar(sel.getIdAlumno());
                refrescarTabla(tabla, alumnoDAO.listarTodos());
            }
        });

        botones.getChildren().addAll(agregar, editar, eliminar);
        root.getChildren().addAll(title, botones, tabla);
        setContent(root);
    }

    private void mostrarDialogoAlumno(Alumno existente, Runnable alGuardar) {
        Stage dialog = new Stage();
        dialog.setTitle(existente == null ? "Nuevo estudiante" : "Editar estudiante");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(24));

        TextField nombre = new TextField(existente != null ? existente.getNombre() : "");
        TextField apellido = new TextField(existente != null ? existente.getApellido() : "");
        TextField correo = new TextField(existente != null ? existente.getCorreo() : "");
        TextField matricula = new TextField(existente != null ? String.valueOf(existente.getMatricula()) : "");
        PasswordField password = new PasswordField();
        if (existente != null) password.setText(existente.getPassword());
        TextField telefono = new TextField(existente != null ? existente.getTelefono() : "");
        TextField domicilio = new TextField(existente != null ? existente.getDomicilio() : "");
        TextField localidad = new TextField(existente != null ? existente.getLocalidad() : "");
        TextField edad = new TextField(existente != null ? String.valueOf(existente.getEdad()) : "");

        int r = 0;
        grid.addRow(r++, new Label("Nombre"), nombre);
        grid.addRow(r++, new Label("Apellido"), apellido);
        grid.addRow(r++, new Label("Correo"), correo);
        grid.addRow(r++, new Label("Matrícula"), matricula);
        grid.addRow(r++, new Label("Password"), password);
        grid.addRow(r++, new Label("Teléfono"), telefono);
        grid.addRow(r++, new Label("Domicilio"), domicilio);
        grid.addRow(r++, new Label("Localidad"), localidad);
        grid.addRow(r++, new Label("Edad"), edad);

        Button guardar = new Button("Guardar");
        guardar.getStyleClass().add("btn-primary");
        guardar.setOnAction(e -> {
            try {
                Alumno a = existente != null ? existente : new Alumno();
                a.setNombre(nombre.getText().trim());
                a.setApellido(apellido.getText().trim());
                a.setCorreo(correo.getText().trim());
                a.setMatricula(Integer.parseInt(matricula.getText().trim()));
                a.setPassword(password.getText());
                a.setTelefono(telefono.getText().trim());
                a.setDomicilio(domicilio.getText().trim());
                a.setLocalidad(localidad.getText().trim());
                a.setEdad(edad.getText().isBlank() ? 0 : Integer.parseInt(edad.getText().trim()));

                boolean ok = existente == null ? alumnoDAO.registrar(a) : alumnoDAO.actualizar(a);
                if (ok) { dialog.close(); alGuardar.run(); }
                else avisar("No se pudo guardar el estudiante.");
            } catch (NumberFormatException ex) {
                avisar("Matrícula y edad deben ser numéricos.");
            }
        });
        grid.addRow(r, guardar);

        Scene scene = new Scene(grid, 380, 480);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }

    // ================================================================ PROFESORES

    @FXML
    private void showMaestros() {
        marcarActivo(btnMaestros);
        screenTitleLabel.setText("Teacher Management");

        VBox root = new VBox(16);
        Label title = new Label("Profesores");
        title.getStyleClass().add("section-title");

        TableView<Maestro> tabla = new TableView<>();
        tabla.setPrefHeight(500);
        agregarColumna(tabla, "Nombre", Maestro::getNombreCompleto, 220);
        agregarColumna(tabla, "Correo", Maestro::getCorreo, 220);
        agregarColumna(tabla, "Localidad", Maestro::getLocalidad, 150);
        agregarColumna(tabla, "Teléfono", Maestro::getTelefono, 120);
        tabla.getItems().addAll(maestroDAO.listarTodos());

        HBox botones = new HBox(10);
        Button agregar = new Button("+ Agregar profesor");
        agregar.getStyleClass().add("btn-primary");
        agregar.setOnAction(e -> mostrarDialogoMaestro(null, () -> refrescarTabla(tabla, maestroDAO.listarTodos())));

        Button editar = new Button("Editar");
        editar.getStyleClass().add("btn-secondary");
        editar.setOnAction(e -> {
            Maestro sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { avisar("Selecciona un profesor de la tabla."); return; }
            mostrarDialogoMaestro(sel, () -> refrescarTabla(tabla, maestroDAO.listarTodos()));
        });

        Button eliminar = new Button("Eliminar");
        eliminar.getStyleClass().add("btn-danger");
        eliminar.setOnAction(e -> {
            Maestro sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { avisar("Selecciona un profesor de la tabla."); return; }
            if (confirmar("¿Eliminar al profesor " + sel.getNombreCompleto() + "?")) {
                maestroDAO.eliminar(sel.getIdMaestro());
                refrescarTabla(tabla, maestroDAO.listarTodos());
            }
        });

        botones.getChildren().addAll(agregar, editar, eliminar);
        root.getChildren().addAll(title, botones, tabla);
        setContent(root);
    }

    private void mostrarDialogoMaestro(Maestro existente, Runnable alGuardar) {
        Stage dialog = new Stage();
        dialog.setTitle(existente == null ? "Nuevo profesor" : "Editar profesor");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(24));

        TextField nombre = new TextField(existente != null ? existente.getNombre() : "");
        TextField apellido = new TextField(existente != null ? existente.getApellido() : "");
        TextField correo = new TextField(existente != null ? existente.getCorreo() : "");
        PasswordField password = new PasswordField();
        if (existente != null) password.setText(existente.getPassword());
        TextField telefono = new TextField(existente != null ? existente.getTelefono() : "");
        TextField domicilio = new TextField(existente != null ? existente.getDomicilio() : "");
        TextField localidad = new TextField(existente != null ? existente.getLocalidad() : "");
        TextField edad = new TextField(existente != null ? String.valueOf(existente.getEdad()) : "");

        int r = 0;
        grid.addRow(r++, new Label("Nombre"), nombre);
        grid.addRow(r++, new Label("Apellido"), apellido);
        grid.addRow(r++, new Label("Correo"), correo);
        grid.addRow(r++, new Label("Password"), password);
        grid.addRow(r++, new Label("Teléfono"), telefono);
        grid.addRow(r++, new Label("Domicilio"), domicilio);
        grid.addRow(r++, new Label("Localidad"), localidad);
        grid.addRow(r++, new Label("Edad"), edad);

        Button guardar = new Button("Guardar");
        guardar.getStyleClass().add("btn-primary");
        guardar.setOnAction(e -> {
            try {
                Maestro m = existente != null ? existente : new Maestro();
                m.setNombre(nombre.getText().trim());
                m.setApellido(apellido.getText().trim());
                m.setCorreo(correo.getText().trim());
                m.setPassword(password.getText());
                m.setTelefono(telefono.getText().trim());
                m.setDomicilio(domicilio.getText().trim());
                m.setLocalidad(localidad.getText().trim());
                m.setEdad(edad.getText().isBlank() ? 0 : Integer.parseInt(edad.getText().trim()));

                boolean ok = existente == null ? maestroDAO.registrar(m) : maestroDAO.actualizar(m);
                if (ok) { dialog.close(); alGuardar.run(); }
                else avisar("No se pudo guardar el profesor.");
            } catch (NumberFormatException ex) {
                avisar("La edad debe ser numérica.");
            }
        });
        grid.addRow(r, guardar);

        Scene scene = new Scene(grid, 380, 440);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }

    // ================================================================ CURSOS

    @FXML
    private void showCursos() {
        marcarActivo(btnCursos);
        screenTitleLabel.setText("Course Management");

        VBox root = new VBox(16);
        HBox headerRow = new HBox();
        Label title = new Label("Course Management");
        title.getStyleClass().add("section-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button agregar = new Button("+ Add New Course");
        agregar.getStyleClass().add("btn-primary");
        headerRow.getChildren().addAll(title, spacer, agregar);

        TableView<Curso> tabla = new TableView<>();
        tabla.setPrefHeight(480);
        agregarColumna(tabla, "Nombre", Curso::getNombre, 220);
        agregarColumna(tabla, "Nivel", Curso::getNivel, 80);
        agregarColumna(tabla, "Profesor", c -> c.getNombreMaestro() != null ? c.getNombreMaestro() : "Sin asignar", 200);
        agregarColumna(tabla, "Inicio", c -> String.valueOf(c.getfDeInicio()), 110);
        agregarColumna(tabla, "Fin", c -> String.valueOf(c.getfDeFinalizacion()), 110);
        tabla.getItems().addAll(cursoDAO.getAllCourses());

        agregar.setOnAction(e -> mostrarDialogoCurso(null, () -> refrescarTabla(tabla, cursoDAO.getAllCourses())));

        HBox botones = new HBox(10);
        Button editar = new Button("Editar");
        editar.getStyleClass().add("btn-secondary");
        editar.setOnAction(e -> {
            Curso sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { avisar("Selecciona un curso de la tabla."); return; }
            mostrarDialogoCurso(sel, () -> refrescarTabla(tabla, cursoDAO.getAllCourses()));
        });
        Button eliminar = new Button("Eliminar");
        eliminar.getStyleClass().add("btn-danger");
        eliminar.setOnAction(e -> {
            Curso sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { avisar("Selecciona un curso de la tabla."); return; }
            if (confirmar("¿Eliminar el curso " + sel.getNombre() + "? Se eliminarán también sus materiales, tareas e inscripciones.")) {
                cursoDAO.eliminar(sel.getIdCurso());
                refrescarTabla(tabla, cursoDAO.getAllCourses());
            }
        });
        botones.getChildren().addAll(editar, eliminar);

        root.getChildren().addAll(headerRow, botones, tabla);
        setContent(root);
    }

    private void mostrarDialogoCurso(Curso existente, Runnable alGuardar) {
        Stage dialog = new Stage();
        dialog.setTitle(existente == null ? "Nuevo curso" : "Editar curso");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(24));

        TextField nombre = new TextField(existente != null ? existente.getNombre() : "");
        ComboBox<String> nivel = new ComboBox<>();
        nivel.getItems().addAll("A1", "A2", "B1", "B2", "C1", "C2");
        if (existente != null) nivel.setValue(existente.getNivel());
        TextArea descripcion = new TextArea(existente != null ? existente.getDescripcion() : "");
        descripcion.setPrefRowCount(3);

        ComboBox<Maestro> maestroCombo = new ComboBox<>();
        maestroCombo.getItems().addAll(maestroDAO.listarTodos());
        if (existente != null) {
            maestroCombo.getItems().stream()
                    .filter(m -> m.getIdMaestro() == existente.getIdMaestro())
                    .findFirst().ifPresent(maestroCombo::setValue);
        }

        DatePicker inicio = new DatePicker(existente != null && existente.getfDeInicio() != null ? existente.getfDeInicio().toLocalDate() : LocalDate.now());
        DatePicker fin = new DatePicker(existente != null && existente.getfDeFinalizacion() != null ? existente.getfDeFinalizacion().toLocalDate() : LocalDate.now().plusMonths(3));

        int r = 0;
        grid.addRow(r++, new Label("Nombre"), nombre);
        grid.addRow(r++, new Label("Nivel"), nivel);
        grid.addRow(r++, new Label("Descripción"), descripcion);
        grid.addRow(r++, new Label("Profesor"), maestroCombo);
        grid.addRow(r++, new Label("Fecha inicio"), inicio);
        grid.addRow(r++, new Label("Fecha fin"), fin);

        Button guardar = new Button("Guardar");
        guardar.getStyleClass().add("btn-primary");
        guardar.setOnAction(e -> {
            Curso c = existente != null ? existente : new Curso();
            c.setNombre(nombre.getText().trim());
            c.setNivel(nivel.getValue());
            c.setDescripcion(descripcion.getText());
            c.setIdMaestro(maestroCombo.getValue() != null ? maestroCombo.getValue().getIdMaestro() : 0);
            c.setfDeInicio(Date.valueOf(inicio.getValue()));
            c.setfDeFinalizacion(Date.valueOf(fin.getValue()));

            boolean ok = existente == null ? cursoDAO.crear(c) : cursoDAO.actualizar(c);
            if (ok) { dialog.close(); alGuardar.run(); }
            else avisar("No se pudo guardar el curso.");
        });
        grid.addRow(r, guardar);

        Scene scene = new Scene(grid, 420, 460);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }

    // ================================================================ INSCRIPCIONES

    @FXML
    private void showInscripciones() {
        marcarActivo(btnInscripciones);
        screenTitleLabel.setText("Enrollment Management");

        VBox root = new VBox(16);
        Label title = new Label("Inscripciones y Estudiantes");
        title.getStyleClass().add("section-title");

        List<Curso> cursos = cursoDAO.getAllCourses();
        ComboBox<Curso> cursoCombo = new ComboBox<>();
        cursoCombo.getItems().addAll(cursos);

        TableView<DetalleCA> tabla = new TableView<>();
        tabla.setPrefHeight(420);
        TableColumn<DetalleCA, String> colAlumno = new TableColumn<>("Alumno");
        colAlumno.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombreAlumno()));
        colAlumno.setPrefWidth(260);
        TableColumn<DetalleCA, String> colCal = new TableColumn<>("Calificación");
        colCal.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getCalificacion() == null ? "Sin calificar" : String.valueOf(d.getValue().getCalificacion())));
        colCal.setPrefWidth(140);
        tabla.getColumns().addAll(colAlumno, colCal);

        ComboBox<Alumno> alumnoCombo = new ComboBox<>();
        alumnoCombo.getItems().addAll(alumnoDAO.listarTodos());
        Button inscribirBtn = new Button("Inscribir alumno");
        inscribirBtn.getStyleClass().add("btn-primary");

        Button darDeBajaBtn = new Button("Dar de baja seleccionado");
        darDeBajaBtn.getStyleClass().add("btn-danger");

        HBox inscribirBox = new HBox(10, new Label("Alumno:"), alumnoCombo, inscribirBtn);
        inscribirBox.setAlignment(Pos.CENTER_LEFT);

        Runnable refrescar = () -> {
            Curso c = cursoCombo.getValue();
            tabla.getItems().setAll(c != null ? detalleCADAO.listarPorCurso(c.getIdCurso()) : List.of());
        };

        cursoCombo.setOnAction(e -> refrescar.run());
        if (!cursos.isEmpty()) { cursoCombo.setValue(cursos.get(0)); refrescar.run(); }

        inscribirBtn.setOnAction(e -> {
            Curso c = cursoCombo.getValue();
            Alumno a = alumnoCombo.getValue();
            if (c == null || a == null) { avisar("Selecciona un curso y un alumno."); return; }
            boolean ok = detalleCADAO.inscribir(c.getIdCurso(), a.getIdAlumno());
            if (!ok) avisar("El alumno ya estaba inscrito en este curso.");
            refrescar.run();
        });

        darDeBajaBtn.setOnAction(e -> {
            Curso c = cursoCombo.getValue();
            DetalleCA sel = tabla.getSelectionModel().getSelectedItem();
            if (c == null || sel == null) { avisar("Selecciona una fila de la tabla."); return; }
            if (confirmar("¿Dar de baja a " + sel.getNombreAlumno() + " de este curso?")) {
                detalleCADAO.darDeBaja(c.getIdCurso(), sel.getIdAlumno());
                refrescar.run();
            }
        });

        HBox selectorBox = new HBox(10, new Label("Curso:"), cursoCombo);
        selectorBox.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().addAll(title, selectorBox, tabla, inscribirBox, darDeBajaBtn);
        setContent(root);
    }

    // ================================================================ REPORTES

    @FXML
    private void showReportes() {
        marcarActivo(btnReportes);
        screenTitleLabel.setText("Reporting Engine");

        VBox root = new VBox(16);
        Label title = new Label("Reporting Engine");
        title.getStyleClass().add("section-title");

        List<Curso> cursos = cursoDAO.getAllCourses();
        ComboBox<Curso> cursoCombo = new ComboBox<>();
        cursoCombo.getItems().addAll(cursos);

        Label promedioLabel = new Label("Promedio del curso: -");
        promedioLabel.getStyleClass().add("stat-card-value");

        TableView<DetalleCA> tabla = new TableView<>();
        tabla.setPrefHeight(380);
        TableColumn<DetalleCA, String> colAlumno = new TableColumn<>("Alumno");
        colAlumno.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombreAlumno()));
        colAlumno.setPrefWidth(280);
        TableColumn<DetalleCA, String> colCal = new TableColumn<>("Calificación");
        colCal.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getCalificacion() == null ? "Sin calificar" : String.valueOf(d.getValue().getCalificacion())));
        colCal.setPrefWidth(140);
        tabla.getColumns().addAll(colAlumno, colCal);

        Runnable refrescar = () -> {
            Curso c = cursoCombo.getValue();
            if (c == null) return;
            List<DetalleCA> lista = detalleCADAO.listarPorCurso(c.getIdCurso());
            tabla.getItems().setAll(lista);
            Double prom = detalleCADAO.promedioCurso(c.getIdCurso());
            promedioLabel.setText(prom != null ? String.format("Promedio del curso: %.2f", prom) : "Promedio del curso: Sin calificaciones");
        };

        cursoCombo.setOnAction(e -> refrescar.run());
        if (!cursos.isEmpty()) { cursoCombo.setValue(cursos.get(0)); refrescar.run(); }

        Button exportar = new Button("Export PDF");
        exportar.getStyleClass().add("btn-primary");
        exportar.setOnAction(e -> exportarReportePDF(cursoCombo.getValue(), tabla.getItems()));

        HBox selectorBox = new HBox(10, new Label("Curso:"), cursoCombo, exportar);
        selectorBox.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().addAll(title, selectorBox, promedioLabel, tabla);
        setContent(root);
    }

    private void exportarReportePDF(Curso curso, List<DetalleCA> registros) {
        if (curso == null) { avisar("Selecciona un curso primero."); return; }
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("reporte_" + curso.getNombre().replaceAll("\\s+", "_") + ".pdf");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        var file = fc.showSaveDialog(contentArea.getScene().getWindow());
        if (file == null) return;

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.add(new Paragraph("EduLink English Academy - Reporte de calificaciones"));
            document.add(new Paragraph("Curso: " + curso.getNombre() + " (" + curso.getNivel() + ")"));
            document.add(new Paragraph(" "));
            for (DetalleCA d : registros) {
                String linea = d.getNombreAlumno() + " - " +
                        (d.getCalificacion() == null ? "Sin calificar" : String.valueOf(d.getCalificacion()));
                document.add(new Paragraph(linea));
            }
            document.close();
            avisar("Reporte exportado correctamente.");
        } catch (Exception ex) {
            avisar("No se pudo exportar el PDF: " + ex.getMessage());
        }
    }

    // ================================================================ UTILS

    private <T> void agregarColumna(TableView<T> tabla, String titulo, java.util.function.Function<T, String> extractor, double ancho) {
        TableColumn<T, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(extractor.apply(data.getValue())));
        col.setPrefWidth(ancho);
        tabla.getColumns().add(col);
    }

    private <T> void refrescarTabla(TableView<T> tabla, List<T> datos) {
        tabla.getItems().setAll(datos);
    }

    private void avisar(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }

    private boolean confirmar(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        var res = alert.showAndWait();
        return res.isPresent() && res.get() == ButtonType.YES;
    }

    private void setContent(javafx.scene.Node node) {
        contentArea.getChildren().setAll(node);
    }
}
