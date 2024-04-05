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

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VisitorsTimeEthaMain {
    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/visitors-time-etha/visitors-time-etha";

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String formattedDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter);

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH + "_" + formattedDate + "_" + "max_rate")
                .addArgument("-C", (v) -> true, true, CONFIG_PATH)
                .addArgument("--area-radius", ArgumentHandler::validateDouble, true, "0.5")
                .addArgument("--conditions", (v) -> true, true, "pbc")
                .addArgument("--etha-start", ArgumentHandler::validateDouble, true, "0")
                .addArgument("--etha-max", ArgumentHandler::validateDouble, true, "5")
                .addArgument("--etha-step", ArgumentHandler::validateDouble, true, "0.5");
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);

        CSVBuilder builder = new CSVBuilder();
        try {
            builder.appendLine(handler.getArgument("-O") + ".csv", "n", "l", "max_rate", "etha", "mean", "stdev");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double visitingAreaRadius = handler.getDoubleArgument("--area-radius");
        double etha = handler.getDoubleArgument("--etha-start");
        double ethaStep = handler.getDoubleArgument("--etha-step");
        double ethaMax = handler.getDoubleArgument("--etha-max");

        double maxI = 5;
        while (etha <= ethaMax) {
            offLaticeParameters.etha = etha;
            offLaticeParameters.hasPeriodicBoundaryConditions = true;
            System.out.println(etha);
            List<EventsQueue> simQueues = new ArrayList<>();

            double i = 0;
            while (i < maxI) {
                offLaticeParameters.particles = OffLaticeUtils.initializeParticles(offLaticeParameters);

                OffLatice offLatice = new OffLatice();
                Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);

                simOffLatice.run(offLaticeParameters);
                EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);

                simQueues.add(queue);
                i++;
            }

            OffLatticeVisitorsMaxRateCsvWorker visitorsMaxRateCsvWorker = new OffLatticeVisitorsMaxRateCsvWorker(handler.getArgument("-O") + ".csv", new OffLaticeParameters(offLaticeParameters), visitingAreaRadius, 0.6);
            new MultiQueueWorkerHandler(visitorsMaxRateCsvWorker, simQueues).run();

            etha += ethaStep;
        }
    }
}
