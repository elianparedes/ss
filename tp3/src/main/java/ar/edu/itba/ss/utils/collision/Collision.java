package ar.edu.itba.ss.utils.collision;

import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Ball;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.ArrayList;
import java.util.List;

public abstract class Collision implements Comparable<Collision> {

    double time;

    SurfaceEntity<Border> border;
    MovableSurfaceEntity<Particle> ball;

    List<MovableSurfaceEntity<Particle>> particlesInvolved;

    public Collision(double time) {
        this.time = time;
        this.particlesInvolved = new ArrayList<>();
    }

    public static BallCollision predictCollision(MovableSurfaceEntity<Particle> ball, MovableSurfaceEntity<Particle> particle, boolean movable) {
        double dx = ball.getX() - particle.getX();
        double dy = ball.getY() - particle.getY();

        double dVx = ball.getXSpeed() - particle.getXSpeed();
        double dVy = ball.getYSpeed() - particle.getYSpeed();

        double dVdR = dx * dVx + dy * dVy;
        if (dVdR > 0) return new BallCollision(Double.MAX_VALUE, particle, ball,movable);

        double dVdV = dVx * dVx + dVy * dVy;
        if (dVdV == 0) return new BallCollision(Double.MAX_VALUE, particle, ball,movable);

        double dRdR = dx * dx + dy * dy;

        double sigma = particle.getEntity().getRadius() + ball.getEntity().getRadius();
        double d = (dVdR * dVdR) - dVdV * (dRdR - sigma * sigma);

        if (d < 0) return new BallCollision(Double.MAX_VALUE, particle, ball,movable);

        return new BallCollision(-(dVdR + Math.sqrt(d)) / dVdV, particle, ball,movable);
    }

    public static WallCollision predictCollision(MovableSurfaceEntity<Particle> particle, SurfaceEntity<Border> b) {
        double collisionTime = Double.MAX_VALUE;
        Border border = b.getEntity();

        if (border.getP1().getX() == border.getP2().getX()) {
            // Vertical Wall
            if (particle.getXSpeed() > 0) {
                double l = (border.getP2().getY() - border.getP1().getY());

                collisionTime = (l - particle.getX() - particle.getEntity().getRadius()) / particle.getXSpeed();
            }
            else if (particle.getXSpeed() < 0)
                collisionTime =  (particle.getEntity().getRadius() - particle.getX()) / particle.getXSpeed();


        } else if (border.getP2().getY() == border.getP1().getY()) {
            // Horizontal Wall
            if (particle.getYSpeed() > 0) {
                double l = (border.getP2().getX() - border.getP1().getX());
                collisionTime = (l - particle.getY() - particle.getEntity().getRadius()) / particle.getYSpeed();
            }
            else if (particle.getYSpeed() < 0)
                collisionTime = (particle.getEntity().getRadius() - particle.getY()) / particle.getYSpeed();
        }

        return new WallCollision(collisionTime, particle, b);
    }

    public static ParticleCollision predictCollision(MovableSurfaceEntity<Particle> current, MovableSurfaceEntity<Particle> other) {
        if (current == other) return new ParticleCollision(Double.MAX_VALUE, current, other);

        double dx = other.getX() - current.getX();
        double dy = other.getY() - current.getY();

        double dVx = other.getXSpeed() - current.getXSpeed();
        double dVy = other.getYSpeed() - current.getYSpeed();

        double dVdR = dx * dVx + dy * dVy;
        if (dVdR > 0) return new ParticleCollision(Double.MAX_VALUE, current, other);

        double dVdV = dVx * dVx + dVy * dVy;
        if (dVdV == 0) return new ParticleCollision(Double.MAX_VALUE, current, other);

        double dRdR = dx * dx + dy * dy;

        double sigma = current.getEntity().getRadius() + other.getEntity().getRadius();
        double d = (dVdR * dVdR) - dVdV * (dRdR - sigma * sigma);

        if (d < 0) return new ParticleCollision(Double.MAX_VALUE, current, other);

        return new ParticleCollision(-(dVdR + Math.sqrt(d)) / dVdV, current, other);
    }

    public abstract List<MovableSurfaceEntity<Particle>> computeCollision();

    @Override
    public int compareTo(Collision o) {
        return Double.compare(this.time, o.time);
    }

    public double getTime() {
        return time;
    }

    public List<MovableSurfaceEntity<Particle>> getParticlesInvolved() {
        return particlesInvolved;
    }

    @Override
    public String toString() {
        return "Collision{" +
                "time=" + time +
                '}';
    }
}

