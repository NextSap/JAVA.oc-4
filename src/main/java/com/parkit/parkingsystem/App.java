package com.parkit.parkingsystem;

import com.parkit.parkingsystem.loader.PropertiesLoader;
import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    private static final Logger LOGGER = LogManager.getLogger("App");
    private static final String CONFIG_PATH = "src/main/resources/config.properties";
    private static final PropertiesLoader CONFIG_LOADER = new PropertiesLoader();

    public static void main(String[] args) {
        LOGGER.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }

    public static String getConfig(String key) {
        return CONFIG_LOADER.getProperties().getProperty(key);
    }

    public static String getConfigPath() { return CONFIG_PATH; }
}
