package ar.edu.itba.ss.simulation.worker;

import ar.edu.itba.ss.simulation.events.EventsQueue;

public class QueueWorkerHandler implements Runnable {

    private final QueueWorker worker;
    private final EventsQueue queue;


    public QueueWorkerHandler(QueueWorker worker, EventsQueue queue) {
        this.worker = worker;
        this.queue = queue;
    }

    @Override
    public void run() {
        worker.execute(queue);
    }
}
