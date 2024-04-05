package ar.edu.itba.ss.output;

import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.io.IOException;
import java.util.*;

public class OffLatticeVisitorsRateSlopeCsvWorker extends OffLatticeVisitorsRateCsvWorker {

    public OffLatticeVisitorsRateSlopeCsvWorker(String outputPath, OffLaticeParameters parameters, double visitingAreaRadius) {
        super(outputPath, parameters, visitingAreaRadius);
    }

    @Override
    public void execute(EventsQueue queue) {
        double visitingAreaCenter = this.parameters.cimParameters.l / 2;
        VisitingArea visitingArea = new VisitingArea(visitingAreaCenter, visitingAreaCenter, this.visitingAreaRadius);

        Map<Integer, Boolean> visitorsMap = new HashMap<>();


        CSVBuilder builder = new CSVBuilder();
        String outputPath = this.outputPath;

        List<Double> slopes = new ArrayList<>();
        for (Event<?> e : queue) {
            OffLaticeState state = (OffLaticeState) e.getPayload();
            List<MovableSurfaceEntity<Particle>> results = state.getParticles();

            for (MovableSurfaceEntity<Particle> movable : results) {
                // Compute visitors
                boolean isVisiting = visitingArea.isInside(movable.getX(), movable.getY());
                if (isVisiting) visitorsMap.put(movable.getEntity().getId(), true);
            }

            long visitedCount = visitorsMap.entrySet().stream()
                    .filter(Map.Entry::getValue)
                    .count();

            if (state.getTime() >= 500 && state.getTime() <= 2500) {
                double derivate = ((double) visitedCount / state.getTime());
                slopes.add(derivate);
            };

        }

        double sum = 0;
        for (double slope : slopes) {
            sum += slope;
        }
        double mean = sum / slopes.size();

        double sumOfSquaredDifferences = 0;
        for (double slope : slopes) {
            sumOfSquaredDifferences += Math.pow(slope - mean, 2);
        }
        double variance = sumOfSquaredDifferences / (slopes.size() - 1);
        double stdDeviation = Math.sqrt(variance);

        try {
            builder.appendLine(outputPath,
                    String.valueOf(parameters.cimParameters.n),
                    String.valueOf(parameters.cimParameters.l),
                    String.valueOf(parameters.etha),
                    String.valueOf(mean),
                    String.valueOf(stdDeviation));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
}

