package ar.edu.itba.ss.simulation.worker;

import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.util.List;

public interface MultiQueueWorker {
    void execute(List<EventsQueue> queues);
}
