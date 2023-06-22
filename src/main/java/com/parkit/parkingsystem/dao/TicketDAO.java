package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.App;
import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.ParkingType;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class TicketDAO {

    private static final Logger LOGGER = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public boolean saveTicket(Ticket ticket) {
        Connection con = dataBaseConfig.getConnection();
        boolean saved = false;

        try (PreparedStatement ps = con.prepareStatement(App.getConfig("SAVE_TICKET"))) {
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehiclePlate());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : new Timestamp(ticket.getOutTime().getTime()));

            saved = ps.execute();

            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception e) {
            LOGGER.error("Error saving ticket:", e);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return saved;
    }

    public Ticket getTicket(String vehiclePlate) {
        Connection con = dataBaseConfig.getConnection();
        Ticket ticket = null;

        try (PreparedStatement ps = con.prepareStatement(App.getConfig("GET_TICKET"))) {
            ps.setString(1, vehiclePlate);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ParkingSpot parkingSpot = ParkingSpot.builder()
                        .withId(rs.getInt(1)).withParkingType(ParkingType.valueOf(rs.getString(6)))
                        .withIsAvailable(false).build();

                ticket = Ticket.builder()
                        .withParkingSpot(parkingSpot).withId(rs.getInt(2)).withVehiclePlate(vehiclePlate)
                        .withPrice(rs.getDouble(3)).withInTime(rs.getTimestamp(4))
                        .withOutTime(rs.getTimestamp(5)).build();
            }

            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);

            if (ticket == null) {
                LOGGER.error("Error getting ticket: ticket not found");
                throw new NullPointerException("Ticket with plate `" + vehiclePlate + "` not found");
            }
        } catch (Exception ex) {
            LOGGER.error("Error getting ticket:", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return ticket;
    }

    public boolean updateTicket(Ticket ticket) {
        Connection con = dataBaseConfig.getConnection();
        boolean updated = false;

        try (PreparedStatement ps = con.prepareStatement(App.getConfig("UPDATE_TICKET"))) {
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3, ticket.getId());

            updated = ps.executeUpdate() != 0;

            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            LOGGER.error("Error updating ticket info:", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return updated;
    }

    public int getAmountTicket(String vehiclePlate) {
        Connection con = dataBaseConfig.getConnection();
        int amount = -1;

        try (PreparedStatement ps = con.prepareStatement(App.getConfig("GET_TICKETS"))) {
            ps.setString(1, vehiclePlate);

            ResultSet rs = ps.executeQuery();
            rs.last();
            amount = rs.getRow();

            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeResultSet(rs);
        } catch (Exception ex) {
            LOGGER.error("Error getting amount of ticket:", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return amount;
    }
}