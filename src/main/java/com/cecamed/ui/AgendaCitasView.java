package com.cecamed.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import com.cecamed.model.Usuario;

public class AgendaCitasView {
    private Usuario usuarioActual;

    public AgendaCitasView(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public VBox crear() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: white;");
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        Label titulo = new Label("📅 Agenda de Citas");
        titulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label contenido = new Label("Módulo de Agenda de Citas - Médico: " + usuarioActual.getNombre());
        contenido.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");

        Button btnNuevaCita = new Button("➕ Nueva Cita");
        btnNuevaCita.setPrefWidth(150);
        btnNuevaCita.setStyle("-fx-font-size: 12; -fx-background-color: #4CAF50; -fx-text-fill: white;");

        root.getChildren().addAll(titulo, contenido, new Separator(), btnNuevaCita);
        return root;
    }
}
