package parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import parkingsystem.integration.config.DataBaseTestConfig;
import parkingsystem.integration.service.DataBasePrepareService;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;

    private static FareCalculatorService fareCalculatorService;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        fareCalculatorService = new FareCalculatorService();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehiclePlate()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertNotNull(ticket);
        Assertions.assertEquals("ABCDEF", ticket.getVehiclePlate());
        Assertions.assertEquals(ticket.getParkingSpot(), parkingSpotDAO.getParking(ticket.getParkingSpot().getId()));
    }

    @Test
    public void testParkingLotExit() throws InterruptedException {
        testParkingACar();
        Thread.sleep(500);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        double priceFromDB = ticket.getPrice();
        fareCalculatorService.calculateFare(ticket);
        Assertions.assertEquals(ticket.getPrice(), priceFromDB);
    }

    @Test
    public void testParkingLotExitRecurringUser() throws InterruptedException {
        testParkingACar();
        Thread.sleep(500);
        testParkingLotExit();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        ticket.setDiscounted(true);
        double priceFromDB = ticket.getPrice();
        fareCalculatorService.calculateFare(ticket);
        Assertions.assertEquals(ticket.getPrice() * 0.95, priceFromDB);
    }

}
