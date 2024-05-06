package ar.edu.itba.ss.algorithms.gear;

import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.utils.AuxiliarMath;

import java.util.ArrayList;
import java.util.List;

public class GearAlgorithmStep {
    public static List<Vector> calculateStep(double dt, List<Vector> r, Force force, double mass, double[] alpha){

        // Predict
        List<Vector> newR = new ArrayList<>(r);
        for (int j = 0; j < r.size(); j++) {
            for (int k = j+1; k < r.size(); k++) {
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

        return newR;
    }

    public static double factor(double dt, int k){
        if(k == 0) return 1;
        return Math.pow(dt, k) / AuxiliarMath.factorial(k);
    }
}
