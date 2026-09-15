package com.cecamed.dao;

import com.cecamed.model.Usuario;
import com.cecamed.database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

    public void crear(Usuario usuario, String usuarioModificador) {
        String sql = "INSERT INTO usuarios (nombre, email, contrasena, rol, modificado_por) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setString(4, usuario.getRol());
            pstmt.setString(5, usuarioModificador);
            pstmt.executeUpdate();
            logger.info("Usuario creado: " + usuario.getEmail());
        } catch (SQLException e) {
            logger.error("Error al crear usuario", e);
        }
    }

    public Usuario obtenerPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapeoResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener usuario", e);
        }
        return null;
    }

    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapeoResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener usuario", e);
        }
        return null;
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapeoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener usuarios", e);
        }
        return usuarios;
    }

    public void actualizar(Usuario usuario, String usuarioModificador) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, rol = ?, activo = ?, fecha_ultima_modificacion = ?, modificado_por = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getRol());
            pstmt.setBoolean(4, usuario.isActivo());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(6, usuarioModificador);
            pstmt.setInt(7, usuario.getId());
            pstmt.executeUpdate();
            logger.info("Usuario actualizado: " + usuario.getEmail());
        } catch (SQLException e) {
            logger.error("Error al actualizar usuario", e);
        }
    }

    public void eliminar(int id) {
        String sql = "UPDATE usuarios SET activo = 0 WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            logger.info("Usuario eliminado (lógicamente): " + id);
        } catch (SQLException e) {
            logger.error("Error al eliminar usuario", e);
        }
    }

    private Usuario mapeoResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setEmail(rs.getString("email"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setRol(rs.getString("rol"));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        if (rs.getTimestamp("fecha_ultima_modificacion") != null) {
            usuario.setFechaUltimaModificacion(rs.getTimestamp("fecha_ultima_modificacion").toLocalDateTime());
        }
        usuario.setModificadoPor(rs.getString("modificado_por"));
        return usuario;
    }
}
