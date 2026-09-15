package com.cecamed.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import com.cecamed.model.Usuario;

public class ExpedientesClinicosView {
    private Usuario usuarioActual;

    public ExpedientesClinicosView(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public VBox crear() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: white;");
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        Label titulo = new Label("📄 Expedientes Clínicos");
        titulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label contenido = new Label("Módulo de Expedientes Clínicos - Médico: " + usuarioActual.getNombre());
        contenido.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");

        Button btnConsultarExpediente = new Button("🔍 Consultar Expediente");
        btnConsultarExpediente.setPrefWidth(150);
        btnConsultarExpediente.setStyle("-fx-font-size: 12; -fx-background-color: #2196F3; -fx-text-fill: white;");

        root.getChildren().addAll(titulo, contenido, new Separator(), btnConsultarExpediente);
        return root;
    }
}
