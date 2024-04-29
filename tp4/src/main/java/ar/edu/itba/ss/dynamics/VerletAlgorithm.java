package ar.edu.itba.ss.dynamics;

import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventListener;

import java.math.BigDecimal;

public class VerletAlgorithm implements Algorithm<VerletParameters> {
    @Override
    public void calculate(VerletParameters params, EventListener eventListener) {
        double time;
        Vector currentPos = params.getCurrent();
        Vector previousPos = params.getPrevious();
        Vector currentSpeed = params.getInitialSpeed();

        for (int i = 0; i < params.getMaxIterations(); i++) {
            time = params.getDt() * i;

            Vector nextPos = currentPos.multiply(2).sub(previousPos)
                    .sum(params.getForce().apply(currentPos, currentSpeed).multiply((params.getDt() * params.getDt()) / params.getMass()));
            currentSpeed = nextPos.sub(previousPos).multiply(1 / (2 * params.getDt()));

            eventListener.emit(new Event<>(new VerletState(currentPos, currentSpeed, time, params.getDt())));

            previousPos = currentPos;
            currentPos = nextPos;
        }
    }
}
