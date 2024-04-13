package ar.edu.itba.ss.utils.collision;

import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public abstract class Collision implements Comparable<Collision> {

    double time;

    SurfaceEntity<Border> border;

    List<MovableSurfaceEntity<Particle>> particlesInvolved;

    public Collision(double time) {
        this.time = time;
        this.particlesInvolved = new ArrayList<>();
    }

    public abstract List<MovableSurfaceEntity<Particle>> computeCollision();

    @Override
    public int compareTo(Collision o) {
        return (int) (this.time - o.time);
    }

    public double getTime() {
        return time;
    }

    public List<MovableSurfaceEntity<Particle>> getParticlesInvolved() {
        return particlesInvolved;
    }

    public static WallCollision predictCollision(MovableSurfaceEntity<Particle> particle, SurfaceEntity<Border> b) {
        double collisionTime = Double.MAX_VALUE;

        Border border = b.getEntity();
        if (border.getP1().getX() == border.getP2().getX()) {
            double dirVect = border.getP1().getX() - particle.getX();
            dirVect += dirVect < 0 ? particle.getEntity().getRadius() : -1 * particle.getEntity().getRadius();

            if (particle.getXSpeed() * dirVect >= 0) {
                collisionTime = (border.getP1().getX() - Math.signum(dirVect) * particle.getEntity().getRadius() - particle.getX()) / particle.getXSpeed();
            }

        } else if (border.getP2().getY() == border.getP1().getY()) {
            double dirVect = border.getP1().getY() - particle.getY();
            dirVect += dirVect < 0 ? particle.getEntity().getRadius() : -1 * particle.getEntity().getRadius();

            if (particle.getYSpeed() * dirVect >= 0) {
                collisionTime = (border.getP1().getY() - Math.signum(dirVect) * particle.getEntity().getRadius() - particle.getY()) / particle.getYSpeed();
            }
        }

        return new WallCollision(collisionTime, particle, b);
    }


    public static ParticleCollision predictCollision(MovableSurfaceEntity<Particle> current, MovableSurfaceEntity<Particle> other) {
        double collisionTime = Double.MAX_VALUE;

        double sigma = current.getEntity().getRadius() + other.getEntity().getRadius();
        double[] dR = new double[]{other.getX() - current.getX(), other.getY() - current.getY()};
        double[] dV = new double[]{other.getXSpeed() - current.getXSpeed(), other.getYSpeed() - current.getYSpeed()};

        double dotProductDvDr = dR[0] * dV[0] + dR[1] * dV[1];
        double dotProductDrDr = dR[0] * dR[0] + dR[1] * dR[1];
        double dotProductDvDv = dV[0] * dV[0] + dV[1] * dV[1];

        double d = dotProductDvDr * dotProductDvDr - dotProductDvDv * (dotProductDrDr - sigma * sigma);

        if (dotProductDvDr < 0 && d > 0) {
            collisionTime = -1 * (dotProductDvDr + Math.sqrt(d)) / (dotProductDvDv);
        }

        return new ParticleCollision(collisionTime, current, other);
    }

    @Override
    public String toString() {
        return "Collision{" +
                "time=" + time +
                '}';
    }
}

