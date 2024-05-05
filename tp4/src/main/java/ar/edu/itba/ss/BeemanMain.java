package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.AlgorithmState;
import ar.edu.itba.ss.algorithms.beeman.BeemanAlgorithm;
import ar.edu.itba.ss.algorithms.beeman.BeemanParameters;
import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.ForceType;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.output.CSVBuilder;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.io.IOException;

public class BeemanMain {

    /**
     * Algorithm constants
     */
    private static final double MASS = 70;
    private static final double K = 10000;
    private static final double GAMMA = 100;

    private static final double POSITION_START = 1;

    private static final double DT= 0.001;

    private static final int MAX_ITERATIONS_REF = 7500;


    public static void main(String[] args) {
        Force force = new Force() {
            @Override
            public Vector apply(double t) {
                throw new RuntimeException("Not implemented");
            }

            @Override
            public Vector apply(Vector v0, Vector v1) {
                return v0.multiply(-1 * K).sub(v1.multiply(GAMMA));
            }
        };
        Vector initialPos = new Vector(POSITION_START, 0);
        Vector initialSpeed = new Vector(-GAMMA / (2 * MASS), 0);
        Vector previousPos = initialPos.sum(initialSpeed.multiply(-1 * DT)).sum(force.apply(initialPos, initialSpeed).multiply((DT * DT) / (2 * MASS)));
        Vector previousSpeed = initialSpeed.sum(force.apply(initialPos, initialSpeed).multiply((-1*DT) / MASS));
        Vector previousAcceleration = force.apply(previousPos, previousSpeed).multiply(1 / MASS);
        Vector currentAcceleration = force.apply(initialPos, initialSpeed).multiply(1 / MASS);

        BeemanParameters parameters = new BeemanParameters(MASS,DT,MAX_ITERATIONS_REF,initialPos,initialSpeed,previousAcceleration,currentAcceleration,force, ForceType.POS_SPEED_FORCE);
        BeemanAlgorithm algorithm = new BeemanAlgorithm();
        EventsQueue eventsQueue = new EventsQueue();

        algorithm.calculate(parameters, eventsQueue::add);

        String fileName = "output/beeman.csv";

        CSVBuilder builder = new CSVBuilder();
        try {
            builder.appendLine(fileName, "dt", "time", "x", "y");
            for (Event<?> e : eventsQueue) {
                AlgorithmState state = (AlgorithmState) e.getPayload();

                builder.appendLine(
                        fileName,
                        String.valueOf(state.getDt()),
                        String.valueOf(state.getTime()),
                        String.valueOf(state.getPosition().getX()),
                        String.valueOf(state.getPosition().getY())
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
