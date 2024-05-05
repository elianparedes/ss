package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.AlgorithmState;
import ar.edu.itba.ss.algorithms.gear.GearAlgorithm;
import ar.edu.itba.ss.algorithms.gear.GearParameters;
import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.output.CSVBuilder;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.io.IOException;

public class GearMain {

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
    private static final double DT_START = 0.0001;

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

        double[] alphaValues = new double[]{(3.0 / 16.0), (251.0 / 360), 1.0, (11.0 / 18.0), (1.0 / 6.0), (1.0 / 60.0)};

        Vector initialPos = new Vector(POSITION_START, 0);
        Vector initialSpeed = new Vector(-GAMMA / (2 * MASS), 0);
        Vector initialAcceleration = force.apply(initialPos, initialSpeed).divide(MASS);

        int maxIterations = getMaxIterationsForDt(dt);

        GearParameters parameters = new GearParameters(initialSpeed, initialAcceleration, initialPos, alphaValues, force, MASS, dt, maxIterations);
        GearAlgorithm algorithm = new GearAlgorithm();

        EventsQueue eventsQueue = new EventsQueue();
        algorithm.calculate(parameters, eventsQueue::add);

        String fileName = String.format("output/i%s.csv", 1);

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
