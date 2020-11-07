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
        TspSolver solver = new TspSolver();

        start = Instant.now();
        solution = solver.solve(cities, Algorithm.HILL_CLIMBING);
        finish = Instant.now();
        System.out.println("Hill Climbing:");
        System.out.println("Solution obtained in " + Duration.between(start, finish).toMillis() + " milliseconds");
        System.out.println("Final tour length: " + solution.getDistance());
        System.out.println("Tour: " + solution + "\n\n");

        start = Instant.now();
        solver.configureHCRR(10);
        solution = solver.solve(cities, Algorithm.HILL_CLIMBING_RANDOM_RESTARTS);
        finish = Instant.now();
        System.out.println("Hill Climbing with 10 random restarts:");
        System.out.println("Solution obtained in " + Duration.between(start, finish).toMillis() + " milliseconds");
        System.out.println("Final tour length: " + solution.getDistance());
        System.out.println("Tour: " + solution + "\n\n");

        start = Instant.now();
        solution = solver.solve(cities, Algorithm.SIMULATED_ANNEALING);
        finish = Instant.now();
        System.out.println("Simulated Annealing:");
        System.out.println("Solution obtained in " + Duration.between(start, finish).toMillis() + " milliseconds");
        System.out.println("Final tour length: " + solution.getDistance());
        System.out.println("Tour: " + solution + "\n\n");

        start = Instant.now();
        solver.configureGS(2000, 2, 0.01, 1.0, 600, 3600000L);
        solution = solver.solve(cities, Algorithm.GENETIC_SEARCH);
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
