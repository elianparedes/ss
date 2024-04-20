package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/test.csv";

    public static void main(String[] args) throws IOException {

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH)
                .addArgument("-C", (v) -> true, true, CONFIG_PATH);
        handler.parse(args);

        JsonConfigReader configReader = new JsonConfigReader();
        MolecularDynamicsParameters params = configReader.readConfig(handler.getArgument("-C"), MolecularDynamicsParameters.class);
        MovableSurfaceEntity<Particle> ball = new MovableSurfaceEntity<>(new Particle(params.rb, params.massB), params.l/ 2, params.l/2,0,0);

        List<MovableSurfaceEntity<Particle>> particles = MolecularDynamicsAlgorithm.generateRandomParticles(
                params.l, params.n, params.rp, params.speed, params.massP, ball
        );

        List<SurfaceEntity<Border>> fixedObjects = new ArrayList<>();
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(0, 0), new Point(0, params.l)), 0, 0));
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(params.l, 0), new Point(params.l, params.l)), params.l, params.l / 2));
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(0, 0), new Point(params.l, 0)), params.l/ 2, 0));
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(0, params.l), new Point(params.l, params.l)), params.l/ 2, params.l));

        params.particles = particles;
        params.fixedObjects = fixedObjects;
        params.ball = ball;


        MolecularDynamicsAlgorithm algorithm = new MolecularDynamicsAlgorithm();
        Simulation<MolecularDynamicsParameters> simulation = new Simulation<>(algorithm);
        simulation.run(params);

        EventsQueue events = simulation.getEventQueue(MolecularDynamicsState.class);

        CSVBuilder builder = new CSVBuilder();

        String filePath = handler.getArgument("-O");
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("No se pudo eliminar el archivo existente: " + filePath);
            }
        }
        try {
            builder.appendLine(filePath, "time", "id", "x", "y", "vx", "vy", "radius");
            for (Event<?> e : events) {
                MolecularDynamicsState state = (MolecularDynamicsState) e.getPayload();
                List<MovableSurfaceEntity<Particle>> p = state.getParticles();

                MovableSurfaceEntity<Particle> ballState = ball;
                for (MovableSurfaceEntity<Particle> particle : p) {
                    if(params.movable && Objects.equals(particle.getEntity().getId(), ball.getEntity().getId())){
                        ballState = particle;
                    } else {
                        builder.appendLine(
                                filePath,
                                String.valueOf(state.getTime()),
                                String.valueOf(particle.getEntity().getId()),
                                String.valueOf(particle.getX()),
                                String.valueOf(particle.getY()),
                                String.valueOf(particle.getXSpeed()),
                                String.valueOf(particle.getYSpeed()),
                                String.valueOf(particle.getEntity().getRadius())
                        );
                    }
                }
                System.out.println(ballState);
                builder.appendLine(filePath,String.valueOf(state.getTime()),
                        String.valueOf(ballState.getEntity().getId()),
                        String.valueOf(ballState.getX()),
                        String.valueOf(ballState.getY()),
                        String.valueOf(ballState.getXSpeed()),
                        String.valueOf(ballState.getYSpeed()),
                        String.valueOf(ballState.getEntity().getRadius()));
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}