package ar.edu.itba.ss.simulation.events;

@FunctionalInterface
public interface EventListener {
    void emit(Event<?> event);
}
