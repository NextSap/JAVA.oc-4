package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InputReaderUtil {

    private static final Scanner scan = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    /**
     * Return the
     * @return number written to the console by the user
     */
    public int readSelection() {
        try {
            return Integer.parseInt(scan.nextLine());
        } catch (Exception e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     *
     * @return the vehicle plate written to the console by the user
     */
    public String readVehiclePlate() {
        String vehiclePlate = scan.nextLine();
        if (vehiclePlate == null || vehiclePlate.trim().length() == 0)
            throw new IllegalArgumentException("Invalid input provided");

        return vehiclePlate;
    }
}
