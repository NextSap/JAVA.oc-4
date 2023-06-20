package com.parkit.parkingsystem.loader;

import com.parkit.parkingsystem.App;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Getter
public class PropertiesLoader {


    private static final Logger LOGGER = LogManager.getLogger("PropertiesLoader");
    private final Properties properties;


    public PropertiesLoader() {
        this.properties = load();
    }

    /**
     *
     * @return Config file
     */
    public Properties load() {
        try (InputStream input = Files.newInputStream(Paths.get(App.getConfigPath()))) {

            Properties prop = new Properties();

            prop.load(input);

            return prop;
        } catch (Exception e) {
            LOGGER.error("Unable to load properties file");
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }
}
