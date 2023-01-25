/*
 * An Air traffic simulator application, generates virtual airports and simulates planes using runways to land and
 *  depart
 */
package q2;

/**
 * Published: 08/01/2021
 *
 * @author Saar
 */
public class App2 {
    /**
     * A generic main function to launch the app.
     * @param args Additional launch arguments - unused.
     */
    public static void main(String[] args) throws InterruptedException {
    Simulation simulation = new Simulation();
    simulation.start();
    }
}
