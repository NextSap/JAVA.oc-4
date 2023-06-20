package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InputReaderUtil {

    private static final Scanner SCAN = new Scanner(System.in);
    private static final Logger LOGGER = LogManager.getLogger("InputReaderUtil");

    /**
     * Return the
     * @return number written to the console by the user
     */
    public int readSelection() {
        try {
            return Integer.parseInt(SCAN.nextLine());
        } catch (Exception e) {
            LOGGER.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     *
     * @return the vehicle plate written to the console by the user
     */
    public String readVehiclePlate() {
        String vehiclePlate = SCAN.nextLine();
        if (vehiclePlate == null || vehiclePlate.trim().length() == 0)
            throw new IllegalArgumentException("Invalid input provided");

        return vehiclePlate;
    }
}
