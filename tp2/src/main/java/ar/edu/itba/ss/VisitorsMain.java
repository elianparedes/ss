package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.OffLaticeUtils;
import ar.edu.itba.ss.output.OffLatticeVisitorsRateCsvWorker;
import ar.edu.itba.ss.output.OffLatticeVisitorsVizCsvWorker;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorkerHandler;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class VisitorsMain {

    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/visitors/visitors";

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String formattedDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter);

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH + "_" + formattedDate)
                .addArgument("-C", (v) -> true, true, CONFIG_PATH)
                .addArgument("--area-radius", ArgumentHandler::validateDouble, true, "0.5")
                .addArgument("--conditions", (v) -> true, true, "pbc");
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);

        double visitingAreaRadius = handler.getDoubleArgument("--area-radius");
        offLaticeParameters.hasPeriodicBoundaryConditions = handler.getArgument("--conditions").equals("pbc");
        offLaticeParameters.particles = OffLaticeUtils.initializeParticles(offLaticeParameters);
        OffLaticeParameters aux = new OffLaticeParameters(offLaticeParameters);

        OffLatice offLatice = new OffLatice();
        Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);

        simOffLatice.run(aux);
        EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);

        OffLatticeVisitorsVizCsvWorker visitorsVizCsvWorker = new OffLatticeVisitorsVizCsvWorker(handler.getArgument("-O") + '_' + offLaticeParameters.cimParameters.l + '_' + offLaticeParameters.cimParameters.n + '_' + aux.etha + ".csv", aux, visitingAreaRadius);
        Thread visitorsVizCsvThread = new Thread(new QueueWorkerHandler(visitorsVizCsvWorker, queue));
        visitorsVizCsvThread.start();

        OffLatticeVisitorsRateCsvWorker visitorsRateCsvWorker = new OffLatticeVisitorsRateCsvWorker(handler.getArgument("-O") + '_' + offLaticeParameters.cimParameters.l + '_' + offLaticeParameters.cimParameters.n + '_' + aux.etha + '_' + "rates" + ".csv", aux, visitingAreaRadius);
        Thread visitorsRateCsvThread = new Thread(new QueueWorkerHandler(visitorsRateCsvWorker, queue));
        visitorsRateCsvThread.start();
    }
}
