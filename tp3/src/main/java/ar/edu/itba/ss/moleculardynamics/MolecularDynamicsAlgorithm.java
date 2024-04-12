package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.moleculardynamics.strategies.CollisionStrategiesHandler;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.utils.entity.Entity;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                        Math.pow(finalX - p.getX(), 2) + Math.pow(finalY - p.getY(), 2) <= Math.pow(radius + p.getEntity().getRadius(), 2))
                        || x - radius < 0 || x + radius > l || y - radius < 0 || y + radius > l;
            } while (notValid);

            particles.add(new MovableSurfaceEntity<>(new Particle(radius, mass), x, y, speed, Math.random() * Math.PI * 2));
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

        double d = dotProductDvDr * dotProductDvDr - dotProductDvDv * (dotProductDrDr - sigma * sigma);

        if (dotProductDvDr >= 0 || d < 0) {
            return Double.MAX_VALUE;
        }

        return -1 * (dotProductDvDr + Math.sqrt(d)) / (dotProductDvDv);
    }

    private CollisionState getMinCollisionTime(Map<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> collisions) {
        double minCollisionTime = Double.MAX_VALUE;
        CollisionState collisionState = new CollisionState(minCollisionTime, null, null);

        for (Map.Entry<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> entry : collisions.entrySet()) {
            Map<MovableSurfaceEntity<Particle>, Double> innerMap = entry.getValue();

            for (Map.Entry<MovableSurfaceEntity<Particle>, Double> innerEntry : innerMap.entrySet()) {
                double collisionTime = innerEntry.getValue();

                if (collisionTime < minCollisionTime) {
                    minCollisionTime = collisionTime;
                    collisionState.p1 = entry.getKey();
                    collisionState.p2 = innerEntry.getKey();
                    collisionState.time = minCollisionTime;
                }
            }
        }

        return collisionState;
    }

    private CollisionWithFixedState getMinCollisionTimeWithFixed(Map<MovableSurfaceEntity<Particle>, Map<SurfaceEntity<Border>, Double>> collisionsWithFixed) {
        double minCollisionTime = Double.MAX_VALUE;
        CollisionWithFixedState collisionWithFixedState = new CollisionWithFixedState(minCollisionTime, null, null);

        for (Map.Entry<MovableSurfaceEntity<Particle>, Map<SurfaceEntity<Border>, Double>> entry : collisionsWithFixed.entrySet()) {
            Map<SurfaceEntity<Border>, Double> innerMap = entry.getValue();

            for (Map.Entry<SurfaceEntity<Border>, Double> innerEntry : innerMap.entrySet()) {
                double collisionTime = innerEntry.getValue();

                if (collisionTime < minCollisionTime) {
                    minCollisionTime = collisionTime;
                    collisionWithFixedState.p = entry.getKey();
                    collisionWithFixedState.fixed = innerEntry.getKey();
                    collisionWithFixedState.time = minCollisionTime;
                }
            }
        }

        return collisionWithFixedState;
    }

    private List<MovableSurfaceEntity<Particle>> evolveState(final double t, final List<MovableSurfaceEntity<Particle>> initialState) {
        List<MovableSurfaceEntity<Particle>> newState = new ArrayList<>();

        for (MovableSurfaceEntity<Particle> particleInitialState : initialState) {
            newState.add(MovableSurfaceEntity.uniformLinearMotion(particleInitialState, t));
        }

        return newState;
    }

    private void calculateCollisionsBetweenParticles(Map<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> collisions,
                                                     List<MovableSurfaceEntity<Particle>> toUpdateParticles, List<MovableSurfaceEntity<Particle>> allParticles,
                                                     double currentTime) {

        for (MovableSurfaceEntity<Particle> current : toUpdateParticles) {
            for (MovableSurfaceEntity<Particle> other : allParticles) {
                if(!current.equals(other)) {
                    Map<MovableSurfaceEntity<Particle>, Double> times = collisions.getOrDefault(current, new HashMap<>());
                    times.put(other, getCollisionTime(current, other) + currentTime);
                    collisions.putIfAbsent(current, times);
                }
            }
        }
    }

    private void calculateCollisionsWithFixedObjects(Map<MovableSurfaceEntity<Particle>, Map<SurfaceEntity<Border>, Double>> collisions,
                                                     List<MovableSurfaceEntity<Particle>> toUpdateParticles, List<SurfaceEntity<Border>> fixedObjects,
                                                     double currentTime) {
        CollisionStrategiesHandler handler = new CollisionStrategiesHandler();

        for (MovableSurfaceEntity<Particle> current : toUpdateParticles) {
            for (SurfaceEntity<Border> fixedObject : fixedObjects) {
                Map<SurfaceEntity<Border>, Double> times = collisions.getOrDefault(current, new HashMap<>());
                times.put(fixedObject, handler.handle(current, fixedObject, fixedObject.getEntity().getClass()) + currentTime);
                collisions.putIfAbsent(current, times);
            }
        }
    }

    @Override
    public void calculate(MolecularDynamicsParameters params, EventListener eventListener) {

        //Between Particles
        Map<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> collisions = new HashMap<>();

        //Between particles and fixed objects
        Map<MovableSurfaceEntity<Particle>, Map<SurfaceEntity<Border>, Double>> collisionsWithFixed = new HashMap<>();

        //Current time is added to updated collisions
        double currentTime = 0;

        //Get all collisions between particles
        calculateCollisionsBetweenParticles(collisions, params.particles, params.particles, currentTime);

        //Get all collisions between particles and fixed objects
        calculateCollisionsWithFixedObjects(collisionsWithFixed, params.particles, params.fixedObjects, currentTime);

        List<MovableSurfaceEntity<Particle>> newState = params.particles;

        double previousTime = 0;

        while (currentTime < params.maxIterations) {
            CollisionState collisionState = getMinCollisionTime(collisions);
            CollisionWithFixedState collisionWithFixedState = getMinCollisionTimeWithFixed(collisionsWithFixed);

            currentTime = Math.min(collisionState.time, collisionWithFixedState.time);

            List<MovableSurfaceEntity<Particle>> toUpdateParticles = new ArrayList<>();

            List<MovableSurfaceEntity<Particle>> stateBefore = evolveState(currentTime-previousTime, newState);

            if (currentTime == collisionState.time) {
                //TODO: Maybe emit an event with the stateBefore

                MovableSurfaceEntity<Particle> p1Before = null;
                for (MovableSurfaceEntity<Particle> p : stateBefore) {
                    if (p.getEntity().getId().equals(collisionState.p1.getEntity().getId())) {
                        p1Before = p;
                        break;  // Detiene el bucle una vez que se encuentra el elemento
                    }
                }

                if (p1Before != null) {
                    // Elemento encontrado
                } else {
                    // Elemento no encontrado
                }

                MovableSurfaceEntity<Particle> p2Before = null;
                for (MovableSurfaceEntity<Particle> p : stateBefore) {
                    if (p.getEntity().getId().equals(collisionState.p2.getEntity().getId())) {
                        p2Before = p;
                        break;  // Detiene el bucle una vez que se encuentra el elemento
                    }
                }

                if (p2Before != null) {
                    // Elemento encontrado
                } else {
                    // Elemento no encontrado
                }

                double sigma = collisionState.p1.getEntity().getRadius() + collisionState.p1.getEntity().getRadius();

                double dX = collisionState.p2.getX() - collisionState.p1.getX();
                double dY = collisionState.p2.getY() - collisionState.p1.getY();

                double[] dR = new double[]{dX,dY};
                double[] dV = new double[]{collisionState.p2.getXSpeed() - collisionState.p1.getXSpeed(), collisionState.p2.getYSpeed() - collisionState.p1.getYSpeed()};

                double dotProductDvDr = dR[0] * dV[0] + dR[1] * dV[1];

                System.out.println("collStateP1: " + collisionState.p1);
                System.out.println("collStateP2: " + collisionState.p2);

                //Update involved particles speeds
                MovableSurfaceEntity<Particle> p1After = MovableSurfaceEntity.collisionMotion(p1Before, p2Before , dotProductDvDr,sigma,dX,dY);
                //The order change
                MovableSurfaceEntity<Particle> p2After = MovableSurfaceEntity.collisionMotion(p2Before, p1Before , dotProductDvDr,sigma,-1*dX,-1*dY);

                collisions.remove(p1After);
                collisions.remove(p2After);
                collisionsWithFixed.remove(p1After);
                collisionsWithFixed.remove(p2After);

                //Re-calculate collisions between all particles and the recently changed particles.
                toUpdateParticles.add(p1After);
                toUpdateParticles.add(p2After);

                System.out.println("p1after: " + p1After);
                System.out.println("p2After: " + p2After);

            } else if (currentTime == collisionWithFixedState.time) {
                MovableSurfaceEntity<Particle> pBefore = null;

                for (MovableSurfaceEntity<Particle> p: newState) {
                    if(p.getEntity().getId().equals(collisionWithFixedState.p.getEntity().getId())){
                        System.out.println(p);
                        break;
                    }
                }

                System.out.println("stateFixed: "+ collisionWithFixedState.p);
                for (MovableSurfaceEntity<Particle> p : stateBefore) {
                    if (p.getEntity().getId().equals(collisionWithFixedState.p.getEntity().getId())) {
                        pBefore = p;
                        break;  // Detiene el bucle una vez que se encuentra el elemento
                    }
                }

                if (pBefore != null) {
                    // Elemento encontrado
                } else {
                    // Elemento no encontrado
                }
                System.out.println("pBefore: " + pBefore);
                MovableSurfaceEntity<Particle> pAfter = MovableSurfaceEntity.collisionWithFixed(pBefore, collisionWithFixedState.fixed);

                System.out.println("pAfter: " + pAfter);
                collisionsWithFixed.remove(pAfter);
                collisions.remove(pAfter);

                toUpdateParticles.add(pAfter);
            }

            newState = stateBefore.stream()
                    .map(particle -> {
                        // Buscar una partícula correspondiente en toUpdateParticles
                        Optional<MovableSurfaceEntity<Particle>> matchingParticle = toUpdateParticles.stream()
                                .filter(p -> Objects.equals(p.getEntity().getId(), particle.getEntity().getId()))
                                .findFirst();

                        // Si se encuentra, devolver la partícula actualizada; de lo contrario, devolver la original
                        return matchingParticle.orElse(particle);
                    })
                    .collect(Collectors.toList());

            calculateCollisionsBetweenParticles(collisions, toUpdateParticles, params.particles, currentTime);
            calculateCollisionsWithFixedObjects(collisionsWithFixed, toUpdateParticles, params.fixedObjects, currentTime);

            previousTime = currentTime;
        }
    }

    private static class CollisionState {
        public double time;
        public MovableSurfaceEntity<Particle> p1;
        public MovableSurfaceEntity<Particle> p2;

        public CollisionState(double time, MovableSurfaceEntity<Particle> p1, MovableSurfaceEntity<Particle> p2) {
            this.time = time;
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    private static class CollisionWithFixedState {
        public double time;
        public MovableSurfaceEntity<Particle> p;
        public SurfaceEntity<Border> fixed;

        public CollisionWithFixedState(double time, MovableSurfaceEntity<Particle> p, SurfaceEntity<Border> fixed) {
            this.time = time;
            this.p = p;
            this.fixed = fixed;
        }

    }
}
