package ar.edu.itba.ss.algorithms.gear;

import ar.edu.itba.ss.algorithms.AlgorithmState;
import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.Event;
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
        double time = 0;
        double mass = params.getMass();
        double[] alpha = params.getAlpha();

        for (int i = 0; i < params.getMaxIterations(); i++) {

            // Predict
            List<Vector> newR = new ArrayList<>(r);
            for (int j = 0; j < r.size(); j++) {
                for (int k = j; k < r.size(); k++) {
                    newR.set(j, newR.get(j).sum(r.get(k).multiply(factor(dt, k - j))));
                }
            }

            // Evaluate
            Vector futureA = force.apply(newR.get(0), newR.get(1)).divide(mass);
            Vector dA = futureA.sub(newR.get(2));
            Vector dR2 = dA.multiply(factor(dt, 2));

            // Correct
            for(int j = 0; j < r.size(); j++) {
                newR.set(j, newR.get(j).sum(dR2.multiply(alpha[j]).divide(factor(dt, j))));
            }

            eventListener.emit(new Event<>(new AlgorithmState(newR.get(5), time, params.getDt())));

            r = newR;
            time += dt;
        }
    }

    private double factor(double dt, int k){
        if(k == 0) return 1;
        return Math.pow(dt, k) / AuxiliarMath.factorial(k);
    }
}
