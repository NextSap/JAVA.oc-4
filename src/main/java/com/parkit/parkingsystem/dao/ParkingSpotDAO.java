package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.App;
import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.ParkingType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ParkingSpotDAO {
    private static final Logger LOGGER = LogManager.getLogger("ParkingSpotDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public int getNextAvailableSlot(ParkingType parkingType) {
        Connection con = dataBaseConfig.getConnection();
        int nextSlot = -1;

        try (PreparedStatement ps = con.prepareStatement(App.getConfig("GET_NEXT_PARKING_SPOT"))) {
            ps.setString(1, parkingType.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next())
                nextSlot = rs.getInt(1);

            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot:", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return nextSlot;
    }

    public boolean updateParking(ParkingSpot parkingSpot) {
        Connection con = dataBaseConfig.getConnection();
        boolean updated = false;

        try (PreparedStatement ps = con.prepareStatement(App.getConfig("UPDATE_PARKING_SPOT"))) {
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());

            updated = ps.execute();

            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            LOGGER.error("Error updating parking:", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return updated;
    }

    public ParkingSpot getParking(int id) {
        Connection con = dataBaseConfig.getConnection();
        ParkingSpot parkingSpot = null;

        try (PreparedStatement ps = con.prepareStatement(App.getConfig("GET_PARKING_SPOT"))) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
                parkingSpot = ParkingSpot.builder()
                        .withId(rs.getInt(1))
                        .withIsAvailable(rs.getBoolean(2))
                        .withParkingType(ParkingType.valueOf(rs.getString(3)))
                        .build();

            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            LOGGER.error("Error getting parking info:", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return parkingSpot;
    }
}
