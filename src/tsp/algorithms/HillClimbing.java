package tsp.algorithms;

import tsp.City;
import tsp.Tour;

import java.util.Collections;
import java.util.List;

public class HillClimbing extends Search {

    Tour current;

    public HillClimbing(List<City> c) {
        super(c);
        current = new Tour(c);
    }

    public HillClimbing (Tour given) {
        current = given.duplicate();
    }

    public void run () {
        int i = 0; int j = 0;

        while (true) {
            Tour neighbor = current.duplicate();

            //These loops try every possible swap in the tour to see if it improves the objective function.
            //The loops end when, tried every single swap, none of them has improved the o.f.
            for (i = 0; i< neighbor.noCities(); i++)
            {
                for (j = i + 1; j < neighbor.noCities(); j++)
                {
                    Collections.swap(neighbor.getCities(), i, j);

                    if (neighbor.getDistance() < current.getDistance()) {
                        //we had an improvement, updating the current tour and ending the loop
                        current = neighbor.duplicate();
                        break;
                    } else { //no improvements, we revert the swap
                        neighbor = current.duplicate();
                    }
                }

                //we break the outer loop because if the condition of the inner loop is still met, it means that
                // we found a successful swap in the inner loop, so we "did our move up the hill"
                if (j < neighbor.noCities())
                    break;
            }

            //the two for loops went all the way through without success,
            //so there's no adjacent state that improves the objective function and we can end this
            if (i == neighbor.noCities() && j == i)
                break;
        }

        //we update the static variable best with the solution produced by this thread
        synchronized (best) {
            if (current.getDistance() < best.getDistance()) {
                best = current.duplicate();
            }
        }
    }
}
