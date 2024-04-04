package ar.edu.itba.ss.simulation.worker;

import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.util.List;

public class MultiQueueWorkerHandler implements Runnable {

    private final MultiQueueWorker worker;
    private final List<EventsQueue> queues;


    public MultiQueueWorkerHandler(MultiQueueWorker worker, List<EventsQueue>  queues) {
        this.worker = worker;
        this.queues = queues;
    }

    @Override
    public void run() {
        worker.execute(queues);
    }

}
