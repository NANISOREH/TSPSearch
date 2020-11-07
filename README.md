TSPSearch is a basic implementation of a bunch of algorithms to search for
suboptimal solutions to the Traveling Salesman Problem.
It's built upon an example showed in my AI University class. 
The simulated annealing implementation and the modeling of the problem (though heavily refactored)
are taken from there.

Implemented algorithms are: hill climbing, hill climbing with random restarts,
simulated annealing, and a genetic algorithm. I also added a brute force search, 
that's obviously only useful on small inputs to compare the results with the optimal solution. 

It includes a façade-like class called TspSolver that lets you choose, configure
and run the algorithms pretty easily from a client class.

Ngl, searching for solutions with the genetic algorithm doesn't give the best results at the moment,
I'm looking for ways to improve it. 
 