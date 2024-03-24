package ar.edu.itba.ss;

import ar.edu.itba.ss.cim.CellIndexMethod;
import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventProcessor;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        CellIndexMethod cellIndexMethod = new CellIndexMethod();
        Simulation<CellIndexMethodParameters> sim = new Simulation<>(cellIndexMethod);
        CellIndexMethodParameters cellIndexMethodParameters = new CellIndexMethodParameters(1, 1, 1, 1.0, new ArrayList<>());

        EventProcessor processor = new EventProcessor();
        processor.registerHandler(Integer.class,(p)-> System.out.println("Este es del hanlder: " + p));

        sim.run(cellIndexMethodParameters);

        EventsQueue queue = sim.getEventQueue(Integer.class);
        for (Event<?> e:queue) {
            processor.processEvent(e);
        }
    }
}