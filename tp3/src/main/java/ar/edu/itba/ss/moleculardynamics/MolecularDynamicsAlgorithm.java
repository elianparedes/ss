package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.*;

public class MolecularDynamicsAlgorithm implements Algorithm<MolecularDynamicsParameters> {

    public static List<MovableSurfaceEntity<Particle>> generateRandomParticles(double l, double n, double radius, double speed, double mass){
        List<MovableSurfaceEntity<Particle>> particles = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            double x = Math.random() * l;
            double y = Math.random() * l;
            double angle = Math.random()*Math.PI*2;

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

            particles.add(new MovableSurfaceEntity<>(new Particle(radius,mass),x,y,speed, angle));
        }

        return particles;
    }

    private double getCollisionTime(MovableSurfaceEntity<Particle> current, MovableSurfaceEntity<Particle> other){
        double sigma = current.getEntity().getRadius() + other.getEntity().getRadius();
        double[] dR= new double[]{other.getX() - current.getX(),other.getY() - current.getY()};
        double[] dV= new double[]{other.getXSpeed()- current.getXSpeed(),other.getYSpeed() - current.getYSpeed()};

        double dotProductDvDr = dR[0]*dV[0] + dR[1]*dV[1];
        double dotProductDrDr = dR[0]*dR[0] + dR[1]*dR[1];
        double dotProductDvDv = dV[0]*dV[0] + dV[1]*dV[1];

        double d = dotProductDvDr*dotProductDvDr - (dotProductDvDv*dotProductDvDr)*(dotProductDrDr*dotProductDrDr-sigma*sigma);

        if(dotProductDvDr >= 0 || d < 0){
            return Double.MAX_VALUE;
        }

        return -1*(dotProductDvDr + Math.sqrt(d))/(dotProductDvDv);
    }

    private void getFirstCollision(Map<MovableSurfaceEntity<Particle>,Map<MovableSurfaceEntity<Particle>,Double>>  collisions){

        //TODO: Revisar
        MovableSurfaceEntity<Particle> minParticle1 = null;
        MovableSurfaceEntity<Particle> minParticle2 = null;
        double minTime= Double.MAX_VALUE;

        for (Map.Entry<MovableSurfaceEntity<Particle>, Map<MovableSurfaceEntity<Particle>, Double>> entry : collisions.entrySet()) {
            MovableSurfaceEntity<Particle> particle1 = entry.getKey();
            Map<MovableSurfaceEntity<Particle>, Double> innerMap = entry.getValue();

            for (Map.Entry<MovableSurfaceEntity<Particle>, Double> innerEntry : innerMap.entrySet()) {
                MovableSurfaceEntity<Particle> particle2 = innerEntry.getKey();
                double distance = innerEntry.getValue();

                if (distance < minTime) {
                    minTime = distance;
                    minParticle1 = particle1;
                    minParticle2 = particle2;
                }
            }
        }

    }

    @Override
    public void calculate(MolecularDynamicsParameters params, EventListener eventListener) {
        Map<MovableSurfaceEntity<Particle>,Map<MovableSurfaceEntity<Particle>,Double>> collisions = new HashMap<>();

        for (MovableSurfaceEntity<Particle> current:params.particles) {
            for (MovableSurfaceEntity<Particle> other: params.particles) {
                Map<MovableSurfaceEntity<Particle>,Double> times = collisions.getOrDefault(current,new HashMap<>());
                times.put(current,getCollisionTime(current,other));
                collisions.putIfAbsent(current,times);
            }
        }

        //TODO: getFirstCollision (Particles envolved and time)
    }
}
