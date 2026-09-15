package com.cecamed.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracionApp {
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionApp.class);
    private static Properties propiedades;

    static {
        propiedades = new Properties();
        try (FileInputStream fis = new FileInputStream("config/app.properties")) {
            propiedades.load(fis);
            logger.info("Configuración cargada correctamente");
        } catch (IOException e) {
            logger.warn("Archivo de configuración no encontrado, usando valores por defecto");
            cargarValoresDefecto();
        }
    }

    private static void cargarValoresDefecto() {
        propiedades.setProperty("app.nombre", "CECAmed");
        propiedades.setProperty("app.version", "1.0.0");
        propiedades.setProperty("app.descripcion", "Sistema de Gestión de Clínica");
        propiedades.setProperty("database.url", "jdbc:sqlite:CECAmed.db");
        propiedades.setProperty("log.level", "INFO");
    }

    public static String obtener(String clave) {
        return propiedades.getProperty(clave);
    }

    public static String obtener(String clave, String valorPorDefecto) {
        return propiedades.getProperty(clave, valorPorDefecto);
    }

    public static int obtenerInt(String clave, int valorPorDefecto) {
        try {
            return Integer.parseInt(propiedades.getProperty(clave, String.valueOf(valorPorDefecto)));
        } catch (NumberFormatException e) {
            return valorPorDefecto;
        }
    }

    public static boolean obtenerBoolean(String clave, boolean valorPorDefecto) {
        String valor = propiedades.getProperty(clave, String.valueOf(valorPorDefecto));
        return Boolean.parseBoolean(valor);
    }
}
