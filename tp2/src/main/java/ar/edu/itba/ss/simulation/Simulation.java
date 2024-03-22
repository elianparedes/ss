package ar.edu.itba.ss.simulation;

import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.util.function.Consumer;

public class Simulation<P extends AlgorithmParameters>{

    private final Algorithm<P> algorithm;
    private final EventsQueue events;

    public Simulation(Algorithm<P> algorithm){
        this.algorithm = algorithm;
        this.events = new EventsQueue();
    }

    public Simulation(Algorithm<P> algorithm, EventsQueue eventsQueue){
        this.algorithm =algorithm;
        this.events = eventsQueue;
    }

    public void run(P params) {
        algorithm.calculate(params, events::add);
    }

    public EventsQueue getEvents() {
        return events;
    }
}
