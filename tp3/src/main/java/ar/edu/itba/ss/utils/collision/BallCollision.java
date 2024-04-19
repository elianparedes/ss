package ar.edu.itba.ss.utils.collision;

import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Ball;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class BallCollision extends Collision {
    public BallCollision(double time, MovableSurfaceEntity<Particle> particle, SurfaceEntity<Ball> ball) {
        super(time);

        this.particlesInvolved.add(particle);
        this.ball = ball;
    }

    @Override
    public List<MovableSurfaceEntity<Particle>> computeCollision() {
        List<MovableSurfaceEntity<Particle>> collidingParticles = new ArrayList<>();

        if (this.particlesInvolved.size() == 1) {
            MovableSurfaceEntity<Particle> particle = this.particlesInvolved.get(0);

            double angle = particle.getAngle();
            double vx = particle.getXSpeed();
            double vy = particle.getYSpeed();
            double cos = Math.cos(angle);
            double sen = Math.sin(angle);
            double cn = 1;
            double ct = 1;

            double newXSpeed = (-1 * cn * cos * cos + ct * sen * sen) * vx - (cn + ct) * sen * cos * vy;
            double newYSpeed = -1 * (cn + ct) * sen * cos * vx + (-1 * cn * sen * sen + ct * cos * cos) * vy;
            double newSpeed = Math.sqrt(Math.pow(newXSpeed, 2) + Math.pow(newYSpeed, 2));
            double newAngle = Math.atan2(newYSpeed, newXSpeed);

            collidingParticles.add(new MovableSurfaceEntity<>(particle.getEntity(), particle.getX(), particle.getY(), newSpeed, newAngle));
        }

        return  collidingParticles;

    }
}
