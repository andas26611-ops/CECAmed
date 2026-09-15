package com.cecamed.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static void initDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Tabla de Usuarios
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL,"
                    + "email TEXT UNIQUE NOT NULL,"
                    + "contrasena TEXT NOT NULL,"
                    + "rol TEXT NOT NULL,"
                    + "activo BOOLEAN DEFAULT 1,"
                    + "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "fecha_ultima_modificacion TIMESTAMP,"
                    + "modificado_por TEXT"
                    + ")");

            // Tabla de Pacientes
            stmt.execute("CREATE TABLE IF NOT EXISTS pacientes ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL,"
                    + "apellido TEXT NOT NULL,"
                    + "cedula TEXT UNIQUE NOT NULL,"
                    + "fecha_nacimiento DATE,"
                    + "genero TEXT,"
                    + "telefono_contacto TEXT,"
                    + "email TEXT,"
                    + "direccion TEXT,"
                    + "ciudad TEXT,"
                    + "alergias TEXT,"
                    + "antecedentes_patologicos TEXT,"
                    + "activo BOOLEAN DEFAULT 1,"
                    + "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "fecha_ultima_modificacion TIMESTAMP,"
                    + "modificado_por TEXT"
                    + ")");

            // Tabla de Citas
            stmt.execute("CREATE TABLE IF NOT EXISTS citas ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "paciente_id INTEGER NOT NULL,"
                    + "nombre_paciente TEXT NOT NULL,"
                    + "fecha DATE NOT NULL,"
                    + "hora TIME NOT NULL,"
                    + "motivo TEXT NOT NULL,"
                    + "estado TEXT DEFAULT 'PENDIENTE',"
                    + "notas TEXT,"
                    + "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "fecha_ultima_modificacion TIMESTAMP,"
                    + "modificado_por TEXT,"
                    + "FOREIGN KEY (paciente_id) REFERENCES pacientes(id)"
                    + ")");

            // Tabla de Historial Médico
            stmt.execute("CREATE TABLE IF NOT EXISTS historial_medico ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "paciente_id INTEGER NOT NULL,"
                    + "fecha_consulta TIMESTAMP,"
                    + "diagnostico TEXT,"
                    + "tratamiento TEXT,"
                    + "medicamentos TEXT,"
                    + "observaciones TEXT,"
                    + "medico TEXT,"
                    + "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "fecha_ultima_modificacion TIMESTAMP,"
                    + "modificado_por TEXT,"
                    + "FOREIGN KEY (paciente_id) REFERENCES pacientes(id)"
                    + ")");

            // Tabla de Auditoría
            stmt.execute("CREATE TABLE IF NOT EXISTS auditoria ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "usuario TEXT NOT NULL,"
                    + "accion TEXT NOT NULL,"
                    + "entidad TEXT NOT NULL,"
                    + "id_entidad INTEGER,"
                    + "detalles TEXT,"
                    + "fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")");

            logger.info("Base de datos inicializada correctamente");

            // Crear usuario administrador por defecto
            crearUsuarioAdmin();

        } catch (Exception e) {
            logger.error("Error al inicializar la base de datos", e);
        }
    }

    private static void crearUsuarioAdmin() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Verificar si ya existe un administrador
            var resultSet = stmt.executeQuery("SELECT COUNT(*) as count FROM usuarios WHERE rol = 'ADMINISTRADOR'");
            if (resultSet.next() && resultSet.getInt("count") == 0) {
                // Insertar usuario administrador por defecto
                String contrasenaHash = com.cecamed.model.Usuario.hashContrasena("admin123");
                stmt.execute("INSERT INTO usuarios (nombre, email, contrasena, rol) VALUES ("
                        + "'Administrador',"
                        + "'admin@cecamed.com',"
                        + "'" + contrasenaHash + "',"
                        + "'ADMINISTRADOR'"
                        + ")");
                logger.info("Usuario administrador creado: admin@cecamed.com / admin123");
            }
        } catch (Exception e) {
            logger.error("Error al crear usuario administrador", e);
        }
    }
}
