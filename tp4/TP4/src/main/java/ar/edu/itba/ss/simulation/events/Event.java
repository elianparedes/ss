package ar.edu.itba.ss.simulation.events;

public class Event<T> {

    private final T payload;
    public Event(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return payload.toString();
    }
}
