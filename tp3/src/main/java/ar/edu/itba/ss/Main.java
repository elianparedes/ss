package ar.edu.itba.ss;

import ar.edu.itba.ss.moleculardynamics.MolecularDynamicsAlgorithm;
import ar.edu.itba.ss.moleculardynamics.MolecularDynamicsParameters;
import ar.edu.itba.ss.moleculardynamics.MolecularDynamicsState;
import ar.edu.itba.ss.output.CSVBuilder;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.geometry.Point;
import ar.edu.itba.ss.utils.models.Ball;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int N = 5;
    private static final double L = 0.1;

    private static final double RP = 0.001;

    private static final double RB = 0.005;

    private static final double MASS = 1;

    private static final double SPEED = 0.01;

    private static final int MAX_IT = 5000;

    public static void main(String[] args) {

        SurfaceEntity<Ball> ball = new SurfaceEntity<>(new Ball(RB, MASS), L / 2, L /2);

        List<MovableSurfaceEntity<Particle>> particles = MolecularDynamicsAlgorithm.generateRandomParticles(
                L, N, RP, SPEED, MASS, ball
        );

        List<SurfaceEntity<Border>> fixedObjects = new ArrayList<>();
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(0, 0), new Point(0, L)), 0, 0));
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(L, 0), new Point(L, L)), L, L / 2));
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(0, 0), new Point(L, 0)), L / 2, 0));
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(0, L), new Point(L, L)), L / 2, L));

        MolecularDynamicsParameters params = new MolecularDynamicsParameters(
                particles,
                fixedObjects,
                ball,
                MAX_IT
        );

        MolecularDynamicsAlgorithm algorithm = new MolecularDynamicsAlgorithm();
        Simulation<MolecularDynamicsParameters> simulation = new Simulation<>(algorithm);
        simulation.run(params);

        EventsQueue events = simulation.getEventQueue(MolecularDynamicsState.class);

        CSVBuilder builder = new CSVBuilder();
        try {
            builder.appendLine("output/test.csv", "time", "id", "x", "y", "radius");
            for (Event<?> e : events) {
                MolecularDynamicsState state = (MolecularDynamicsState) e.getPayload();
                List<MovableSurfaceEntity<Particle>> p = state.getParticles();

                for (MovableSurfaceEntity<Particle> particle : p) {
                    builder.appendLine(
                            "output/test.csv",
                            String.valueOf(state.getTime()),
                            String.valueOf(particle.getEntity().getId()),
                            String.valueOf(particle.getX()),
                            String.valueOf(particle.getY()),
                            String.valueOf(particle.getEntity().getRadius())
                    );
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}