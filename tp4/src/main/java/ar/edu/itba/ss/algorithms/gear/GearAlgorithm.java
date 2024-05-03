package ar.edu.itba.ss.algorithms.gear;

import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.utils.AuxiliarMath;

import java.util.ArrayList;
import java.util.List;

public class GearAlgorithm implements Algorithm<GearParameters> {
    @Override
    public void calculate(GearParameters params, EventListener eventListener) {

        List<Vector> r = params.getR();
        Force force = params.getForce();

        double dt = params.getDt();
        double t = 0;
        double mass = params.getMass();
        double[] alpha = params.getAlpha();

        for (int i = 0; i < params.getMaxIterations(); i++) {

            // Predict
            List<Vector> newR = new ArrayList<>(r);
            for (int j = 0; j < r.size(); j++) {
                for (int k = j; k < r.size(); k++) {
                    newR.set(j, r.get(j).multiply(factor(dt, k)));
                }
            }

            // Evaluate
            Vector dR = (force.apply(r.get(0), r.get(1).divide(mass))).sub(r.get(2).multiply(factor(dt, 2)));

            // Correct
            for(int j = 0; j < r.size(); j++)
                newR.set(j, newR.get(j).sum(alpha.get(j).multiply(dR).divide(factor(dt, j))));

            r = newR;
            t += dt;
        }
    }

    private double factor(double dt, int k){
        if(k == 0) return 1;
        return Math.pow(dt, k) / AuxiliarMath.factorial(k);
    }
}
