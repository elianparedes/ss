package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MolecularDynamicsAlgorithm implements Algorithm<MolecularDynamicsParameters> {

    public static List<MovableSurfaceEntity<Particle>> generateRandomParticles(double l, double n, double radius, double speed, double mass) {
        List<MovableSurfaceEntity<Particle>> particles = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            double x = Math.random() * l;
            double y = Math.random() * l;
            double angle = Math.random() * Math.PI * 2;

            boolean notValid;
            do {
                final double finalX = x;
                final double finalY = y;
                notValid = particles.stream().anyMatch(p -> Math.pow(finalX - p.getX(), 2) + Math.pow(finalY - p.getY(), 2) <= Math.pow(radius + p.getEntity().getRadius(), 2));

                if (notValid) {
                    x = Math.random() * l;
                    y = Math.random() * l;
                }
            } while (notValid);

            particles.add(new MovableSurfaceEntity<>(new Particle(radius, mass), x, y, speed, angle));
        }

        return particles;
    }

    private double getCollisionTime(MovableSurfaceEntity<Particle> current, MovableSurfaceEntity<Particle> other) {
        double sigma = current.getEntity().getRadius() + other.getEntity().getRadius();
        double[] dR = new double[]{other.getX() - current.getX(), other.getY() - current.getY()};
        double[] dV = new double[]{other.getXSpeed() - current.getXSpeed(), other.getYSpeed() - current.getYSpeed()};

        double dotProductDvDr = dR[0] * dV[0] + dR[1] * dV[1];
        double dotProductDrDr = dR[0] * dR[0] + dR[1] * dR[1];
        double dotProductDvDv = dV[0] * dV[0] + dV[1] * dV[1];

        double d = dotProductDvDr * dotProductDvDr - (dotProductDvDv * dotProductDvDr) * (dotProductDrDr * dotProductDrDr - sigma * sigma);

        if (dotProductDvDr >= 0 || d < 0) {
            return Double.MAX_VALUE;
        }

        return -1 * (dotProductDvDr + Math.sqrt(d)) / (dotProductDvDv);
    }

    private double getMinCollisionTime(Map<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> collisions) {
        double minCollisionTime = Double.MAX_VALUE;

        for (Map.Entry<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> entry : collisions.entrySet()) {
            Map<MovableSurfaceEntity<Particle>, Double> innerMap = entry.getValue();

            for (Map.Entry<MovableSurfaceEntity<Particle>, Double> innerEntry : innerMap.entrySet()) {
                double collisionTime = innerEntry.getValue();

                if (collisionTime < minCollisionTime) {
                    minCollisionTime = collisionTime;
                }
            }
        }

        return minCollisionTime;
    }

    private List<MovableSurfaceEntity<Particle>> getStateAt(final double t, final List<MovableSurfaceEntity<Particle>> initialState) {
        List<MovableSurfaceEntity<Particle>> newState = new ArrayList<>();

        for (MovableSurfaceEntity<Particle> particleInitialState : initialState) {
            double x = particleInitialState.getX();
            double y = particleInitialState.getY();

            double xt = particleInitialState.getXSpeed() * t;
            double yt = particleInitialState.getYSpeed() * t;

            double newX = x + xt;
            double newY = y + yt;

            MovableSurfaceEntity<Particle> newParticleState = new MovableSurfaceEntity<>(
                    particleInitialState.getEntity(),
                    newX, newY,
                    particleInitialState.getSpeed(), particleInitialState.getAngle()
            );

            newState.add(newParticleState);
        }

        return newState;
    }

    private List<MovableSurfaceEntity<Particle>> getStateAfterCollision(final List<MovableSurfaceEntity<Particle>> stateWhileCollision) {
        // TODO: Implement

        return new ArrayList<>();
    }

    @Override
    public void calculate(MolecularDynamicsParameters params, EventListener eventListener) {
        double time = 0;
        List<MovableSurfaceEntity<Particle>> state = params.particles;

        Map<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> collisions = new HashMap<>();

        for (MovableSurfaceEntity<Particle> current : params.particles) {
            for (MovableSurfaceEntity<Particle> other : params.particles) {
                Map<MovableSurfaceEntity<Particle>, Double> times = collisions.getOrDefault(current, new HashMap<>());
                times.put(current, getCollisionTime(current, other));
                collisions.putIfAbsent(current, times);
            }
        }

        // TODO: Maybe wrap all of this behaviour inside a Box class?
        double minCollisionTime = getMinCollisionTime(collisions);
        List<MovableSurfaceEntity<Particle>> stateWhileCollision = getStateAt(minCollisionTime, params.particles);
        List<MovableSurfaceEntity<Particle>> stateAfterCollision = getStateAfterCollision(stateWhileCollision);

    }
}
