package ar.edu.itba.ss.utils.collision;

import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleCollision extends Collision {

    public ParticleCollision(double time, MovableSurfaceEntity<Particle> p1, MovableSurfaceEntity<Particle> p2) {
        super(time);

        this.particlesInvolved.add(p1);
        this.particlesInvolved.add(p2);
    }

    @Override
    public List<MovableSurfaceEntity<Particle>> computeCollision() {
        List<MovableSurfaceEntity<Particle>> collidingParticles = new ArrayList<>();

        if (this.particlesInvolved.size() == 2) {
            MovableSurfaceEntity<Particle> p1 = this.particlesInvolved.get(0);
            MovableSurfaceEntity<Particle> p2 = this.particlesInvolved.get(1);

            double dx = p2.getX() - p1.getX();
            double dy = p2.getY() - p1.getY();

            double dVx = p2.getXSpeed() - p1.getXSpeed();
            double dVy = p2.getYSpeed() - p1.getYSpeed();

            double dVdR = dx * dVx + dy * dVy;
            double dist = p1.getEntity().getRadius() + p2.getEntity().getRadius();

            double J = 2 * p1.getEntity().getMass() * p2.getEntity().getMass() * dVdR / ((p1.getEntity().getMass() + p2.getEntity().getMass()) * dist);
            double Jx = J * dx / dist;
            double Jy = J * dy / dist;

            double newP1XSpeed = p1.getXSpeed() + Jx / p1.getEntity().getMass();
            double newP1YSpeed = p1.getYSpeed() + Jy / p1.getEntity().getMass();
            double newP1Speed = Math.sqrt(Math.pow(newP1XSpeed, 2) + Math.pow(newP1YSpeed, 2));
            double newP1Angle = Math.atan2(newP1YSpeed, newP1XSpeed);
            collidingParticles.add(new MovableSurfaceEntity<>(p1.getEntity(), p1.getX(), p1.getY(), newP1Speed, newP1Angle));

            double newP2XSpeed = p2.getXSpeed() - Jx / p2.getEntity().getMass();
            double newP2YSpeed = p2.getYSpeed() - Jy / p2.getEntity().getMass();
            double newP2Speed = Math.sqrt(Math.pow(newP2XSpeed, 2) + Math.pow(newP2YSpeed, 2));
            double newP2Angle = Math.atan2(newP2YSpeed, newP2XSpeed);
            collidingParticles.add(new MovableSurfaceEntity<>(p2.getEntity(), p2.getX(), p2.getY(), newP2Speed, newP2Angle));
        }

        return collidingParticles;
    }
}
