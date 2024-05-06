package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.utils.AuxiliarMath;

import java.util.ArrayList;
import java.util.List;

public class MarsVoyage {

    private static final double GRAVITY_CONSTANT = 6.693 * Math.pow(10, -11);
    private static final double SUN_MASS = 1.989 * Math.pow(10, 30);
    private static final double EARTH_MASS = 5.972 * Math.pow(10, 24);
    private static final double MARS_MASS = 6.39 * Math.pow(10, 23);

    public static void main(String[] args) {
        List<Particle> particles = new ArrayList<>(); // Initialize particles
        for (Particle particle: particles) {
            List<Vector> forces = new ArrayList<>();
            for (Particle other:particles) {
                if(!particle.equals(other)){
                    forces.add(calculateForce(particle.getPosition().sub(other.getPosition()),
                            particle.getMass(),
                            other.getMass()));
                }
            }
            particle.setAcceleration(sumForces(forces).divide(particle.getMass()));
        }

    }

    private static Vector calculateForce(Vector position, double m1, double m2){
        double angle = position.angle();
        double forceModule = GRAVITY_CONSTANT* m1 * m2 / position.norm2();
        return new Vector(forceModule * Math.cos(angle), forceModule * Math.sin(angle));
    }

    private static Vector sumForces(List<Vector> forces){
        Vector totalForce = new Vector(0,0);
        for(Vector force : forces){
            totalForce = totalForce.sum(force);
        }
        return totalForce;
    }

}
