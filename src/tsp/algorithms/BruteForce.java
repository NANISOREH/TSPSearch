package tsp.algorithms;

import tsp.City;
import tsp.Tour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BruteForce extends Search {

    public BruteForce(List<City> c) {
        super (c);
    }

    /*Just tries every permutation of the input list, nothing interesting*/
    public void run () {

        Tour current = new Tour(cities);
        double bestScore = current.getDistance();

        int n = cities.size();
        int[] indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }

        int i = 0;
        while (i < n) {
            if (indexes[i] < i) {
                Collections.swap(current.getCities(), i % 2 == 0 ?  0: indexes[i], i);
                if (current.getDistance() < bestScore){
                    best = current.duplicate();
                    bestScore = best.getDistance();
                }
                indexes[i]++;
                i = 0;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }
    }

}
