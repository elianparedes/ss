package ar.edu.itba.ss.cim;

import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.simulation.events.EventsQueue;

public class CellIndexMethod implements Algorithm<CellIndexMethodParameters> {

    @Override
    public void calculate(CellIndexMethodParameters params, EventListener eventListener) {
        int i;

        for (int j = 0; j < 100; j++) {
            i = j * 10;

            eventListener.emit(new Event<>(i));
        }
    }
}
