package ar.edu.itba.ss.simulation.events;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventProcessor {
    private final Map<Class<?>, Consumer<Object>> handlers = new HashMap<>();

    public <T> void registerHandler(Class<T> type, Consumer<T> handler) {
        handlers.put(type, obj -> {
            if (type.isInstance(obj)) {
                handler.accept(type.cast(obj));
            }
        });
    }

    public <T> void processEvent(Event<T> event) {
        Consumer<Object> handler = handlers.get(event.getPayload().getClass());
        if (handler != null) {
            handler.accept(event.getPayload());
        } else {
            throw new IllegalStateException("No handler registered for payload type: " + event.getPayload().getClass());
        }
    }
}
