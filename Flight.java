package q2;

import java.util.Random;

/**
 * Published: 08/01/2021
 *
 * @author Saar
 */
public class Flight extends Thread {
    private final int flightId;
    private final Airport departureAirport;
    private final Airport arrivalAirport;
    private final Random random = new Random();
    private static final int MIN_SLEEP_TIME = 2;
    private static final int MAX_SLEEP_TIME = 5 - MIN_SLEEP_TIME;

    /**
     * Implements a flight as a java thread.
     * @param flightId The id to use for the flight.
     * @param departureAirport The departure airport to call.
     * @param arrivalAirport The arrival airport to call.
     */
    public Flight(int flightId, Airport departureAirport, Airport arrivalAirport) {
        this.flightId = flightId;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }

    /**
     * Implements the whole flight procedure
     * Runs when the thread starts
     */
    @Override
    public void run() {
        try {
            depart();
            fly();
            land();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Allocates a runway at the departure airport, and simulates a departure.
     * @throws Exception If the thread gets interrupted unexpectedly and cannot recover.
     */
    private void depart() throws Exception {
        int departureLane = this.departureAirport.depart(this.flightId);
        if (departureLane == -1)
            throw new Exception("Failed to acquire departure lane");
        simulateFlight();
        this.departureAirport.freeRunway(this.flightId, departureLane);
    }

    /**
     * Simulates the flight itself
     */
    private void fly() {
        simulateFlight();
    }

    /**
     * Allocates a runway at the destination airport, and simulates a landing.
     * @throws Exception If the thread gets interrupted unexpectedly and cannot recover.
     */
    private void land() throws Exception {
        int arrivalLane = this.arrivalAirport.land(this.flightId);
        if (arrivalLane == -1)
            throw new Exception("Failed to acquire landing lane");
        simulateFlight();
        this.arrivalAirport.freeRunway(this.flightId, arrivalLane);
    }

    /**
     * Sleeps to simulate a flight stage.
     */
    private void simulateFlight() {
        try {
            Thread.sleep(random.nextInt(MAX_SLEEP_TIME) + MIN_SLEEP_TIME);
        } catch (InterruptedException ignored) {
        }
    }
}
