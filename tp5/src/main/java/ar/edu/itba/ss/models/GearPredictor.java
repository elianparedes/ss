package ar.edu.itba.ss.models;

import ar.edu.itba.ss.utils.AuxiliarMath;

import java.util.ArrayList;
import java.util.List;

public class GearPredictor {

    private double dt;
    private final double initialTime;

    private static final double[] ALPHAS = new double[]{(3.0 / 20.0), (251.0 / 360), 1.0, (11.0 / 18.0), (1.0 / 6.0), (1.0 / 60.0)};


    public GearPredictor(double dt) {
        this.dt = dt;
        this.initialTime = 0;
    }

    public GearPredictor(double dt, double initialTime) {
        this.dt = dt;
        this.initialTime = initialTime;
    }

    public void next(){
        this.dt += initialTime + dt;
    }

    public Particle predict(Particle particle){
        List<Vector> r = particle.getR();
        List<Vector> newR = new ArrayList<>(r);
        for (int j = 0; j < r.size(); j++) {
            for (int k = j + 1; k < r.size(); k++) {
                newR.set(j, newR.get(j).sum(r.get(k).multiply(factor(dt, k - j))));
            }
        }

        return new Particle(particle.getName(), particle.getMass(), particle.getRadius(), newR, particle.getDesiredSpeed(), particle.getTarget());
    }

    public Vector evaluate(Particle predicted, Particle future){
        Vector futureA = future.getAcceleration();
        Vector dA = futureA.sub(predicted.getAcceleration());

        return dA.multiply(factor(dt,2));
    }

    public Particle correct(Particle predicted, Vector dR2){
        List<Vector> r = predicted.getR();
        List<Vector> newR = new ArrayList<>(r);

        for (int k = 0; k < predicted.getR().size(); k++) {
            newR.set(k, newR.get(k).sum(dR2.multiply(ALPHAS[k]).divide(factor(dt, k))));
        }

        return new Particle(predicted.getName(), predicted.getMass(), predicted.getRadius(), newR, predicted.getDesiredSpeed(), predicted.getTarget());
    }

    public static Particle calculateFutureParticle(Particle particle, Vector force){

        Particle updated = new Particle(particle.getName(),particle.getMass(), particle.getRadius(), new ArrayList<>(particle.getR()), particle.getDesiredSpeed(), particle.getTarget());
        updated.setVelocity(particle.getVelocity());
        updated.setPosition(particle.getPosition());
        updated.setAcceleration(force.divide(particle.getMass()));

        return updated;
    }

    private static Vector sumForces(List<Vector> forces) {
        Vector totalForce = new Vector(0, 0);
        for (Vector force : forces) {
            totalForce = totalForce.sum(force);
        }
        return totalForce;
    }

    private static double factor(double dt, int k){
        if(k == 0) return 1;
        return Math.pow(dt, k) / AuxiliarMath.factorial(k);
    }
}
