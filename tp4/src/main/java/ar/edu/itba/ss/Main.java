package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.AlgorithmState;
import ar.edu.itba.ss.algorithms.beeman.BeemanAlgorithm;
import ar.edu.itba.ss.algorithms.beeman.BeemanParameters;
import ar.edu.itba.ss.algorithms.verlet.VerletAlgorithm;
import ar.edu.itba.ss.algorithms.verlet.VerletParameters;
import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.ForceType;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.output.CSVBuilder;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.io.IOException;

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
            public Vector apply(Vector v0, Vector v1) {
                return v0.multiply(-1 * K).sub(v1.multiply(GAMMA));
            }
        };

        double dt = DT_START;
        for (int i = 0; i < BENCHMARK_MAX_ITERATIONS; i++) {
            Vector initialPos = new Vector(POSITION_START, 0);
            Vector initialSpeed = new Vector(-GAMMA / (2 * MASS), 0);
            Vector previousPos = initialPos.sum(initialSpeed.multiply(-1 * dt)).sum(force.apply(initialPos, initialSpeed).multiply((dt * dt) / (2 * MASS)));
            Vector previousSpeed = initialSpeed.sum(force.apply(initialPos, initialSpeed).multiply((-1 * dt) / MASS));
            Vector previousAcceleration = force.apply(previousPos, previousSpeed).multiply(1 / MASS);
            Vector currentAcceleration = force.apply(initialPos, initialSpeed).multiply(1 / MASS);

            int maxIterations = getMaxIterationsForDt(dt);

            VerletParameters parameters = new VerletParameters(initialPos, previousPos, initialSpeed, force, MASS, dt, maxIterations);
            VerletAlgorithm algorithm = new VerletAlgorithm();

            EventsQueue eventsQueue = new EventsQueue();
            algorithm.calculate(parameters, eventsQueue::add);

            BeemanParameters beemanParameters = new BeemanParameters(MASS, dt, maxIterations, initialPos, initialSpeed, previousAcceleration, currentAcceleration, force, ForceType.POS_SPEED_FORCE);
            BeemanAlgorithm beemanAlgorithm = new BeemanAlgorithm();
            EventsQueue beemanEventsQueue = new EventsQueue();

            beemanAlgorithm.calculate(beemanParameters, beemanEventsQueue::add);

            String fileName = String.format("output/i%s.csv", i);

            CSVBuilder builder = new CSVBuilder();
            try {
                builder.appendLine(fileName, "dt", "time", "verlet", "beeman");
                for (int j = 0; j < maxIterations; j++) {
                    //Verlet
                    AlgorithmState state = (AlgorithmState) eventsQueue.get(j).getPayload();
                    String verlet = String.valueOf(state.getPosition().getX());

                    //Beeman
                    state = (AlgorithmState) beemanEventsQueue.get(j).getPayload();
                    String beeman = String.valueOf(state.getPosition().getX());

                    builder.appendLine(
                            fileName,
                            String.valueOf(state.getDt()),
                            String.valueOf(state.getTime()),
                            verlet,
                            beeman
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            dt += DT_STEP;
        }
    }
}
