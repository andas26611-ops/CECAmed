package com.cecamed.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.cecamed.model.Usuario;
import com.cecamed.dao.AuditoriaDAO;
import com.cecamed.model.Auditoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardView {
    private static final Logger logger = LoggerFactory.getLogger(DashboardView.class);
    private Usuario usuarioActual;
    private AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private BorderPane root;

    public DashboardView(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void show(Stage primaryStage) {
        primaryStage.setTitle("CECAmed - Dashboard");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);

        root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Menu lateral
        VBox menuLateral = crearMenuLateral(primaryStage);
        root.setLeft(menuLateral);

        // Panel central inicial
        root.setCenter(crearPanelBienvenida());

        // Header
        HBox header = crearHeader();
        root.setTop(header);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox crearMenuLateral(Stage stage) {
        VBox menu = new VBox();
        menu.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");
        menu.setSpacing(10);
        menu.setPrefWidth(250);

        Label titulo = new Label("CECAmed");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white;");

        Separator sep1 = new Separator();
        sep1.setStyle("-fx-border-color: #555;");

        Button btnPacientes = new Button("👥 Gestionar Pacientes");
        btnPacientes.setPrefWidth(200);
        btnPacientes.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnPacientes.setOnAction(e -> abrirGestionPacientes());

        Button btnCitas = new Button("📅 Agenda de Citas");
        btnCitas.setPrefWidth(200);
        btnCitas.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnCitas.setOnAction(e -> abrirAgendaCitas());

        Button btnHistorial = new Button("📋 Historial Médico");
        btnHistorial.setPrefWidth(200);
        btnHistorial.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnHistorial.setOnAction(e -> abrirHistorialMedico());

        Button btnExpedientes = new Button("📄 Expedientes Clínicos");
        btnExpedientes.setPrefWidth(200);
        btnExpedientes.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnExpedientes.setOnAction(e -> abrirExpedientes());

        Separator sep2 = new Separator();
        sep2.setStyle("-fx-border-color: #555;");

        Button btnAuditoria = new Button("🔍 Auditoría");
        btnAuditoria.setPrefWidth(200);
        btnAuditoria.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnAuditoria.setOnAction(e -> abrirAuditoria());

        Button btnSalir = new Button("🚪 Cerrar Sesión");
        btnSalir.setPrefWidth(200);
        btnSalir.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-text-fill: #f44336;");
        btnSalir.setOnAction(e -> cerrarSesion(stage));

        menu.getChildren().addAll(
                titulo, sep1,
                btnPacientes, btnCitas, btnHistorial, btnExpedientes,
                sep2, btnAuditoria,
                new Separator(),
                btnSalir
        );

        return menu;
    }

    private HBox crearHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #2196F3; -fx-padding: 15;");
        header.setSpacing(10);

        Label titulo = new Label("CECAmed - Sistema de Gestión de Clínica");
        titulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white;");

        Label usuario = new Label("Usuario: " + usuarioActual.getNombre() + " (" + usuarioActual.getRol() + ")");
        usuario.setStyle("-fx-font-size: 12; -fx-text-fill: white;");

        header.getChildren().addAll(titulo);
        header.setHgrow(titulo, javafx.scene.layout.Priority.ALWAYS);
        header.getChildren().add(usuario);

        return header;
    }

    private VBox crearPanelBienvenida() {
        VBox panel = new VBox();
        panel.setStyle("-fx-background-color: white;");
        panel.setSpacing(20);
        panel.setPadding(new Insets(40));

        Label bienvenida = new Label("¡Bienvenido, " + usuarioActual.getNombre() + "!");
        bienvenida.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label descripcion = new Label("Selecciona una opción del menú lateral para comenzar.");
        descripcion.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");

        Label informacion = new Label("Sistema de Gestión de Clínica CECAmed\n\nMódulos disponibles:\n• Gestión de Pacientes\n• Agenda de Citas\n• Historial Médico\n• Expedientes Clínicos\n• Auditoría");
        informacion.setStyle("-fx-font-size: 12; -fx-text-fill: #333;");
        informacion.setWrapText(true);

        panel.getChildren().addAll(bienvenida, descripcion, new Separator(), informacion);
        return panel;
    }

    private void abrirGestionPacientes() {
        GestionPacientesView vista = new GestionPacientesView(usuarioActual);
        root.setCenter(vista.crear());
    }

    private void abrirAgendaCitas() {
        AgendaCitasView vista = new AgendaCitasView(usuarioActual);
        root.setCenter(vista.crear());
    }

    private void abrirHistorialMedico() {
        HistorialMedicoView vista = new HistorialMedicoView(usuarioActual);
        root.setCenter(vista.crear());
    }

    private void abrirExpedientes() {
        ExpedientesClinicosView vista = new ExpedientesClinicosView(usuarioActual);
        root.setCenter(vista.crear());
    }

    private void abrirAuditoria() {
        AuditoriaView vista = new AuditoriaView(usuarioActual);
        root.setCenter(vista.crear());
    }

    private void cerrarSesion(Stage stage) {
        auditoriaDAO.registrar(new Auditoria(usuarioActual.getNombre(), "LOGOUT", "USUARIO", usuarioActual.getId(), "Sesión cerrada"));
        logger.info("Sesión cerrada: " + usuarioActual.getEmail());
        
        LoginView loginView = new LoginView();
        loginView.show(stage);
    }
}
