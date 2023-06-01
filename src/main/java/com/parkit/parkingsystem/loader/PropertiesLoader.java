package com.parkit.parkingsystem.loader;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Getter
public class PropertiesLoader {

    private final Properties properties;

    public PropertiesLoader() {
        this.properties = load();
    }

    /**
     *
     * @return Config file
     */
    private Properties load() {
        try (InputStream input = Files.newInputStream(Paths.get("/Users/louisdiilio/IdeaProjects/@ Openclassrooms/JAVA.oc-4/src/main/resources/config.properties"))) {

            Properties prop = new Properties();

            prop.load(input);

            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
