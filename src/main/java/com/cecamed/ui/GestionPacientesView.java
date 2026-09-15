package com.cecamed.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.cecamed.model.Usuario;
import com.cecamed.model.Paciente;
import com.cecamed.dao.PacienteDAO;
import com.cecamed.dao.AuditoriaDAO;
import com.cecamed.model.Auditoria;
import java.time.LocalDate;
import java.util.List;

public class GestionPacientesView {
    private Usuario usuarioActual;
    private PacienteDAO pacienteDAO = new PacienteDAO();
    private AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private TableView<Paciente> tablaPacientes;

    public GestionPacientesView(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public VBox crear() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: white;");
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        // Título
        Label titulo = new Label("👥 Gestión de Pacientes");
        titulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        // Panel de búsqueda
        HBox panelBusqueda = crearPanelBusqueda();

        // Tabla de pacientes
        tablaPacientes = crearTablaPacientes();
        actualizarTablaPacientes();

        // Panel de botones
        HBox panelBotones = crearPanelBotones();

        root.getChildren().addAll(titulo, panelBusqueda, new Separator(), tablaPacientes, panelBotones);
        return root;
    }

    private HBox crearPanelBusqueda() {
        HBox panel = new HBox();
        panel.setSpacing(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-padding: 10;");

        Label label = new Label("Buscar:");
        TextField busquedaField = new TextField();
        busquedaField.setPromptText("Nombre, apellido o cédula...");
        busquedaField.setPrefWidth(300);

        Button btnBuscar = new Button("🔍 Buscar");
        btnBuscar.setOnAction(e -> {
            String criterio = busquedaField.getText();
            if (criterio.isEmpty()) {
                actualizarTablaPacientes();
            } else {
                List<Paciente> resultados = pacienteDAO.buscar(criterio);
                ObservableList<Paciente> items = FXCollections.observableArrayList(resultados);
                tablaPacientes.setItems(items);
            }
        });

        Button btnLimpiar = new Button("🗑 Limpiar");
        btnLimpiar.setOnAction(e -> {
            busquedaField.clear();
            actualizarTablaPacientes();
        });

        panel.getChildren().addAll(label, busquedaField, btnBuscar, btnLimpiar);
        return panel;
    }

    private TableView<Paciente> crearTablaPacientes() {
        TableView<Paciente> tabla = new TableView<>();
        tabla.setPrefHeight(400);

        TableColumn<Paciente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colId.setPrefWidth(50);

        TableColumn<Paciente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colNombre.setPrefWidth(120);

        TableColumn<Paciente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));
        colApellido.setPrefWidth(120);

