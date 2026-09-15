package com.cecamed.dao;

import com.cecamed.model.HistorialMedico;
import com.cecamed.database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistorialMedicoDAO {
    private static final Logger logger = LoggerFactory.getLogger(HistorialMedicoDAO.class);

    public void crear(HistorialMedico historial, String usuarioModificador) {
        String sql = "INSERT INTO historial_medico (paciente_id, fecha_consulta, diagnostico, tratamiento, medicamentos, observaciones, medico, modificado_por) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, historial.getPacienteId());
            pstmt.setTimestamp(2, Timestamp.valueOf(historial.getFechaConsulta()));
            pstmt.setString(3, historial.getDiagnostico());
            pstmt.setString(4, historial.getTratamiento());
            pstmt.setString(5, historial.getMedicamentos());
            pstmt.setString(6, historial.getObservaciones());
            pstmt.setString(7, historial.getMedico());
            pstmt.setString(8, usuarioModificador);
            pstmt.executeUpdate();
            logger.info("Historial médico creado para paciente: " + historial.getPacienteId());
        } catch (SQLException e) {
            logger.error("Error al crear historial médico", e);
        }
    }

    public HistorialMedico obtenerPorId(int id) {
        String sql = "SELECT * FROM historial_medico WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapeoResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener historial médico", e);
        }
        return null;
    }

    public List<HistorialMedico> obtenerPorPaciente(int pacienteId) {
        List<HistorialMedico> historiales = new ArrayList<>();
        String sql = "SELECT * FROM historial_medico WHERE paciente_id = ? ORDER BY fecha_consulta DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pacienteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                historiales.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener historial médico por paciente", e);
        }
        return historiales;
    }

    public List<HistorialMedico> obtenerTodos() {
        List<HistorialMedico> historiales = new ArrayList<>();
        String sql = "SELECT * FROM historial_medico ORDER BY fecha_consulta DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                historiales.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener historiales médicos", e);
        }
        return historiales;
    }

    public void actualizar(HistorialMedico historial, String usuarioModificador) {
        String sql = "UPDATE historial_medico SET diagnostico = ?, tratamiento = ?, medicamentos = ?, observaciones = ?, medico = ?, fecha_ultima_modificacion = ?, modificado_por = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, historial.getDiagnostico());
            pstmt.setString(2, historial.getTratamiento());
            pstmt.setString(3, historial.getMedicamentos());
            pstmt.setString(4, historial.getObservaciones());
            pstmt.setString(5, historial.getMedico());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(7, usuarioModificador);
            pstmt.setInt(8, historial.getId());
            pstmt.executeUpdate();
            logger.info("Historial médico actualizado: " + historial.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar historial médico", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM historial_medico WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            logger.info("Historial médico eliminado: " + id);
        } catch (SQLException e) {
            logger.error("Error al eliminar historial médico", e);
        }
    }

    private HistorialMedico mapeoResultSet(ResultSet rs) throws SQLException {
        HistorialMedico historial = new HistorialMedico();
        historial.setId(rs.getInt("id"));
        historial.setPacienteId(rs.getInt("paciente_id"));
        if (rs.getTimestamp("fecha_consulta") != null) {
            historial.setFechaConsulta(rs.getTimestamp("fecha_consulta").toLocalDateTime());
        }
        historial.setDiagnostico(rs.getString("diagnostico"));
        historial.setTratamiento(rs.getString("tratamiento"));
        historial.setMedicamentos(rs.getString("medicamentos"));
        historial.setObservaciones(rs.getString("observaciones"));
        historial.setMedico(rs.getString("medico"));
        historial.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        if (rs.getTimestamp("fecha_ultima_modificacion") != null) {
            historial.setFechaUltimaModificacion(rs.getTimestamp("fecha_ultima_modificacion").toLocalDateTime());
        }
        historial.setModificadoPor(rs.getString("modificado_por"));
        return historial;
    }
}
