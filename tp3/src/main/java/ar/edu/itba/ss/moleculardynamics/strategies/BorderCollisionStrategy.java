package ar.edu.itba.ss.moleculardynamics.strategies;

import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

public class BorderCollisionStrategy implements CollisionStrategy<Border> {
    @Override
    public Double handleCollision(MovableSurfaceEntity<Particle> particle, SurfaceEntity<Border> entity) {
        Border border = entity.getEntity();
        if (border.getP1().getX() == border.getP2().getX()) {
            double dirVect = border.getP1().getX() - particle.getX();
            if (particle.getXSpeed() * dirVect < 0) {
                return Double.MAX_VALUE;
            }

            double collisionTime = (border.getP1().getX() - Math.signum(dirVect) * particle.getEntity().getRadius() - particle.getX()) / particle.getXSpeed();

            if (collisionTime < 0)
                throw new RuntimeException(String.format("{ particle: %s, border: %s }", particle, border));

            return collisionTime;

        } else if (border.getP2().getY() == border.getP1().getY()) {
            double dirVect = border.getP1().getY() - particle.getY();
            if (particle.getYSpeed() * dirVect < 0) {
                return Double.MAX_VALUE;
            }

            double collisionTime = (border.getP1().getY() - Math.signum(dirVect) * particle.getEntity().getRadius() - particle.getY()) / particle.getYSpeed();

            if (collisionTime < 0)
                throw new RuntimeException(String.format("{ particle: %s, border: %s }", particle, border));

            return collisionTime;

        } else {
            throw new RuntimeException("Method do not handle non-orthogonal borders");
        }
    }
}
