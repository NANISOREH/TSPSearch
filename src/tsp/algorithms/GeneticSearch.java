package tsp.algorithms;

import tsp.City;
import tsp.Tour;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class GeneticSearch extends Search {
    Logger log = Logger.getLogger("GeneticSearch");

    //number of solutions in a single generation
    private int populationSize;

    //number of times the population is halved to get the final mating pool
    //populationSize / (2 * tournamentRounds) HAS to be even
    private int tournamentRounds;

    //the probability of encountering a mutation in a "chromosome"
    private double mutationProbability;

    //the percentage of tours to be considered as "elite" and preserved despite the current offspring
    //not offering an improvement in average fitness on the previous generation
    private double elitismRate;

    //limit of consecutive iterations allowed without picking a new generation
    private int maxUnluckyRuns;

    //time limit expressed in milliseconds
    private long timeBudget;

    public GeneticSearch (List<City> c) {
        super(c);
    }

    public void run () {
        ArrayList<Tour> currentGeneration = new ArrayList<>();
        ArrayList<Tour> currentGenerationBackup;
        ArrayList<Tour> nextGeneration = new ArrayList<>();
        double bestAverage = 0.0;
        double average;
        int unluckyRuns = 0;
        int iterations = 0;
        Instant start;
        Instant current;

        //We start by creating random solutions to get us started
        for (int i = 0; i < populationSize; i++) {
            currentGeneration.add(i, new Tour(cities));
        }
        average = calculateAverageFitness(currentGeneration);
        bestAverage = average;
        start = Instant.now();
        current = Instant.now();

        //Main loop of the algorithm
        for (iterations=0; unluckyRuns < maxUnluckyRuns && Duration.between(start, current).toMillis() < timeBudget; iterations++) {

            //list with a copy of the current generation in case the next one is not worth keeping around
            currentGenerationBackup = new ArrayList<>(currentGeneration);

            //We start a deathmatch tournament to implement selection:
            //the solutions in the current generation are coupled and will fight each other to get selected.
            //We play two rounds to narrow the list down to a fraction of the original size.
            for (int i = 0; i < tournamentRounds; i++) {
                currentGeneration = new ArrayList<>(playTournament(currentGeneration));
            }

            //We make our selected few reproduce;
            //Every one of them will mate with as many random partners as needed to make sure
            //that the next generation has the same fixed population as the current one
            int random;
            for (int i = 0; i < currentGeneration.size(); i++) {
                random = 0;

                for (int j=0; j < populationSize / (currentGeneration.size()/2); j++) {

                    while (random == i) {
                        random = (int) (currentGeneration.size() * Math.random());
                    }

                    nextGeneration.add(reproduce(currentGeneration.get(i), currentGeneration.get(random)));
                }
            }

            //Very simple mutation schedule: every city in a child Tour generated has a small fixed chance of being swapped with another one
            int randomIndex;
            for (Tour t : nextGeneration) {
                for (City c : t.getCities()) {
                    if (Math.random() < mutationProbability) {
                        randomIndex = (int) (cities.size() * Math.random());
                        Collections.swap(t.getCities(), t.getCities().indexOf(c), randomIndex);
                    }
                }
            }

            //We calculate and judge the average fitness of the new generation:
            //If the average fitness improved in this iteration, we reset the number of unlucky runs.
            //If the average fitness didn't improve, we increment the number of unlucky runs.
            //After a certain number of unlucky runs, the algorithm will stop and call it a day.
            average = calculateAverageFitness(nextGeneration);
            if (average >= bestAverage) {
                unluckyRuns++;
            }
            else if (average < bestAverage){
                unluckyRuns = 0;
                bestAverage = average;
            }

            //We update the best Tour if the champion of the current batch is worthy of the crown
            Tour champion = findBestTour(nextGeneration);
            if (champion.getDistance() < getBest().getDistance()) {
                synchronized (best) {
                    best = champion.duplicate();
                }
            }

            //If we improved the average fitness we update the currentGeneration list with the new generation,
            //or else we revert to the previous one if we didn't. In the latter case, we still keep the top 5% tours of the generation.
            if (unluckyRuns == 0)  {
                log.info("Thread " + currentThread() + ":\n" + "New generation accepted on iteration "
                        + iterations + "\nAverage fitness: " + average + "\nCurrent best: " + best.getDistance() + "\n\n");
                currentGeneration = new ArrayList<>(nextGeneration);
            } else {
                currentGeneration = new ArrayList<>(currentGenerationBackup);
                preserveElite(currentGeneration, nextGeneration);
            }
            nextGeneration.clear();

            //Checking if we ran out of time
            current = Instant.now();
            if (Duration.between(start, current).toMillis() > timeBudget)
                break;
        }

        log.warning("One thread has finished after " + iterations + " iterations and the top result is "
                + findBestTour(currentGeneration) + "\nFitness: " + findBestTour(currentGeneration).getDistance());

    }

    private void preserveElite(ArrayList<Tour> target, ArrayList<Tour> source) {
        Collections.sort(target);
        Collections.sort(source);

        for (int i=0; i < (int) source.size() * elitismRate; i++) {
            target.set(target.size() - i - 1, source.get(i));
        }
    }

    /*
    A basic ibridation algorithm. I'm looking for a better one.
    * */
    private Tour reproduce(Tour firstParent, Tour secondParent) {
        Tour child = new Tour();
        child.setCities(new ArrayList<City>());
        int i;

        for (i=0; i<cities.size(); i++) {
            if (Math.random() < 0.5) {
                break;
            }
            child.getCities().add(i, firstParent.getCity(i));
        }

        for (City c : secondParent.getCities()) {
            if (!child.getCities().contains(c)){
                child.getCities().add(c);
            }
        }

        return child;
    }

    /*Helper method implementing a K-way tournament.
    * In other words
    * TOURNAMENT ARC!
    * */
    private ArrayList<Tour> playTournament (List<Tour> participants) {
        ArrayList<Tour> selected = new ArrayList<>();
        ArrayList<Integer> drawnNumbers = new ArrayList<>();
        int random = 0;
        for (int i = 0; i < participants.size(); i++) {
            if (!drawnNumbers.contains(i)) {
                drawnNumbers.add(i);
                while (drawnNumbers.contains(random)) {
                    random = (int) (participants.size() * Math.random());
                }
                drawnNumbers.add(random);
                selected.add(playGame(participants.get(i), participants.get(random)));
            }
        }

        return selected;
    }

    /*Plays an actual "match" of the tournament.
    * The winner will most probably be the fittest Tour.
    * There's a very slight chance that the weaker one will win,
    * because, you know, we all like a good underdog story!
    * */
    private Tour playGame (Tour first, Tour second) {
        double firstProb;
        if (first.getDistance() < second.getDistance()) {
            firstProb = 0.999;
        } else {
            firstProb = 0.001;
        }

        if (Math.random() < firstProb)
            return first;
        else
            return second;
    }

    private double calculateAverageFitness(ArrayList<Tour> tours) {
        double total = 0;
        for (Tour t : tours) {
            total += t.getDistance();
        }

        return total / tours.size();
    }

    private Tour findBestTour(ArrayList<Tour> tours) {
        Tour best = tours.get(0);

        for (Tour t : tours) {
            if (t.getDistance() < best.getDistance()) {
                best = t.duplicate();
            }
        }

        return best;
    }

    public void setParameters (int populationSize, int tournamentRounds, double mutationProbability, double elitismRate, int maxUnluckyRuns, long timeBudget) {
        this.populationSize = populationSize;
        this.tournamentRounds = tournamentRounds;
        this.mutationProbability = mutationProbability;
        this.elitismRate = elitismRate;
        this.maxUnluckyRuns = maxUnluckyRuns;
        this.timeBudget = timeBudget;
    }
}
