package ar.edu.itba.ss.output;

import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorker;

import java.io.IOException;
import java.util.List;

public class OffLaticeCsvWorker implements QueueWorker {

    private final String outputPath;
    private final OffLaticeParameters parameters;

    public OffLaticeCsvWorker(String outputPath, OffLaticeParameters parameters) {
        this.outputPath = outputPath;
        this.parameters = parameters;
    }

    @Override
    public void execute(EventsQueue queue) {
        CSVBuilder builder = new CSVBuilder();
        String outputPath = this.outputPath;
        try {
            builder.appendLine(outputPath,"time", "n", "l", "id", "x", "y", "radius", "speed", "angle", "va", "etha");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Event<?> e : queue) {
            OffLaticeState state = (OffLaticeState) e.getPayload();
            List<MovableSurfaceEntity<Particle>> results = state.getParticles();
            for (MovableSurfaceEntity<Particle> movable : results) {
                try {
                    builder.appendLine(outputPath,
                            String.valueOf(state.getTime()),
                            String.valueOf(parameters.cimParameters.n),
                            String.valueOf(parameters.cimParameters.l),
                            String.valueOf(movable.getEntity().getId()),
                            String.valueOf(movable.getX()),
                            String.valueOf(movable.getY()),
                            String.valueOf(movable.getEntity().getRadius()),
                            String.valueOf(movable.getSpeed()),
                            String.valueOf(movable.getAngle()),
                            String.valueOf(state.getVa()),
                            String.valueOf(parameters.etha));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
