package tsp.algorithms;

import tsp.City;
import tsp.Tour;

import java.util.ArrayList;
import java.util.List;

/* Abstract class incapsulating shared data and behaviour of any search algorithm for TSP*/
public abstract class Search extends Thread{
    protected List<City> cities;
    protected static Tour best;

    protected Search(List<City> c) {
        cities = new ArrayList<>(c);
        best = new Tour(cities);
    }

    public static Tour getBest () {
        return best;
    }
}
