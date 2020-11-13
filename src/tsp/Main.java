package tsp;

import tsp.algorithms.Algorithm;
import tsp.algorithms.SimulatedAnnealing;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        List<City> cities = Utilities.populate(100);

        Instant start;
        Instant finish;
        Tour solution;
        TspSolver solver = TspSolver.getSolver();

        start = Instant.now();
        solution = solver.search(cities, Algorithm.HILL_CLIMBING);
        finish = Instant.now();
        System.out.println("Hill Climbing:");
        System.out.println("Solution obtained in " + Duration.between(start, finish).toMillis() + " milliseconds");
        System.out.println("Final tour length: " + solution.getDistance());
        System.out.println("Tour: " + solution + "\n\n");

/*        start = Instant.now();
        solution = solver.configureHCRR(10).search(cities, Algorithm.HILL_CLIMBING_RANDOM_RESTARTS);
        finish = Instant.now();
        System.out.println("Hill Climbing with 10 random restarts:");
        System.out.println("Solution obtained in " + Duration.between(start, finish).toMillis() + " milliseconds");
        System.out.println("Final tour length: " + solution.getDistance());
        System.out.println("Tour: " + solution + "\n\n");*/

        start = Instant.now();
        solution = solver.search(cities, Algorithm.SIMULATED_ANNEALING);
        finish = Instant.now();
        System.out.println("Simulated Annealing:");
        System.out.println("Solution obtained in " + Duration.between(start, finish).toMillis() + " milliseconds");
        System.out.println("Final tour length: " + solution.getDistance());
        System.out.println("Tour: " + solution + "\n\n");

        start = Instant.now();
        solution = solver.configureGS(2000, 2, 0.02, 0.05, 500, 3600000L).
                search(cities, Algorithm.GENETIC_SEARCH);
        finish = Instant.now();
        System.out.println("Genetic Search Approach:");
        System.out.println("Solution obtained in " + Duration.between(start, finish).toMillis() + " milliseconds");
        System.out.println("Final tour length: " + solution.getDistance());
        System.out.println("Tour: " + solution + "\n\n");

/*        start = Instant.now();
        solution = solver.solve(cities, Algorithm.BRUTE_FORCE);
        finish = Instant.now();
        System.out.println("Brute Force:");
        System.out.println("Solution obtained in " + Duration.between(start, finish).toMillis() + "milliseconds");
        System.out.println("Final tour length: " + solution.getDistance());
        System.out.println("Tour: " + solution + "\n\n");*/
    }
}
