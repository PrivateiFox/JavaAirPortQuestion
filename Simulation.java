package q2;

import java.util.Random;

/**
 * Published: 08/01/2021
 *
 * @author Saar
 */
public class Simulation {
    private static final int AIRPORT_RUNWAY_COUNT = 3;
    private static final int AIRPORT_COUNT = 2;
    private static final int FLIGHT_COUNT = 10;
    private static final int FIRST_AIRPORT_ID = 0;
    private static final int SECOND_AIRPORT_ID = 1;
    private final Random random = new Random();
    private final Airport[] airports;
    private final Flight[] flights;

    /**
     * Simulates AIRPORT_COUNT Airports(each with AIRPORT_RUNWAY_COUNT runways) and FLIGHT_COUNT flights.
     * Flights are threads.
     */
    public Simulation() {
        this.airports = new Airport[AIRPORT_COUNT];
        this.flights = new Flight[FLIGHT_COUNT];
        populateAirports();
        populateFlights();
    }

    /**
     * Starts the simulation
     * @throws InterruptedException if a thread gets an interrupt.
     */
    public void start() throws InterruptedException {
        startFlights();
        waitForFlightsToFinish();
    }

    /**
     * Busy waits for all flight simulation threads to finish execution.
     * @throws InterruptedException if a thread gets an interrupt.
     */
    private void waitForFlightsToFinish() throws InterruptedException {
        for (Flight flight : this.flights) {
            flight.join(0);
        }
    }

    /**
     * Starts all flight threads by calling their respective .start functions.
     */
    private void startFlights() {
        for (Flight value : this.flights) {
            value.start();
        }
    }

    /**
     * Generates a list of airports.
     */
    private void populateAirports() {
        for (int count = 0; count < AIRPORT_COUNT; count++)
            this.airports[count] = new Airport(String.valueOf(count), AIRPORT_RUNWAY_COUNT);
    }

    /**
     * Generates a list of flights.
     */
    private void populateFlights() {
        for (int count = 0; count < FLIGHT_COUNT; count++)
            if (random.nextInt(this.airports.length) == FIRST_AIRPORT_ID)
                this.flights[count] = new Flight(count, this.airports[FIRST_AIRPORT_ID],
                        this.airports[SECOND_AIRPORT_ID]);
            else
                this.flights[count] = new Flight(count, this.airports[SECOND_AIRPORT_ID],
                        this.airports[FIRST_AIRPORT_ID]);
    }

}
