package com.cecamed.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.cecamed.model.Usuario;
import com.cecamed.model.Auditoria;
import com.cecamed.dao.AuditoriaDAO;
import java.util.List;

public class AuditoriaView {
    private Usuario usuarioActual;
    private AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private TableView<Auditoria> tablaAuditoria;

    public AuditoriaView(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public VBox crear() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: white;");
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        Label titulo = new Label("🔍 Auditoría del Sistema");
        titulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        // Tabla de auditoría
        tablaAuditoria = crearTablaAuditoria();
        actualizarTablaAuditoria();

        // Panel de filtros
        HBox panelFiltros = crearPanelFiltros();

        root.getChildren().addAll(titulo, new Separator(), panelFiltros, tablaAuditoria);
        return root;
    }

    private HBox crearPanelFiltros() {
        HBox panel = new HBox();
        panel.setSpacing(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-padding: 10;");

        Label label = new Label("Filtrar por:");
        
        ComboBox<String> filtroCombo = new ComboBox<>();
        filtroCombo.getItems().addAll("Todos", "LOGIN", "LOGOUT", "CREAR", "MODIFICAR", "ELIMINAR");
        filtroCombo.setValue("Todos");
        filtroCombo.setPrefWidth(150);

        Button btnFiltrar = new Button("🔍 Filtrar");
        btnFiltrar.setOnAction(e -> {
            String seleccion = filtroCombo.getValue();
            if ("Todos".equals(seleccion)) {
                actualizarTablaAuditoria();
            } else {
                List<Auditoria> resultados = auditoriaDAO.obtenerPorAccion(seleccion);
                ObservableList<Auditoria> items = FXCollections.observableArrayList(resultados);
                tablaAuditoria.setItems(items);
            }
        });

        Button btnRefrescar = new Button("🔄 Refrescar");
        btnRefrescar.setOnAction(e -> actualizarTablaAuditoria());

        panel.getChildren().addAll(label, filtroCombo, btnFiltrar, btnRefrescar);
        return panel;
    }

    private TableView<Auditoria> crearTablaAuditoria() {
        TableView<Auditoria> tabla = new TableView<>();
        tabla.setPrefHeight(400);

        TableColumn<Auditoria, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colId.setPrefWidth(50);

        TableColumn<Auditoria, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsuario()));
        colUsuario.setPrefWidth(120);

        TableColumn<Auditoria, String> colAccion = new TableColumn<>("Acción");
        colAccion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAccion()));
        colAccion.setPrefWidth(100);

        TableColumn<Auditoria, String> colEntidad = new TableColumn<>("Entidad");
        colEntidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEntidad()));
        colEntidad.setPrefWidth(100);

        TableColumn<Auditoria, String> colDetalles = new TableColumn<>("Detalles");
        colDetalles.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDetalles() != null ? cellData.getValue().getDetalles() : ""));
        colDetalles.setPrefWidth(200);

        TableColumn<Auditoria, String> colFecha = new TableColumn<>("Fecha/Hora");
        colFecha.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaHora().toString()));
        colFecha.setPrefWidth(150);

        tabla.getColumns().addAll(colId, colUsuario, colAccion, colEntidad, colDetalles, colFecha);
        return tabla;
    }

    private void actualizarTablaAuditoria() {
        List<Auditoria> auditorias = auditoriaDAO.obtenerTodas();
        ObservableList<Auditoria> items = FXCollections.observableArrayList(auditorias);
        tablaAuditoria.setItems(items);
    }
}
