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
        double time;
        double mass = params.getMass();
        double[] alpha = params.getAlpha();

        for (int i = 0; i < params.getMaxIterations(); i++) {
            time = i*dt;
            eventListener.emit(new Event<>(new AlgorithmState(r.get(0), time, params.getDt())));
            r = GearAlgorithmStep.calculateStep(dt,r,force,mass,alpha);
        }
    }
}
