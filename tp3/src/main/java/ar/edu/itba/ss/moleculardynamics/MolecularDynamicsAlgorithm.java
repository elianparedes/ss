package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private CollisionState getMinCollisionTime(Map<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> collisions) {
        double minCollisionTime = Double.MAX_VALUE;
        CollisionState collisionState = new CollisionState(minCollisionTime,null,null);

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

    private List<MovableSurfaceEntity<Particle>> evolveState(final double t, final List<MovableSurfaceEntity<Particle>> initialState) {
        List<MovableSurfaceEntity<Particle>> newState = new ArrayList<>();

        for (MovableSurfaceEntity<Particle> particleInitialState : initialState) {
            newState.add(MovableSurfaceEntity.uniformLinearMotion(particleInitialState,t));
        }

        return newState;
    }

    private void calculateCollisionsBetweenParticles(Map<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> collisions,
                                               List<MovableSurfaceEntity<Particle>> toUpdateParticles,List<MovableSurfaceEntity<Particle>> allParticles,
                                                     double currentTime){

        for (MovableSurfaceEntity<Particle> current: toUpdateParticles) {
            for (MovableSurfaceEntity<Particle> other:allParticles) {
                Map<MovableSurfaceEntity<Particle>, Double> times = collisions.getOrDefault(current, new HashMap<>());
                times.put(current, getCollisionTime(current, other) + currentTime);
                collisions.putIfAbsent(current, times);
            }
        }
    }

    @Override
    public void calculate(MolecularDynamicsParameters params, EventListener eventListener) {
        Map<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> collisions = new HashMap<>();

        //Current time is added to updated collisions
        double currentTime = 0;

        //Get all collisions between particles
        calculateCollisionsBetweenParticles(collisions,params.particles,params.particles,currentTime);

        //Get all collisions between particles and fixed objects
        //TODO

        // TODO: Maybe wrap all of this behaviour inside a Box class?


        //Get all minTime for each collision case
        //TODO
        CollisionState collisionState = getMinCollisionTime(collisions);

        //Take the lower time of all collision cases
        //TODO
        double time = collisionState.time;
        currentTime = time;

        List<MovableSurfaceEntity<Particle>> stateBefore = evolveState(time, params.particles);

        //TODO: Maybe emit an event with the stateBefore

        //Update involved particles speeds
        MovableSurfaceEntity<Particle> p1After = MovableSurfaceEntity.collisionMotion(collisionState.p1,collisionState.p2);
        //The order change
        MovableSurfaceEntity<Particle> p2After = MovableSurfaceEntity.collisionMotion(collisionState.p2,collisionState.p1);

        //To keep immutability, we create new list for new state
        List<MovableSurfaceEntity<Particle>> stateAfter = stateBefore.stream()
                .flatMap(particle -> {
                    if (particle.getEntity().getId().equals(p1After.getEntity().getId()) ) {
                        return Stream.of(p1After);
                    } else if (particle.getEntity().getId().equals(p2After.getEntity().getId())) {
                        return Stream.of(p2After);
                    } else {
                        return Stream.of(particle);
                    }
                })
                .collect(Collectors.toList());


        //Re-calculate collisions between all particles and the recently changed particles.

        List<MovableSurfaceEntity<Particle>> toUpdateParticles = new ArrayList<>();
        toUpdateParticles.add(p1After);
        toUpdateParticles.add(p2After);

        calculateCollisionsBetweenParticles(collisions,toUpdateParticles,params.particles,currentTime);
    }

    private static class CollisionState{
        private  double time;
        private  MovableSurfaceEntity<Particle> p1;
        private MovableSurfaceEntity<Particle> p2;

        public CollisionState(double time, MovableSurfaceEntity<Particle> p1, MovableSurfaceEntity<Particle> p2) {
            this.time = time;
            this.p1 = p1;
            this.p2 = p2;
        }
    }
}
