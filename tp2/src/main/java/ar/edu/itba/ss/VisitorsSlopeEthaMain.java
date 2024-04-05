package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.OffLaticeUtils;
import ar.edu.itba.ss.output.*;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.MultiQueueWorkerHandler;
import ar.edu.itba.ss.simulation.worker.QueueWorkerHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VisitorsSlopeEthaMain {

    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/visitors-slope-rate/visitors";

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String formattedDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter);

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH + "_" + formattedDate + "_" + "slope_rate")
                .addArgument("-C", (v) -> true, true, CONFIG_PATH)
                .addArgument("--area-radius", ArgumentHandler::validateDouble, true, "0.5")
                .addArgument("--conditions", (v) -> true, true, "obc")
                .addArgument("--etha-start", ArgumentHandler::validateDouble, true, "0")
                .addArgument("--etha-max", ArgumentHandler::validateDouble, true, "5")
                .addArgument("--etha-step", ArgumentHandler::validateDouble, true, "0.2");
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);

        CSVBuilder builder = new CSVBuilder();
        try {
            builder.appendLine(handler.getArgument("-O") + ".csv", "n", "l", "etha", "mean", "stdev");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double visitingAreaRadius = handler.getDoubleArgument("--area-radius");
        BigDecimal etha = BigDecimal.valueOf(handler.getDoubleArgument("--etha-start"));
        BigDecimal ethaStep = BigDecimal.valueOf(handler.getDoubleArgument("--etha-step"));
        BigDecimal ethaMax = BigDecimal.valueOf(handler.getDoubleArgument("--etha-max"));

        while (etha.compareTo(ethaMax) <= 0) {
            offLaticeParameters.etha = etha.doubleValue();
            offLaticeParameters.particles = OffLaticeUtils.initializeParticles(offLaticeParameters);
            offLaticeParameters.hasPeriodicBoundaryConditions = false;

            OffLatice offLatice = new OffLatice();
            Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);

            simOffLatice.run(offLaticeParameters);
            EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);

            OffLatticeVisitorsRateSlopeCsvWorker visitorsRateSlopeCsvWorker = new OffLatticeVisitorsRateSlopeCsvWorker(handler.getArgument("-O") + ".csv", new OffLaticeParameters(offLaticeParameters), visitingAreaRadius);
            new QueueWorkerHandler(visitorsRateSlopeCsvWorker, queue).run();

            System.out.println(offLaticeParameters.hasPeriodicBoundaryConditions);

            if (etha.equals(ethaStep) || etha.equals(ethaMax)) {
                OffLatticeVisitorsVizCsvWorker visitorsVizCsvWorker = new OffLatticeVisitorsVizCsvWorker(handler.getArgument("-O") + '_' + offLaticeParameters.cimParameters.l + '_' + offLaticeParameters.cimParameters.n + '_' + offLaticeParameters.etha + '_' + "viz" +  ".csv",  new OffLaticeParameters(offLaticeParameters), visitingAreaRadius);
                new QueueWorkerHandler(visitorsVizCsvWorker, queue).run();

                OffLatticeVisitorsRateCsvWorker visitorsRateCsvWorker = new OffLatticeVisitorsRateCsvWorker(handler.getArgument("-O") + '_' + offLaticeParameters.cimParameters.l + '_' + offLaticeParameters.cimParameters.n + '_' + offLaticeParameters.etha + '_' + "rates" + ".csv", new OffLaticeParameters(offLaticeParameters), visitingAreaRadius);
                new QueueWorkerHandler(visitorsRateCsvWorker, queue).run();
            }

            etha = etha.add(ethaStep);
        }
    }

}
