package tsp.algorithms;

import tsp.City;
import tsp.Tour;
import tsp.Utilities;

import java.util.Collections;
import java.util.List;

/**
 * The class SimulatedAnnealing implements the actual algorithm to solve the traveling salesman problem.
 */
public class SimulatedAnnealing extends Search {
    // First, let's set the two input parameter of the algorithm, namely the temperature and the cooling factor.
    private static double temperature = 10000;
    private static double coolingFactor = 0.99996;

    public SimulatedAnnealing(List<City> c) {
        super(c);
    }

    public void run () {

        // We create a new Tour with the given cities. Remember that the Tour constructor will shuffle the list.
        Tour current = new Tour(cities);

        // Now, let's define the main loop of the algorithm. As shown, the for loop
        // will slowly lower the temperature by a cooling factor.
        for (double t = temperature; t > 1; t *= coolingFactor) {
            Tour neighbor = current.duplicate();

            // In each iteration of the loop, we generate a neighbor
            // by randomly swapping two cities in our current tour.
            int index1 = (int) (neighbor.noCities() * Math.random());
            int index2 = (int) (neighbor.noCities() * Math.random());

            // Note that the method swap of Collections implements
            // the actual swapping of two elements of a list.
            Collections.swap(neighbor.getCities(), index1, index2);

            double currentLength = current.getDistance();
            double neighborLength = neighbor.getDistance();

            // By means of the probability method, the algorithm determines
            // whether the neighbori will be accepted or not.
            if (Math.random() < probability(currentLength, neighborLength, t)) {
                // If so, the neighbor becomes the current solution.
                current = neighbor.duplicate();
            }

            // Finally, if the current length is lower than the best,
            // then the best solution becomes the current one.
            if (current.getDistance() < best.getDistance()) {
                synchronized (best) {
                    best = current.duplicate();
                }
            }
        }
    }

    /**
     * The method computes the probability of accepting one of the two tours given as parameters in form of length.
     * The probability may be based on the temperature given as third parameter.
     *
     * @param f1: the length of the first tour;
     * @param f2: the length of the second tour;
     * @param temp: the current temperature of the algorithm.
     *
     * @return: a double number ranging between 0 and 1, representing the probability
     *          of accepting one of the two tours.
     */
    private double probability(double f1, double f2, double temp) {
        //  Note that if the length of the second tour is shorter than the length
        //  of the first tour we will keep the second tour.
        double delta = f2 - f1;
        if (delta < 0) return 1;
        // Otherwise, we are going to return the probability of accepting the second tour.
        return Math.exp((f1 - f2) / temp);
    }
}