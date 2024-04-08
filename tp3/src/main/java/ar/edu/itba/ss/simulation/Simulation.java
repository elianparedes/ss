package ar.edu.itba.ss.simulation;

import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.util.HashMap;
import java.util.Map;

public class Simulation<P extends AlgorithmParameters>{

    private final Algorithm<P> algorithm;
    private final Map<Class<?>, EventsQueue> events;

    public Simulation(Algorithm<P> algorithm){
        this.algorithm = algorithm;
        this.events = new HashMap<>();
    }

    public void run(P params) {
        algorithm.calculate(params,(e)->{
            Class<?> payloadClass = e.getPayload().getClass();
            EventsQueue eventsQueue = events.getOrDefault(payloadClass,new EventsQueue());
            eventsQueue.add(e);
            events.putIfAbsent(payloadClass,eventsQueue);
        });
    }

    public EventsQueue getEventQueue(Class<?> payloadClass) {
        return events.get(payloadClass);
    }
}
