package com.cecamed.dao;

import com.cecamed.model.Cita;
import com.cecamed.database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {
    private static final Logger logger = LoggerFactory.getLogger(CitaDAO.class);

    public void crear(Cita cita, String usuarioModificador) {
        String sql = "INSERT INTO citas (paciente_id, nombre_paciente, fecha, hora, motivo, estado, notas, modificado_por) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cita.getPacienteId());
            pstmt.setString(2, cita.getNombrePaciente());
            pstmt.setDate(3, Date.valueOf(cita.getFecha()));
            pstmt.setTime(4, Time.valueOf(cita.getHora()));
            pstmt.setString(5, cita.getMotivo());
            pstmt.setString(6, cita.getEstado());
            pstmt.setString(7, cita.getNotas());
            pstmt.setString(8, usuarioModificador);
            pstmt.executeUpdate();
            logger.info("Cita creada: " + cita.getNombrePaciente() + " - " + cita.getFecha());
        } catch (SQLException e) {
            logger.error("Error al crear cita", e);
        }
    }

    public Cita obtenerPorId(int id) {
        String sql = "SELECT * FROM citas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapeoResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener cita", e);
        }
        return null;
    }

    public List<Cita> obtenerTodas() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas ORDER BY fecha DESC, hora DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                citas.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener citas", e);
        }
        return citas;
    }

    public List<Cita> obtenerPorPaciente(int pacienteId) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE paciente_id = ? ORDER BY fecha DESC, hora DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pacienteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                citas.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener citas por paciente", e);
        }
        return citas;
    }

    public List<Cita> obtenerPorFecha(LocalDate fecha) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE fecha = ? ORDER BY hora";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(fecha));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                citas.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener citas por fecha", e);
        }
        return citas;
    }

    public List<Cita> obtenerPorEstado(String estado) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE estado = ? ORDER BY fecha DESC, hora DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                citas.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener citas por estado", e);
        }
        return citas;
    }

    public void actualizar(Cita cita, String usuarioModificador) {
        String sql = "UPDATE citas SET nombre_paciente = ?, fecha = ?, hora = ?, motivo = ?, estado = ?, notas = ?, fecha_ultima_modificacion = ?, modificado_por = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cita.getNombrePaciente());
            pstmt.setDate(2, Date.valueOf(cita.getFecha()));
            pstmt.setTime(3, Time.valueOf(cita.getHora()));
            pstmt.setString(4, cita.getMotivo());
            pstmt.setString(5, cita.getEstado());
            pstmt.setString(6, cita.getNotas());
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(8, usuarioModificador);
            pstmt.setInt(9, cita.getId());
            pstmt.executeUpdate();
            logger.info("Cita actualizada: " + cita.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar cita", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM citas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            logger.info("Cita eliminada: " + id);
        } catch (SQLException e) {
            logger.error("Error al eliminar cita", e);
        }
    }

    private Cita mapeoResultSet(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setId(rs.getInt("id"));
        cita.setPacienteId(rs.getInt("paciente_id"));
        cita.setNombrePaciente(rs.getString("nombre_paciente"));
        cita.setFecha(rs.getDate("fecha").toLocalDate());
        cita.setHora(rs.getTime("hora").toLocalTime());
        cita.setMotivo(rs.getString("motivo"));
        cita.setEstado(rs.getString("estado"));
        cita.setNotas(rs.getString("notas"));
        cita.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        if (rs.getTimestamp("fecha_ultima_modificacion") != null) {
            cita.setFechaUltimaModificacion(rs.getTimestamp("fecha_ultima_modificacion").toLocalDateTime());
        }
        cita.setModificadoPor(rs.getString("modificado_por"));
        return cita;
    }
}
