package ar.edu.itba.ss.algorithms.beeman;

import ar.edu.itba.ss.algorithms.AlgorithmState;
import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;

import static ar.edu.itba.ss.models.ForceType.POS_FORCE;
import static ar.edu.itba.ss.models.ForceType.POS_SPEED_FORCE;

public class BeemanAlgorithm implements Algorithm<BeemanParameters>{
    @Override
    public void calculate(BeemanParameters params, EventListener eventListener) {
        double time;
        Vector currentPos = params.getCurrent();
        Vector currentSpeed = params.getInitialSpeed();
        Vector currentAcceleration = params.getInitialAcceleration();
        Vector previousAcceleration = params.getPreviousAcceleration();

        for (int i = 0; i < params.getMaxIterations(); i++) {
            time = params.getDt() * i;
            Vector nextPos = currentPos.sum(currentSpeed.multiply(params.getDt()))
                    .sum(currentAcceleration.multiply(2.0/3.0).sum(previousAcceleration.multiply(-1.0/6.0))
                            .multiply(params.getDt() * params.getDt()));
            Vector nextAcceleration;
            if(params.getForceType().equals(POS_FORCE)){
                nextAcceleration = getNextAcceleration(nextPos, params.getMass(), params.getForce());
            } else if (params.getForceType().equals(POS_SPEED_FORCE)) {
                nextAcceleration = getNextAcceleration(nextPos,
                        currentSpeed,
                        currentAcceleration,
                        previousAcceleration,
                        params.getMass(),
                        params.getDt(),
                        params.getForce());
            } else {
                throw new IllegalArgumentException("Invalid force type");
            }
            Vector nextSpeed = currentSpeed.sum(nextAcceleration.multiply(1.0/3.0).sum(currentAcceleration.multiply(5.0/6.0))
                    .sum(previousAcceleration.multiply(-1.0/6.0)).multiply(params.getDt()));

            nextAcceleration = params.getForce().apply(nextPos,nextSpeed).multiply(1.0 / params.getMass());

            eventListener.emit(new Event<>(new AlgorithmState(currentPos, time, params.getDt())));

            currentPos = nextPos;
            currentSpeed = nextSpeed;
            previousAcceleration = currentAcceleration;
            currentAcceleration = nextAcceleration;
        }
    }

    private Vector getNextAcceleration(Vector nextPos, double mass, Force force){
        return force.apply(nextPos,null).multiply(1.0 / mass);
    }

    private Vector getNextAcceleration(Vector nextPos, Vector currentSpeed, Vector currentAcceleration, Vector previousAcceleration, double mass, double dt,Force force){
        Vector predictedSpeed = currentSpeed.sum(currentAcceleration.multiply(3.0/2.0).sum(previousAcceleration.multiply(-1.0/2.0))
                .multiply(dt));
        return force.apply(nextPos,predictedSpeed).multiply(1.0 / mass);
    }
}
