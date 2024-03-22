package ar.edu.itba.ss;

import ar.edu.itba.ss.cim.CellIndexMethod;
import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventProcessor;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        CellIndexMethod cim = new CellIndexMethod();
        CellIndexMethodParameters params = new CellIndexMethodParameters();

        Simulation<CellIndexMethodParameters> sim = new Simulation<>(cim);

        EventProcessor processor = new EventProcessor();
        processor.registerHandler(Integer.class,(p)-> System.out.println("Este es del hanlder: " + p));

        sim.run(params);

        EventsQueue queue = sim.getEvents();
        for (Event<?> e:queue) {
            processor.processEvent(e);
        }
    }
}