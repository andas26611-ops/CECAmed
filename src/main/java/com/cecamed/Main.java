package com.cecamed;

import javafx.application.Application;
import javafx.stage.Stage;
import com.cecamed.ui.LoginView;
import com.cecamed.database.DatabaseInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inicializar base de datos
            DatabaseInitializer.initDatabase();
            logger.info("Base de datos inicializada correctamente");

            // Mostrar ventana de login
            LoginView loginView = new LoginView();
            loginView.show(primaryStage);

        } catch (Exception e) {
            logger.error("Error al iniciar la aplicación", e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
