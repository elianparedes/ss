package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.moleculardynamics.strategies.CollisionStrategiesHandler;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.utils.collision.Collision;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

import java.math.BigDecimal;
import java.util.*;

public class MolecularDynamicsAlgorithm implements Algorithm<MolecularDynamicsParameters> {

    public static List<MovableSurfaceEntity<Particle>> generateRandomParticles(double l, double n, double radius, double speed, double mass) {
        List<MovableSurfaceEntity<Particle>> particles = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            double x, y;
            boolean notValid;
            do {
                x = Math.random() * (l - 2 * radius) + radius;
                y = Math.random() * (l - 2 * radius) + radius;

                final double finalX = x;
                final double finalY = y;
                notValid = particles.stream().anyMatch(p ->
                        Math.pow(finalX - p.getX(), 2) + Math.pow(finalY - p.getY(), 2) <= Math.pow(radius + p.getEntity().getRadius(), 2));
            } while (notValid);

            particles.add(new MovableSurfaceEntity<>(new Particle(radius, mass), x, y, speed, Math.random() * Math.PI * 2));
        }

        return particles;
    }

    private List<MovableSurfaceEntity<Particle>> evolveState(final double t, final List<MovableSurfaceEntity<Particle>> initialState) {
        List<MovableSurfaceEntity<Particle>> newState = new ArrayList<>();

        for (MovableSurfaceEntity<Particle> particle : initialState) {
            double x = particle.getX();
            double y = particle.getY();

            double vx = particle.getXSpeed();
            double vy = particle.getYSpeed();

            x = x + vx * t;
            y = y + vy * t;

            newState.add(new MovableSurfaceEntity<>(particle.getEntity(), x, y, particle.getSpeed(), particle.getAngle()));
        }

        return newState;
    }

    private List<MovableSurfaceEntity<Particle>> collideState(List<MovableSurfaceEntity<Particle>> currentState, List<MovableSurfaceEntity<Particle>> collidingParticles) {
        Map<Integer, MovableSurfaceEntity<Particle>> particleMap = new HashMap<>();

        for (MovableSurfaceEntity<Particle> particle : currentState) {
            particleMap.put(particle.getEntity().getId(), particle);
        }

        for (MovableSurfaceEntity<Particle> collidingParticle : collidingParticles) {
            particleMap.put(collidingParticle.getEntity().getId(), collidingParticle);
        }

        return new ArrayList<>(particleMap.values());
    }

    private Queue<Collision> predictImminentCollisions(List<MovableSurfaceEntity<Particle>> state, List<SurfaceEntity<Border>> borders) {
        Queue<Collision> imminentCollisions = new PriorityQueue<>();

        for (MovableSurfaceEntity<Particle> current : state) {
            for (MovableSurfaceEntity<Particle> other : state) {
                imminentCollisions.add(Collision.predictCollision(current, other));
            }

            for (SurfaceEntity<Border> border : borders) {
                imminentCollisions.add(Collision.predictCollision(current, border));
            }

        }

        return imminentCollisions;
    }

    @Override
    public void calculate(MolecularDynamicsParameters params, EventListener eventListener) {
        List<MovableSurfaceEntity<Particle>> currentState = params.particles;
        Queue<Collision> imminentCollisions = predictImminentCollisions(currentState, params.fixedObjects);

        int i = 0;
        double t = 0;
        while (i < params.maxIterations) {
            if (!imminentCollisions.isEmpty()) {
                Collision imminentCollision = imminentCollisions.poll();

                double dt = imminentCollision.getTime();
                List<MovableSurfaceEntity<Particle>> stateBeforeCollision = evolveState(dt, currentState);
                t += dt;

                List<MovableSurfaceEntity<Particle>> collidingParticles = imminentCollision.computeCollision();
                currentState = collideState(stateBeforeCollision, collidingParticles);

                imminentCollisions = predictImminentCollisions(currentState, params.fixedObjects);
                eventListener.emit(new Event<>(new MolecularDynamicsState(currentState, t)));
                i++;
            }
        }
    }
}
