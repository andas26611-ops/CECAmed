package com.cecamed.dao;

import com.cecamed.model.Auditoria;
import com.cecamed.database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAO {
    private static final Logger logger = LoggerFactory.getLogger(AuditoriaDAO.class);

    public void registrar(Auditoria auditoria) {
        String sql = "INSERT INTO auditoria (usuario, accion, entidad, id_entidad, detalles) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, auditoria.getUsuario());
            pstmt.setString(2, auditoria.getAccion());
            pstmt.setString(3, auditoria.getEntidad());
            pstmt.setInt(4, auditoria.getIdEntidad());
            pstmt.setString(5, auditoria.getDetalles());
            pstmt.executeUpdate();
            logger.info("Auditoría registrada: " + auditoria.getAccion() + " - " + auditoria.getEntidad());
        } catch (SQLException e) {
            logger.error("Error al registrar auditoría", e);
        }
    }

    public List<Auditoria> obtenerTodas() {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT * FROM auditoria ORDER BY fecha_hora DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                auditorias.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener auditorías", e);
        }
        return auditorias;
    }

    public List<Auditoria> obtenerPorUsuario(String usuario) {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT * FROM auditoria WHERE usuario = ? ORDER BY fecha_hora DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                auditorias.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener auditorías por usuario", e);
        }
        return auditorias;
    }

    public List<Auditoria> obtenerPorEntidad(String entidad) {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT * FROM auditoria WHERE entidad = ? ORDER BY fecha_hora DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entidad);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                auditorias.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener auditorías por entidad", e);
        }
        return auditorias;
    }

    public List<Auditoria> obtenerPorAccion(String accion) {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT * FROM auditoria WHERE accion = ? ORDER BY fecha_hora DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accion);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                auditorias.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener auditorías por acción", e);
        }
        return auditorias;
    }

    private Auditoria mapeoResultSet(ResultSet rs) throws SQLException {
        Auditoria auditoria = new Auditoria();
        auditoria.setId(rs.getInt("id"));
        auditoria.setUsuario(rs.getString("usuario"));
        auditoria.setAccion(rs.getString("accion"));
        auditoria.setEntidad(rs.getString("entidad"));
        auditoria.setIdEntidad(rs.getInt("id_entidad"));
        auditoria.setDetalles(rs.getString("detalles"));
        auditoria.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        return auditoria;
    }
}
