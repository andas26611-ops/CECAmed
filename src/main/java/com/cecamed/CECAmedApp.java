package com.cecamed;

import javafx.application.Application;
import javafx.stage.Stage;
import com.cecamed.ui.LoginView;
import com.cecamed.database.DatabaseInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CECAmedApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(CECAmedApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Iniciando aplicación CECAmed");
            
            // Inicializar base de datos
            DatabaseInitializer.initDatabase();
            
            // Mostrar login
            LoginView loginView = new LoginView();
            loginView.show(primaryStage);
            
        } catch (Exception e) {
            logger.error("Error al iniciar la aplicación", e);
        }
    }

    @Override
    public void stop() {
        logger.info("Cerrando aplicación CECAmed");
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
