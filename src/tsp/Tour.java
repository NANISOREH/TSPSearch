package tsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class Tour represents a path covering multiple cities. A Tour is composed of a list
 * of cities belonging to the path as well as of a distance, which will represent the length of the path.
 */
public class Tour {

    private List<City> cities;
    private double distance;

    public Tour(List<City> cities) {
        this.cities = new ArrayList<>(cities);
        // Note that the shuffle method will randomly permute the specified list of cities.
        Collections.shuffle(this.cities);
        updateTourLength();
    }

    public Tour() {}

    public City getCity(int index) {
        return cities.get(index);
    }

    /**
     * The method getTourLength will compute the distance traveled by the salesman.
     * @return the total distance traveled.
     */
    public void updateTourLength() {
        double totalDistance = 0;

        for (int i = 1; i < noCities(); i++) {
            totalDistance += Utilities.distance(cities.get(i), cities.get(i-1));
        }

        distance = totalDistance;
    }

    public double getDistance () {
        updateTourLength();
        return distance;
    }

    public Tour duplicate() {
        Tour duplicated = new Tour();
        duplicated.setCities(new ArrayList<City>(this.cities));
        duplicated.updateTourLength();
        return duplicated;
    }

    public int noCities() {
        return cities.size();
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * The toString method will print the information of an instance of a Tour.
     * @return a String representation of a Tour.
     */
    @Override
    public String toString() {
        String list = "";
        for (City c : cities) {
            list = list + c.getName() + " - ";
        }
        return list;
    }
}
