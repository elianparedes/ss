package ar.edu.itba.ss.simulation;

import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.simulation.events.EventsQueue;

public class Simulation<P extends AlgorithmParameters>{

    private Algorithm<P> algorithm;
    private EventsQueue events;

    public Simulation(Algorithm<P> algorithm){
        this.algorithm = algorithm;
        this.events = new EventsQueue();
    }

    public Simulation(Algorithm<P> algorithm, EventsQueue eventsQueue){
        this.algorithm =algorithm;
        this.events = eventsQueue;
    }

    public void run(P params) {
        algorithm.calculate(params, (e) -> events.add(e));
    }

}
