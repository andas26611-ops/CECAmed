# CECAmed - Guía de Instalación

## Requisitos Previos

### Sistema Operativo
- Windows 10/11
- macOS 10.13+
- Linux (Ubuntu 18.04+, Fedora, etc.)

### Software Requerido
- **Java JDK 11 o superior**
  - Descargar desde: https://www.oracle.com/java/technologies/downloads/
  - Configurar variable de entorno JAVA_HOME

- **Maven 3.6 o superior**
  - Descargar desde: https://maven.apache.org/download.cgi
  - Configurar variable de entorno M2_HOME

### Verificar Instalación

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version
```

## Pasos de Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/andas26611-ops/CECAmed.git
cd CECAmed
```

### 2. Descargar Dependencias

```bash
mvn clean install
```

Este comando:
- Limpia compilaciones anteriores
- Descarga todas las dependencias necesarias
- Compila el proyecto
- Empaqueta la aplicación

### 3. Configurar la Aplicación

Crear archivo `config/app.properties` si no existe:

```properties
app.nombre=CECAmed
app.version=1.0.0
app.descripcion=Sistema de Gestión de Clínica
database.url=jdbc:sqlite:CECAmed.db
log.level=INFO
```

### 4. Ejecutar la Aplicación

```bash
mvn javafx:run
```

O compilar y ejecutar manualmente:

```bash
# Compilar
mvn clean compile

# Empaquetar
mvn package

# Ejecutar JAR
java -jar target/CECAmed-1.0.0.jar
```

## Problemas Comunes

### Error: "java: command not found"
**Solución**: Instalar Java JDK y configurar JAVA_HOME

### Error: "Maven is not recognized"
**Solución**: Instalar Maven y configurar M2_HOME y PATH

### Error: "Cannot connect to database"
**Solución**: Verificar permisos de carpeta y que SQLite esté instalado

### Error: "Cannot load JavaFX modules"
**Solución**: Actualizar el archivo pom.xml con la versión correcta de JavaFX

## Configuración Avanzada

### Cambiar Ubicación de Base de Datos

Editar `config/app.properties`:
```properties
database.url=jdbc:sqlite:/ruta/al/CECAmed.db
```

### Configurar Nivel de Logging

```properties
# DEBUG, INFO, WARN, ERROR
log.level=DEBUG
```

## Verificar Instalación

1. Ejecutar aplicación
2. Pantalla de login debe aparecer
3. Ingresar: admin@cecamed.com / admin123
4. Dashboard debe mostrar módulos disponibles

## Soporte

Si encuentras problemas:
1. Verificar que Java y Maven estén correctamente instalados
2. Revisar archivo de logs: `logs/CECAmed.log`
3. Abrir issue en: https://github.com/andas26611-ops/CECAmed/issues
