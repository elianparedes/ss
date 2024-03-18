package ar.edu.itba.ss;

import ar.edu.itba.ss.cim.CellIndexMethod;
import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        CellIndexMethod cim = new CellIndexMethod();
        CellIndexMethodParameters params = new CellIndexMethodParameters();

        Simulation<CellIndexMethodParameters> sim = new Simulation<>(cim);
        sim.run(params);
    }
}