package com.cecamed.dao;

import com.cecamed.model.Paciente;
import com.cecamed.database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {
    private static final Logger logger = LoggerFactory.getLogger(PacienteDAO.class);

    public void crear(Paciente paciente, String usuarioModificador) {
        String sql = "INSERT INTO pacientes (nombre, apellido, cedula, fecha_nacimiento, genero, telefono_contacto, email, direccion, ciudad, alergias, antecedentes_patologicos, modificado_por) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, paciente.getNombre());
            pstmt.setString(2, paciente.getApellido());
            pstmt.setString(3, paciente.getCedula());
            pstmt.setDate(4, paciente.getFechaNacimiento() != null ? Date.valueOf(paciente.getFechaNacimiento()) : null);
            pstmt.setString(5, paciente.getGenero());
            pstmt.setString(6, paciente.getTelefonoContacto());
            pstmt.setString(7, paciente.getEmail());
            pstmt.setString(8, paciente.getDireccion());
            pstmt.setString(9, paciente.getCiudad());
            pstmt.setString(10, paciente.getAlergias());
            pstmt.setString(11, paciente.getAntecedentesPatologicos());
            pstmt.setString(12, usuarioModificador);
            pstmt.executeUpdate();
            logger.info("Paciente creado: " + paciente.getCedula());
        } catch (SQLException e) {
            logger.error("Error al crear paciente", e);
        }
    }

    public Paciente obtenerPorId(int id) {
        String sql = "SELECT * FROM pacientes WHERE id = ? AND activo = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapeoResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener paciente", e);
        }
        return null;
    }

    public Paciente obtenerPorCedula(String cedula) {
        String sql = "SELECT * FROM pacientes WHERE cedula = ? AND activo = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cedula);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapeoResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener paciente", e);
        }
        return null;
    }

    public List<Paciente> obtenerTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes WHERE activo = 1 ORDER BY apellido, nombre";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pacientes.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener pacientes", e);
        }
        return pacientes;
    }

    public List<Paciente> buscar(String criterio) {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes WHERE activo = 1 AND (nombre LIKE ? OR apellido LIKE ? OR cedula LIKE ?) ORDER BY apellido, nombre";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String busqueda = "%" + criterio + "%";
            pstmt.setString(1, busqueda);
            pstmt.setString(2, busqueda);
            pstmt.setString(3, busqueda);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pacientes.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al buscar pacientes", e);
        }
        return pacientes;
    }

    public void actualizar(Paciente paciente, String usuarioModificador) {
        String sql = "UPDATE pacientes SET nombre = ?, apellido = ?, fecha_nacimiento = ?, genero = ?, telefono_contacto = ?, email = ?, direccion = ?, ciudad = ?, alergias = ?, antecedentes_patologicos = ?, fecha_ultima_modificacion = ?, modificado_por = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, paciente.getNombre());
            pstmt.setString(2, paciente.getApellido());
            pstmt.setDate(3, paciente.getFechaNacimiento() != null ? Date.valueOf(paciente.getFechaNacimiento()) : null);
            pstmt.setString(4, paciente.getGenero());
            pstmt.setString(5, paciente.getTelefonoContacto());
            pstmt.setString(6, paciente.getEmail());
            pstmt.setString(7, paciente.getDireccion());
            pstmt.setString(8, paciente.getCiudad());
            pstmt.setString(9, paciente.getAlergias());
            pstmt.setString(10, paciente.getAntecedentesPatologicos());
            pstmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(12, usuarioModificador);
            pstmt.setInt(13, paciente.getId());
            pstmt.executeUpdate();
            logger.info("Paciente actualizado: " + paciente.getCedula());
        } catch (SQLException e) {
            logger.error("Error al actualizar paciente", e);
        }
    }

    public void eliminar(int id) {
        String sql = "UPDATE pacientes SET activo = 0 WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            logger.info("Paciente eliminado (lógicamente): " + id);
        } catch (SQLException e) {
            logger.error("Error al eliminar paciente", e);
        }
    }

    private Paciente mapeoResultSet(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getInt("id"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellido(rs.getString("apellido"));
        paciente.setCedula(rs.getString("cedula"));
        paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento") != null ? rs.getDate("fecha_nacimiento").toLocalDate() : null);
        paciente.setGenero(rs.getString("genero"));
        paciente.setTelefonoContacto(rs.getString("telefono_contacto"));
        paciente.setEmail(rs.getString("email"));
        paciente.setDireccion(rs.getString("direccion"));
        paciente.setCiudad(rs.getString("ciudad"));
        paciente.setAlergias(rs.getString("alergias"));
        paciente.setAntecedentesPatologicos(rs.getString("antecedentes_patologicos"));
        paciente.setActivo(rs.getBoolean("activo"));
        paciente.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        if (rs.getTimestamp("fecha_ultima_modificacion") != null) {
            paciente.setFechaUltimaModificacion(rs.getTimestamp("fecha_ultima_modificacion").toLocalDateTime());
        }
        paciente.setModificadoPor(rs.getString("modificado_por"));
        return paciente;
    }
}
