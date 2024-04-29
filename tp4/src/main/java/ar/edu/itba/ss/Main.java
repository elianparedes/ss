package ar.edu.itba.ss;

import ar.edu.itba.ss.dynamics.VerletAlgorithm;
import ar.edu.itba.ss.dynamics.VerletParameters;
import ar.edu.itba.ss.dynamics.VerletState;
import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.output.CSVBuilder;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Algorithm constants
     */
    private static final double MASS = 70;
    private static final double K = 10000;
    private static final double GAMMA = 100;

    /**
     * Delta time step and starting values for algorithm benchmarks
     */
    private static final double DT_STEP = 0.00005;
    private static final double DT_START = 0.0000001;

    /**
     * Max benchmark iterations
     */
    private static final double BENCHMARK_MAX_ITERATIONS = 10;

    private static final double POSITION_START = 1;

    /**
     * Max iterations that completes the damped harmonic oscillator period
     */
    private static final double MAX_ITERATIONS_REF = 75000;

    /**
     * Reference delta time for the given max itereations reference that completes the damped harmonic oscillator period
     */
    private static final double DT_REF = 0.0001;

    public static int getMaxIterationsForDt(double dt) {
        return (int) Math.ceil((dt * MAX_ITERATIONS_REF) / DT_REF);
    }

    public static void main(String[] args) {

        Force force = new Force() {
            @Override
            public Vector apply(double t) {
                throw new RuntimeException("Not implemented");
            }

            @Override
            public Vector apply(Vector position, Vector speed) {
                return position.multiply(-1 * K).sub(speed.multiply(GAMMA));
            }
        };

        double dt = DT_START;
        for (int i = 0; i < BENCHMARK_MAX_ITERATIONS; i++) {
            Vector initialPos = new Vector(POSITION_START, 0);
            Vector initialSpeed = new Vector(-GAMMA / 2 * MASS, 0);
            Vector previous = initialPos.sum(initialSpeed.multiply(-1 * dt));

            int maxIterations = getMaxIterationsForDt(dt);

            VerletParameters parameters = new VerletParameters(initialPos, previous, initialSpeed, force, MASS, dt, maxIterations);
            VerletAlgorithm algorithm = new VerletAlgorithm();

            EventsQueue eventsQueue = new EventsQueue();
            algorithm.calculate(parameters, eventsQueue::add);

            String fileName = String.format("output/i%s.csv", i);

            CSVBuilder builder = new CSVBuilder();
            try {
                builder.appendLine(fileName, "dt", "time", "speed", "x", "y");
                for (Event<?> e : eventsQueue) {
                    VerletState state = (VerletState) e.getPayload();

                    builder.appendLine(
                            fileName,
                            String.valueOf(state.getDt()),
                            String.valueOf(state.getTime()),
                            String.valueOf(state.getSpeed().getX()),
                            String.valueOf(state.getPosition().getX()),
                            String.valueOf(state.getPosition().getY())
                    );
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            dt += DT_STEP;
        }
    }
}
