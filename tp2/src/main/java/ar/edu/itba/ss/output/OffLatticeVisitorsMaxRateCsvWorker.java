package ar.edu.itba.ss.output;

import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.MultiQueueWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OffLatticeVisitorsMaxRateCsvWorker extends OffLatticeVisitorsRateCsvWorker implements MultiQueueWorker {

    private final double maxRate;

    public OffLatticeVisitorsMaxRateCsvWorker(String outputPath, OffLaticeParameters parameters, double visitingAreaRadius, double maxRate) {
        super(outputPath, parameters, visitingAreaRadius);

        this.maxRate = maxRate;
    }

    @Override
    public void execute(List<EventsQueue> queues) {
        double visitingAreaCenter = this.parameters.cimParameters.l / 2;
        VisitingArea visitingArea = new VisitingArea(visitingAreaCenter, visitingAreaCenter, this.visitingAreaRadius);

        Map<Integer, Boolean> visitorsMap = new HashMap<>();


        CSVBuilder builder = new CSVBuilder();
        String outputPath = this.outputPath;


        int totalTime = 0;
        List<Integer> times = new ArrayList<>();

        for (EventsQueue queue : queues) {
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

                if (((double) visitedCount / results.size()) >= maxRate) {
                    int time = state.getTime();
                    times.add(time);
                    totalTime += time;

                    break;
                }

            }
        }

        if (!times.isEmpty()) {
            double timeMean = times.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
            double timeStdDev = Math.sqrt(times.stream().mapToDouble(time -> Math.pow(time - timeMean, 2)).sum() / (times.size() - 1));

            try {
                builder.appendLine(outputPath,
                        String.valueOf(parameters.cimParameters.n),
                        String.valueOf(parameters.cimParameters.l),
                        String.valueOf(maxRate),
                        String.valueOf(parameters.etha),
                        String.valueOf(timeMean),
                        String.valueOf(timeStdDev));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}
