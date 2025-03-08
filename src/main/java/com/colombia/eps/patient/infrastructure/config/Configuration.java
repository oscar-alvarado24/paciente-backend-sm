package com.colombia.eps.patient.infrastructure.config;

import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private final Properties properties;

    public Configuration() {
        // Inicializa y carga las propiedades de configuración aquí
        properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            // Manejo de excepciones en caso de error al cargar la configuración
        }
    }

    public String getProperty(String property) {
        return properties.getProperty(property);
    }
}