        TableColumn<Paciente, String> colCedula = new TableColumn<>("Cédula");
        colCedula.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCedula()));
        colCedula.setPrefWidth(120);

        TableColumn<Paciente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelefonoContacto() != null ? cellData.getValue().getTelefonoContacto() : ""));
        colTelefono.setPrefWidth(120);

        tabla.getColumns().addAll(colId, colNombre, colApellido, colCedula, colTelefono);
        return tabla;
    }

    private HBox crearPanelBotones() {
        HBox panel = new HBox();
        panel.setSpacing(10);
        panel.setPadding(new Insets(10));

        Button btnNuevo = new Button("➕ Nuevo Paciente");
        btnNuevo.setPrefWidth(150);
        btnNuevo.setStyle("-fx-font-size: 12; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnNuevo.setOnAction(e -> abrirFormularioNuevoPaciente());

        Button btnEditar = new Button("✍ Editar");
        btnEditar.setPrefWidth(150);
        btnEditar.setStyle("-fx-font-size: 12; -fx-background-color: #2196F3; -fx-text-fill: white;");
        btnEditar.setOnAction(e -> abrirFormularioEditarPaciente());

        Button btnEliminar = new Button("🗑 Eliminar");
        btnEliminar.setPrefWidth(150);
        btnEliminar.setStyle("-fx-font-size: 12; -fx-background-color: #f44336; -fx-text-fill: white;");
        btnEliminar.setOnAction(e -> eliminarPaciente());

        Button btnVer = new Button("🔍 Ver Detalles");
        btnVer.setPrefWidth(150);
        btnVer.setStyle("-fx-font-size: 12; -fx-background-color: #FF9800; -fx-text-fill: white;");
        btnVer.setOnAction(e -> verDetallesPaciente());

        panel.getChildren().addAll(btnNuevo, btnEditar, btnEliminar, btnVer);
        return panel;
    }

    private void actualizarTablaPacientes() {
        List<Paciente> pacientes = pacienteDAO.obtenerTodos();
        ObservableList<Paciente> items = FXCollections.observableArrayList(pacientes);
        tablaPacientes.setItems(items);
    }

    private void abrirFormularioNuevoPaciente() {
        Stage stage = new Stage();
        stage.setTitle("Nuevo Paciente");
        stage.setWidth(600);
        stage.setHeight(700);

        VBox root = new VBox();
        root.setStyle("-fx-background-color: white;");
        root.setSpacing(10);
        root.setPadding(new Insets(20));

        Label titulo = new Label("Registrar Nuevo Paciente");
        titulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");
        nombreField.setPrefHeight(35);

        TextField apellidoField = new TextField();
        apellidoField.setPromptText("Apellido");
        apellidoField.setPrefHeight(35);

        TextField cedulaField = new TextField();
        cedulaField.setPromptText("Cédula");
        cedulaField.setPrefHeight(35);

        DatePicker fechaNacimientoField = new DatePicker();
        fechaNacimientoField.setPromptText("Fecha de Nacimiento");

        ComboBox<String> generoCombo = new ComboBox<>();
        generoCombo.getItems().addAll("M", "F");
        generoCombo.setPromptText("Género");

        TextField telefonoField = new TextField();
        telefonoField.setPromptText("Teléfono");
        telefonoField.setPrefHeight(35);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefHeight(35);

        TextField direccionField = new TextField();
        direccionField.setPromptText("Dirección");
        direccionField.setPrefHeight(35);

        TextField ciudadField = new TextField();
        ciudadField.setPromptText("Ciudad");
        ciudadField.setPrefHeight(35);

        TextArea alergiasArea = new TextArea();
        alergiasArea.setPromptText("Alergias");
        alergiasArea.setPrefHeight(60);
        alergiasArea.setWrapText(true);

        TextArea antecedentesArea = new TextArea();
        antecedentesArea.setPromptText("Antecedentes Patológicos");
        antecedentesArea.setPrefHeight(60);
        antecedentesArea.setWrapText(true);

        Button btnGuardar = new Button("💾 Guardar");
        btnGuardar.setPrefWidth(150);
        btnGuardar.setStyle("-fx-font-size: 12; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnGuardar.setOnAction(e -> {
            if (nombreField.getText().isEmpty() || apellidoField.getText().isEmpty() || cedulaField.getText().isEmpty()) {
                mostrarError("Complete los campos obligatorios");
                return;
            }

            Paciente paciente = new Paciente(
                    nombreField.getText(),
                    apellidoField.getText(),
                    cedulaField.getText(),
                    fechaNacimientoField.getValue()
            );
            paciente.setGenero(generoCombo.getValue());
            paciente.setTelefonoContacto(telefonoField.getText());
            paciente.setEmail(emailField.getText());
            paciente.setDireccion(direccionField.getText());
            paciente.setCiudad(ciudadField.getText());
            paciente.setAlergias(alergiasArea.getText());
            paciente.setAntecedentesPatologicos(antecedentesArea.getText());

            pacienteDAO.crear(paciente, usuarioActual.getNombre());
            auditoriaDAO.registrar(new Auditoria(usuarioActual.getNombre(), "CREAR", "PACIENTE", 0, "Paciente registrado: " + cedulaField.getText()));
            mostrarExito("Paciente registrado correctamente");
            actualizarTablaPacientes();
            stage.close();
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setPrefWidth(150);
        btnCancelar.setStyle("-fx-font-size: 12; -fx-background-color: #f44336; -fx-text-fill: white;");
        btnCancelar.setOnAction(e -> stage.close());

        HBox botonesBox = new HBox();
        botonesBox.setSpacing(10);
        botonesBox.getChildren().addAll(btnGuardar, btnCancelar);

        root.getChildren().addAll(
                titulo,
                new Label("Nombre:"), nombreField,
                new Label("Apellido:"), apellidoField,
                new Label("Cédula:"), cedulaField,
                new Label("Fecha de Nacimiento:"), fechaNacimientoField,
                new Label("Género:"), generoCombo,
                new Label("Teléfono:"), telefonoField,
                new Label("Email:"), emailField,
                new Label("Dirección:"), direccionField,
                new Label("Ciudad:"), ciudadField,
                new Label("Alergias:"), alergiasArea,
                new Label("Antecedentes Patológicos:"), antecedentesArea,
                botonesBox
        );

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        Scene scene = new Scene(scroll);
        stage.setScene(scene);
        stage.show();
    }

    private void abrirFormularioEditarPaciente() {
        Paciente paciente = tablaPacientes.getSelectionModel().getSelectedItem();
        if (paciente == null) {
            mostrarError("Seleccione un paciente");
            return;
        }
        mostrarExito("Editar paciente: " + paciente.getNombre());
    }

    private void eliminarPaciente() {
        Paciente paciente = tablaPacientes.getSelectionModel().getSelectedItem();
        if (paciente == null) {
            mostrarError("Seleccione un paciente");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setContentText("¿Está seguro de eliminar el paciente " + paciente.getNombre() + "?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            pacienteDAO.eliminar(paciente.getId());
            auditoriaDAO.registrar(new Auditoria(usuarioActual.getNombre(), "ELIMINAR", "PACIENTE", paciente.getId(), "Paciente eliminado: " + paciente.getCedula()));
            mostrarExito("Paciente eliminado");
            actualizarTablaPacientes();
        }
    }

    private void verDetallesPaciente() {
        Paciente paciente = tablaPacientes.getSelectionModel().getSelectedItem();
        if (paciente == null) {
            mostrarError("Seleccione un paciente");
            return;
        }
        mostrarExito("Paciente: " + paciente.toString());
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
