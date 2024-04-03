package ar.edu.itba.ss.output;

import ar.edu.itba.ss.cim.geometry.Point;
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
    public OffLatticeVisitorsRateCsvWorker(String outputPath, OffLaticeParameters parameters, double visitingAreaRadius, boolean isOpenBoundaryConditions) {
        super(outputPath, parameters, visitingAreaRadius, isOpenBoundaryConditions);
    }

    @Override
    public void execute(EventsQueue queue) {
        double visitingAreaCenter = this.parameters.cimParameters.l / 2;
        VisitingArea visitingArea = new VisitingArea(visitingAreaCenter, visitingAreaCenter, this.visitingAreaRadius);

        Map<Integer, Boolean> visitorsMap = new HashMap<>();
        Map<Integer, Point> previousPositions = new HashMap<>();


        CSVBuilder builder = new CSVBuilder();

        String outputPath = this.outputPath;
        try {
            builder.appendLine(outputPath, "time", "visiting_count", "visited_count", "not_visited_count");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for (Event<?> e : queue) {
            OffLaticeState state = (OffLaticeState) e.getPayload();
            List<MovableSurfaceEntity<Particle>> results = state.getParticles();

            int visitingCount = 0;

            for (MovableSurfaceEntity<Particle> movable : results) {
                if (isOpenBoundaryConditions) {
                    // Check if particle moved out of viewport
                    Point previousPosition = previousPositions.getOrDefault(movable.getEntity().getId(), null);
                    Point currentPosition = new Point(movable.getX(), movable.getY());
                    if (previousPosition != null) {
                        boolean mustForgetVisitor = periodicBoundaryConditionApplied(currentPosition, previousPosition);
                        if (mustForgetVisitor) visitorsMap.put(movable.getEntity().getId(), false);
                    }
                    previousPositions.put(movable.getEntity().getId(), currentPosition);
                }

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

            long notVisitedCount = results.size() - visitedCount;

            try {
                builder.appendLine(outputPath, String.valueOf(state.getTime()),
                        String.valueOf(visitingCount),
                        String.valueOf(visitedCount),
                        String.valueOf(notVisitedCount));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }

    }
}
