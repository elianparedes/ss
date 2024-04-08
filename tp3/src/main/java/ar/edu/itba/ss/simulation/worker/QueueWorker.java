package ar.edu.itba.ss.simulation.worker;

import ar.edu.itba.ss.simulation.events.EventsQueue;

public interface QueueWorker {
    void execute(EventsQueue queue);
}
