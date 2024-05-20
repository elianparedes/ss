package ar.edu.itba.ss.models;

import java.util.List;

public class SocialForce {

    private static final double KN = 120000;
    
    private static final double A = 2000;

    private static final double B = 0.08;


    public static Vector calculateForce(Particle i, List<Particle> particles, double tau) {

        Vector granularForce = calculateGranularForce(i,particles);
        Vector socialForce = calculateSocialForce(i,particles);
        Vector drivingForce = i.getDesiredSpeedVector().sub(i.getVelocity()).multiply(i.getMass() / tau);

        return granularForce.sum(socialForce).sum(drivingForce);
    }

    private static Vector calculateGranularForce(Particle i, List<Particle> particles){
        Vector granularForce = new Vector(0,0);

        for (Particle j : particles) {
            if(!j.equals(i)){
                Vector normal = j.getPosition().sub(i.getPosition());
                double epsilon = normal.norm() - (i.getRadius()+j.getRadius());
                normal = normal.divide(normal.norm());

                if(epsilon > 0){
                    granularForce.sum(normal.multiply(-1*epsilon*KN));
                }

            }
        }

        return granularForce;
    }

    private static Vector calculateSocialForce(Particle i, List<Particle> particles){

        Vector socialForce = new Vector(0,0);

        for (Particle j : particles) {
            if(!j.equals(i)){
                Vector normal = j.getPosition().sub(i.getPosition());
                double epsilon = normal.norm();
                normal = normal.divide(normal.norm());

                if(epsilon > 0){
                    socialForce.sum(normal.multiply(A*Math.exp(-1*epsilon/B)));
                }

            }
        }

        return socialForce;
    }
}
