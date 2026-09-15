# CECAmed - Sistema de Gestión de Clínica
# Hacer el repositorio público
gh repo edit andas26611-ops/CECAmed --visibility public
## Descripción

CECAmed es un sistema integral de gestión para clínicas médicas desarrollado en Java con JavaFX. Proporciona funcionalidades completas para la administración de pacientes, citas médicas, historial clínico y auditoría de operaciones.

## Características Principales

### 🔐 Autenticación y Seguridad
- Sistema de login con autenticación de usuarios
- Registro de nuevos usuarios con validación
- Roles diferenciados (Administrador, Médico)
- Contraseñas encriptadas con hash
- Auditoría completa de operaciones

### 👥 Gestión de Pacientes
- Registro completo de pacientes con:
  - Información personal (nombre, apellido, cédula)
  - Datos de contacto
  - Historia médica (alergias, antecedentes patológicos)
- Búsqueda y filtrado de pacientes
- Actualización y eliminación de registros
- Validación de datos en tiempo real

### 📅 Agenda de Citas
- Programación de citas médicas
- Gestión de estados (Pendiente, Confirmada, Completada, Cancelada)
- Visualización por fecha y paciente
- Recordatorios de citas

### 📋 Historial Médico
- Registro de consultas y diagnósticos
- Documentación de tratamientos
- Control de medicamentos prescritos
- Observaciones médicas

### 📄 Expedientes Clínicos
- Acceso centralizado a expedientes de pacientes
- Información completa del historial médico
- Documentación de procedimientos
- Generación de reportes

### 🔍 Sistema de Auditoría
- Registro de todas las operaciones del sistema
- Trazabilidad de cambios (quién, qué, cuándo)
- Filtrado por usuario, acción o entidad
- Cumplimiento normativo

## Requisitos Técnicos

### Software
- Java JDK 11 o superior
- Maven 3.6+
- SQLite 3

### Dependencias
- JavaFX 20
- SQLite JDBC 3.43.0.0
- SLF4J 2.0.7
- Logback 1.4.8

## Estructura del Proyecto

```
CECAmed/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/cecamed/
│   │           ├── CECAmedApp.java           # Aplicación principal
│   │           ├── model/                    # Modelos de datos
│   │           │   ├── Usuario.java
│   │           │   ├── Paciente.java
│   │           │   ├── Cita.java
│   │           │   ├── HistorialMedico.java
│   │           │   └── Auditoria.java
│   │           ├── dao/                      # Acceso a datos
│   │           │   ├── UsuarioDAO.java
│   │           │   ├── PacienteDAO.java
│   │           │   ├── CitaDAO.java
│   │           │   ├── HistorialMedicoDAO.java
│   │           │   └── AuditoriaDAO.java
│   │           ├── database/                 # Configuración BD
│   │           │   ├── DatabaseConnection.java
│   │           │   └── DatabaseInitializer.java
│   │           ├── ui/                       # Interfaz gráfica
│   │           │   ├── LoginView.java
│   │           │   ├── DashboardView.java
│   │           │   ├── GestionPacientesView.java
│   │           │   ├── AgendaCitasView.java
│   │           │   ├── HistorialMedicoView.java
│   │           │   ├── ExpedientesClinicosView.java
│   │           │   └── AuditoriaView.java
│   │           └── utils/                    # Utilidades
│   │               ├── ConfiguracionApp.java
│   │               └── ValidadorUtil.java
│   └── test/
│       └── java/
│           └── com/cecamed/
├── config/
│   └── app.properties                        # Configuración
├── pom.xml                                   # Dependencias Maven
└── README.md                                 # Este archivo
```

## Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/andas26611-ops/CECAmed.git
cd CECAmed
```

### 2. Instalar dependencias
```bash
mvn clean install
```

### 3. Ejecutar la aplicación
```bash
mvn javafx:run
```

## Credenciales por Defecto

Al iniciar por primera vez, el sistema crea automáticamente un usuario administrador:

- **Email**: admin@cecamed.com
- **Contraseña**: admin123
- **Rol**: ADMINISTRADOR

## Guía de Uso

### Acceso al Sistema
1. Ejecutar la aplicación
2. Ingresar credenciales en la pantalla de login
3. Hacer clic en "Iniciar Sesión"

### Gestión de Pacientes
1. Seleccionar "Gestionar Pacientes" en el menú
2. Hacer clic en "Nuevo Paciente" para registrar
3. Completar formulario con información del paciente
4. Guardar cambios

### Programar Citas
1. Seleccionar "Agenda de Citas"
2. Hacer clic en "Nueva Cita"
3. Seleccionar paciente y fecha
4. Confirmar programación

### Consultar Historial
1. Seleccionar "Historial Médico"
2. Buscar paciente
3. Ver registros de consultas anteriores

## Base de Datos

### Tablas Principales

#### usuarios
- id (PK)
- nombre
- email (UNIQUE)
- contrasena
- rol
- activo
- fecha_creacion
- fecha_ultima_modificacion
- modificado_por

#### pacientes
- id (PK)
- nombre
- apellido
- cedula (UNIQUE)
- fecha_nacimiento
- genero
- telefono_contacto
- email
- direccion
- ciudad
- alergias
- antecedentes_patologicos
- activo
- fecha_creacion
- fecha_ultima_modificacion
- modificado_por

#### citas
- id (PK)
- paciente_id (FK)
- nombre_paciente
- fecha
- hora
- motivo
- estado
- notas
- fecha_creacion
- fecha_ultima_modificacion
- modificado_por

#### historial_medico
- id (PK)
- paciente_id (FK)
- fecha_consulta
- diagnostico
- tratamiento
- medicamentos
- observaciones
- medico
- fecha_creacion
- fecha_ultima_modificacion
- modificado_por

#### auditoria
- id (PK)
- usuario
- accion
- entidad
- id_entidad
- detalles
- fecha_hora

## Seguridad

- Almacenamiento seguro de contraseñas con hash
- Validación de entrada de datos
- Control de acceso basado en roles
- Auditoría completa de operaciones
- Eliminación lógica de registros (no se borran, se marcan como inactivos)

## Logging

El sistema registra todas las operaciones en archivos de log:
- **Ubicación**: `logs/CECAmed.log`
- **Nivel de logging**: Configurable en `app.properties`

## Desarrollador

**Nombre**: Andas26611-ops
**Email**: andas.26611@gmail.com
**GitHub**: https://github.com/andas26611-ops

## Licencia

Este proyecto está disponible bajo licencia MIT.

## Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el proyecto
2. Crear una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abrir un Pull Request

## Soporte

Para reportar bugs o solicitar features, por favor abrir un issue en:
https://github.com/andas26611-ops/CECAmed/issues

## Roadmap Futuro

- [ ] Integración con servicios de email
- [ ] Generación de reportes PDF
- [ ] Backup automático de base de datos
- [ ] Sincronización en la nube
- [ ] Aplicación móvil
- [ ] Sistema de facturación
- [ ] Integración con laboratorios
- [ ] Telemedicina

---

**CECAmed v1.0.0** - Desarrollado con ❤️ para mejorar la gestión clínica
