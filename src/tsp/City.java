package tsp;

/**
 * The class City represents a city in a two-dimensional space, with the x and y coordinates
 * that are received through the constructor. Furthermore, a City has a name.
 */
public class City {

    private String name;
    private double x;
    private double y;

    public City(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public City(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * The toString method will print the information of an instance of City.
     * @return a String representation of a City.
     */
    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}