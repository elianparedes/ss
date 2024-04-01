package ar.edu.itba.ss.output;

import ar.edu.itba.ss.cim.geometry.Point;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorker;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OffLatticeVisitorsCsvWorker implements QueueWorker {

    private static class VisitingArea {
        private final double centerX;
        private final double centerY;
        private final double radius;

        public VisitingArea(double centerX, double centerY, double radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }

        public boolean isInside(double x, double y) {
            double distanceSquared = (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY);
            return distanceSquared <= radius * radius;
        }
    }

    private final String outputPath;
    private final OffLaticeParameters parameters;
    private final double visitingAreaRadius;

    public OffLatticeVisitorsCsvWorker(String outputPath, double visitingAreaRadius, OffLaticeParameters parameters) {
        this.outputPath = outputPath;
        this.parameters = parameters;
        this.visitingAreaRadius = visitingAreaRadius;
    }

    @Override
    public void execute(EventsQueue queue) {
        double visitingAreaCenter = parameters.cimParameters.l / 2;
        VisitingArea visitingArea = new VisitingArea(visitingAreaCenter, visitingAreaCenter, visitingAreaRadius);
        Map<Integer, Boolean> visitorsMap = new HashMap<>();
        Map<Integer, Point> previousPositions = new HashMap<>();

        CSVBuilder builder = new CSVBuilder();

        String outputPath = this.outputPath;
        try {
            builder.appendLine(outputPath,"time", "id", "x", "y", "radius", "speed", "angle", "va", "etha", "visiting_area_x", "visiting_area_y", "visiting_area_radius", "has_visited", "is_visiting");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Event<?> e : queue) {
            OffLaticeState state = (OffLaticeState) e.getPayload();
            List<MovableSurfaceEntity<Particle>> results = state.getParticles();

            for (MovableSurfaceEntity<Particle> movable : results) {
                try {

                    // Check if periodic boundary condition was applied
                    Point previousPosition = previousPositions.getOrDefault(movable.getEntity().getId(), null);
                    Point currentPosition = new Point(movable.getX(), movable.getY());
                    if (previousPosition != null) {
                        boolean mustForgetVisitor = periodicBoundaryConditionApplied(currentPosition, previousPosition);
                        if (mustForgetVisitor) visitorsMap.put(movable.getEntity().getId(), false);
                    }
                    previousPositions.put(movable.getEntity().getId(), currentPosition);

                    // Compute visitors
                    boolean isVisiting = visitingArea.isInside(movable.getX(), movable.getY());
                    if (isVisiting) {
                        visitorsMap.put(movable.getEntity().getId(), true);
                    }
                    boolean hasVisited = visitorsMap.getOrDefault(movable.getEntity().getId(), false);

                    builder.appendLine(outputPath,String.valueOf(state.getTime()),
                            String.valueOf(movable.getEntity().getId()),
                            String.valueOf(movable.getX()),
                            String.valueOf(movable.getY()),
                            String.valueOf(movable.getEntity().getRadius()),
                            String.valueOf(movable.getSpeed()),
                            String.valueOf(movable.getAngle()),
                            String.valueOf(state.getVa()),
                            String.valueOf(parameters.etha),
                            String.valueOf(visitingArea.centerX),
                            String.valueOf(visitingArea.centerY),
                            String.valueOf(visitingArea.radius),
                            String.valueOf(hasVisited),
                            String.valueOf(isVisiting));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }

    private boolean periodicBoundaryConditionApplied(Point currentPosition, Point previousPosition) {
        double deltaX = Math.abs(currentPosition.getX() - previousPosition.getX());
        double deltaY = Math.abs(currentPosition.getY() - previousPosition.getY());

        if (deltaX > this.parameters.cimParameters.l / 2) {
            return true;
        }

        return deltaY > this.parameters.cimParameters.l / 2;
    }
}
