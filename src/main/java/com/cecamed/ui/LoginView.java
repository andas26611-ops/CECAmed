package com.cecamed.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.cecamed.dao.UsuarioDAO;
import com.cecamed.model.Usuario;
import com.cecamed.dao.AuditoriaDAO;
import com.cecamed.model.Auditoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginView {
    private static final Logger logger = LoggerFactory.getLogger(LoginView.class);
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private AuditoriaDAO auditoriaDAO = new AuditoriaDAO();

    public void show(Stage primaryStage) {
        primaryStage.setTitle("CECAmed - Login");
        primaryStage.setWidth(500);
        primaryStage.setHeight(400);

        VBox root = new VBox();
        root.setStyle("-fx-background-color: #f0f0f0;");
        root.setSpacing(20);
        root.setPadding(new Insets(40));

        // Título
        Label titulo = new Label("CECAmed");
        titulo.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label subtitulo = new Label("Sistema de Gestión de Clínica");
        subtitulo.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");

        // Email
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("admin@cecamed.com");
        emailField.setPrefHeight(40);

        // Contraseña
        Label passwordLabel = new Label("Contraseña:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("admin123");
        passwordField.setPrefHeight(40);

        // Botones
        HBox botonesBox = new HBox();
        botonesBox.setSpacing(10);

        Button loginBtn = new Button("Iniciar Sesión");
        loginBtn.setPrefWidth(200);
        loginBtn.setPrefHeight(40);
        loginBtn.setStyle("-fx-font-size: 14; -fx-background-color: #2196F3; -fx-text-fill: white;");
        loginBtn.setOnAction(e -> autenticar(emailField.getText(), passwordField.getText(), primaryStage));

        Button registroBtn = new Button("Registrarse");
        registroBtn.setPrefWidth(200);
        registroBtn.setPrefHeight(40);
        registroBtn.setStyle("-fx-font-size: 14; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        registroBtn.setOnAction(e -> mostrarRegistro(primaryStage));

        botonesBox.getChildren().addAll(loginBtn, registroBtn);

        // Mensaje de error
        Label mensajeError = new Label();
        mensajeError.setStyle("-fx-text-fill: #f44336;");

        root.getChildren().addAll(
                titulo, subtitulo,
                new Separator(),
                emailLabel, emailField,
                passwordLabel, passwordField,
                botonesBox,
                mensajeError
        );

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void autenticar(String email, String contrasena, Stage stage) {
        if (email.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Por favor complete todos los campos");
            return;
        }

        Usuario usuario = usuarioDAO.obtenerPorEmail(email);
        if (usuario == null) {
            mostrarError("Usuario o contraseña incorrectos");
            logger.warn("Intento de login fallido: " + email);
            return;
        }

        if (!usuario.verificarContrasena(contrasena)) {
            mostrarError("Usuario o contraseña incorrectos");
            logger.warn("Intento de login fallido: " + email);
            return;
        }

        if (!usuario.isActivo()) {
            mostrarError("Usuario inactivo");
            return;
        }

        // Login exitoso
        logger.info("Login exitoso: " + email);
        auditoriaDAO.registrar(new Auditoria(usuario.getNombre(), "LOGIN", "USUARIO", usuario.getId(), "Login exitoso"));

        // Abrir dashboard
        DashboardView dashboard = new DashboardView(usuario);
        dashboard.show(stage);
    }

    private void mostrarRegistro(Stage stage) {
        Stage registroStage = new Stage();
        registroStage.setTitle("Registro de Nuevo Usuario");
        registroStage.setWidth(500);
        registroStage.setHeight(500);

        VBox root = new VBox();
        root.setStyle("-fx-background-color: #f0f0f0;");
        root.setSpacing(15);
        root.setPadding(new Insets(30));

        Label titulo = new Label("Registro de Usuario");
        titulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre completo");
        nombreField.setPrefHeight(35);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefHeight(35);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        passwordField.setPrefHeight(35);

        ComboBox<String> rolCombo = new ComboBox<>();
        rolCombo.getItems().addAll("MEDICO", "ADMINISTRADOR");
        rolCombo.setValue("MEDICO");
        rolCombo.setPrefHeight(35);

        Button registrarBtn = new Button("Registrar");
        registrarBtn.setPrefWidth(150);
        registrarBtn.setPrefHeight(35);
        registrarBtn.setStyle("-fx-font-size: 14; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        registrarBtn.setOnAction(e -> {
            if (nombreField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                mostrarError("Complete todos los campos");
                return;
            }

            if (usuarioDAO.obtenerPorEmail(emailField.getText()) != null) {
                mostrarError("El email ya está registrado");
                return;
            }

            Usuario nuevoUsuario = new Usuario(nombreField.getText(), emailField.getText(), passwordField.getText(), rolCombo.getValue());
            usuarioDAO.crear(nuevoUsuario, "SISTEMA");
            auditoriaDAO.registrar(new Auditoria("SISTEMA", "CREAR", "USUARIO", 0, "Nuevo usuario registrado: " + emailField.getText()));
            mostrarExito("Usuario registrado correctamente");
            registroStage.close();
        });

        Button cancelarBtn = new Button("Cancelar");
        cancelarBtn.setPrefWidth(150);
        cancelarBtn.setPrefHeight(35);
        cancelarBtn.setStyle("-fx-font-size: 14; -fx-background-color: #f44336; -fx-text-fill: white;");
        cancelarBtn.setOnAction(e -> registroStage.close());

        HBox botonesBox = new HBox();
        botonesBox.setSpacing(10);
        botonesBox.getChildren().addAll(registrarBtn, cancelarBtn);

        root.getChildren().addAll(
                titulo,
                new Label("Nombre:"), nombreField,
                new Label("Email:"), emailField,
                new Label("Contraseña:"), passwordField,
                new Label("Rol:"), rolCombo,
                botonesBox
        );

        Scene scene = new Scene(root);
        registroStage.setScene(scene);
        registroStage.show();
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
