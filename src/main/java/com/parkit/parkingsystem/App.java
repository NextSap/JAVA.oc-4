package com.parkit.parkingsystem;

import com.parkit.parkingsystem.loader.PropertiesLoader;
import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger("App");

    private static final PropertiesLoader configLoader = new PropertiesLoader();
    public static void main(String[] args) {
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }

    public static String getConfig(String key) {
        return configLoader.getProperties().getProperty(key);
    }
}
