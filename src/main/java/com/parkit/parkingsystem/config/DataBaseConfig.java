package com.parkit.parkingsystem.config;

import com.parkit.parkingsystem.App;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DataBaseConfig {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

    public Connection getConnection() {
        try {
            LOGGER.info("Create DB connection");
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    App.getConfig("DB_PROD"),
                    App.getConfig("DB_USER_PROD"),
                    App.getConfig("DB_PASSWORD_PROD"));
        } catch (Exception e) {
            LOGGER.error("Error connecting to the prod database", e);
            throw new RuntimeException("Error connecting to the prod database", e);
        }
    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection", e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement", e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set", e);
            }
        }
    }
}