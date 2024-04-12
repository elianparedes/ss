package ar.edu.itba.ss.moleculardynamics.strategies;

import ar.edu.itba.ss.utils.entity.Entity;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.HashMap;
import java.util.Map;

public class CollisionStrategiesHandler {
    private final Map<Class<? extends Entity>, CollisionStrategy<? extends Entity>> strategies = new HashMap<>();

    public CollisionStrategiesHandler() {
        strategies.put(Border.class, new BorderCollisionStrategy());
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> Double handle(MovableSurfaceEntity<Particle> particle, SurfaceEntity<T> entity, Class<? extends Entity> tClass) {
        CollisionStrategy<T> strategy = (CollisionStrategy<T>) strategies.get(tClass);
        if (strategy != null) {
            return strategy.handleCollision(particle, entity);
        } else {
            throw new RuntimeException("No handler implemented for class: " + entity.getClass().getName());
        }
    }
}
