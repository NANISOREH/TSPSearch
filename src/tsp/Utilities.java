package tsp;

import java.util.ArrayList;

/**
 * The class Util is the one containing two key methods for the implementation of a Simulated Annealing algorithm,
 * namely the probability - which will be used to express the likelihood to move into the search space - and the
 * distance, w
 */
public class Utilities {

    /**
     * The method simply computes the Euclidean distance between the two cities given as parameters.
     *
     * @param city1: the first city;
     * @param city2: the second city;
     *
     * @return the Euclidean distance between city1 and city2.
     */
    public static double distance(City city1, City city2) {
        double xDist = Math.abs(city1.getX() - city2.getX());
        double yDist = Math.abs(city1.getY() - city2.getY());
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public static ArrayList<City> populate (int n) {
        ArrayList<City> result = new ArrayList<>();

        for (int i = 0; i<n; i++) {
            City current = new City("City" + (i+1), 10000 * Math.random(), 10000 * Math.random());
            result.add(current);
        }

        return result;
    }
}
