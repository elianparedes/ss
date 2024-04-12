package ar.edu.itba.ss.moleculardynamics.strategies;

import ar.edu.itba.ss.utils.entity.Entity;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Particle;

public interface CollisionStrategy<T extends Entity> {
    Double handleCollision(MovableSurfaceEntity<Particle> particle, SurfaceEntity<T> entity);
}
