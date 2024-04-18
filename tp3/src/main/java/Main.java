import ar.edu.itba.ss.MolecularDynamics;
import ar.edu.itba.ss.MolecularParams;
import ar.edu.itba.ss.MolecularState;
import ar.edu.itba.ss.Particle;
import ar.edu.itba.ss.output.CSVBuilder;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MolecularDynamics molecularDynamics = new MolecularDynamics();

        Particle fixedParticle = new Particle(0.1/2,0.1/2,0,0,0.005,1);

        MolecularParams params = new MolecularParams(200,0.1,1,1,0.001,0.15, fixedParticle, 1, 1);
        Simulation<MolecularParams> simulation = new Simulation<>(molecularDynamics);
        simulation.run(params);

        EventsQueue events = simulation.getEventQueue(MolecularState.class);

        CSVBuilder builder = new CSVBuilder();
        try {
            builder.appendLine("output/test.csv", "time", "id", "x", "y", "radius");
            for (Event<?> e : events) {
                MolecularState state = (MolecularState) e.getPayload();
                List<Particle> particles = state.getParticles();

                for (Particle particle : particles) {
                    builder.appendLine(
                            "output/test.csv",
                            String.valueOf(state.getTime()),
                            String.valueOf(particle.getId()),
                            String.valueOf(particle.getX()),
                            String.valueOf(particle.getY()),
                            String.valueOf(particle.getRadius())
                    );
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
