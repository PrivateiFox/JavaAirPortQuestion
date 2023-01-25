package q2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Published: 08/01/2021
 *
 * @author Saar
 */
public class Airport {
    private final String name;
    private final int runwayCount;
    private static final int FIRST_RUNWAY_ID = 0;
    private final List<Integer> freeRunways = Collections.synchronizedList(new ArrayList<>());
    private final Lock runwaysAccessLock = new ReentrantLock();

    private final Condition canReadRunways = runwaysAccessLock.newCondition();

    /**
     * A representation of an airport.
     * @param airportName The name of the virtual airport.
     * @param runwayCount The amount of runways the airport has available for allocation.
     */
    public Airport(String airportName, int runwayCount) {
        this.name = airportName;
        this.runwayCount = runwayCount;
        populateRunwayIds();
    }

    /**
     * Generates a list of enumerated runways for the airport.
     */
    private void populateRunwayIds() {
        for (int count = 0; count < this.runwayCount; count++) {
            freeRunways.add(count);
        }
    }

    /**
     * Allocates a runway for a departing flight.
     * @param departingFlightId The id of the departing flight
     * @return The id of the runway to use for departure.
     */
    public int depart(int departingFlightId) {
        int runwayId = -1;
        try{
            // freezes requesting function up until there's a free runway available for it.
            this.runwaysAccessLock.lock();
            if (this.freeRunways.isEmpty())
                this.canReadRunways.await();
            runwayId = this.freeRunways.get(FIRST_RUNWAY_ID);
            this.freeRunways.remove(FIRST_RUNWAY_ID);
            System.out.printf("[%s Airport] Flight %d has allocated runway %d for departure%n", this.name, departingFlightId, runwayId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.runwaysAccessLock.unlock();
        }
        return runwayId;
    }

    /**
     * Allocates a runway for a landing flight.
     * @param landingFlightID The id of the landing flight.
     * @return The id of the runway to use for landing.
     */
    public int land(int landingFlightID) {
        // return free RunwayId
        int runwayId = -1;
        try {
            // freezes requesting function up until there's a free runway available for it.
            this.runwaysAccessLock.lock();
            if (this.freeRunways.isEmpty())
                this.canReadRunways.await();
            runwayId = this.freeRunways.get(FIRST_RUNWAY_ID);
            this.freeRunways.remove(FIRST_RUNWAY_ID);
            System.out.printf("[%s Airport] Flight %d has allocated runway %d for landing%n", this.name, landingFlightID, runwayId);
            return runwayId;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.runwaysAccessLock.unlock();
        }
        return runwayId;
    }

    /**
     * Frees a runway for use by new flights.
     * @param flightId The flight who's freeing their runway.
     * @param runwayId The runway id to free(add back to the pool)
     */
    public void freeRunway(int flightId, int runwayId) {
        System.out.printf("[%s Airport] Flight %d has freed runway %d%n", this.name, flightId, runwayId);
        try {
            this.runwaysAccessLock.lock();
            this.freeRunways.add(runwayId);
            this.canReadRunways.signal();
        }
        finally {
            this.runwaysAccessLock.unlock();
        }
    }
}
