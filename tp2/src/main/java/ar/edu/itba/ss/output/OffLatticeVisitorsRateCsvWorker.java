package ar.edu.itba.ss.output;

import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OffLatticeVisitorsRateCsvWorker extends OffLatticeVisitorsVizCsvWorker {
    public OffLatticeVisitorsRateCsvWorker(String outputPath, OffLaticeParameters parameters, double visitingAreaRadius) {
        super(outputPath, parameters, visitingAreaRadius);
    }

    @Override
    public void execute(EventsQueue queue) {
        double visitingAreaCenter = this.parameters.cimParameters.l / 2;
        VisitingArea visitingArea = new VisitingArea(visitingAreaCenter, visitingAreaCenter, this.visitingAreaRadius);

        Map<Integer, Boolean> visitorsMap = new HashMap<>();

        CSVBuilder builder = new CSVBuilder();

        String outputPath = this.outputPath;
        try {
            builder.appendLine(outputPath, "time", "n", "l", "visiting_count", "visited_count");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for (Event<?> e : queue) {
            OffLaticeState state = (OffLaticeState) e.getPayload();
            List<MovableSurfaceEntity<Particle>> results = state.getParticles();

            int visitingCount = 0;
            for (MovableSurfaceEntity<Particle> movable : results) {
                // Compute visitors
                boolean isVisiting = visitingArea.isInside(movable.getX(), movable.getY());
                if (isVisiting) {
                    visitorsMap.put(movable.getEntity().getId(), true);
                    visitingCount++;
                }
            }

            long visitedCount = visitorsMap.entrySet().stream()
                    .filter(Map.Entry::getValue)
                    .count();


            try {
                builder.appendLine(outputPath,
                        String.valueOf(state.getTime()),
                        String.valueOf(parameters.cimParameters.n),
                        String.valueOf(parameters.cimParameters.l),
                        String.valueOf(visitingCount),
                        String.valueOf(visitedCount));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }

    }
}
