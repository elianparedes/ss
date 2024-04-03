package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.OffLaticeUtils;
import ar.edu.itba.ss.output.OffLaticeCsvWorker;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorkerHandler;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {

    public final static String CONFIG_PATH = "config.json";
    public final static String OUTPUT_PATH = "output/default/default";

    public static void main(String[] args) throws IOException {

        JsonConfigReader configReader = new JsonConfigReader();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String formattedDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter);

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH + "_" + formattedDate)
                .addArgument("-C", (v) -> true, true, CONFIG_PATH);
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);
        offLaticeParameters.particles = OffLaticeUtils.initializeParticles(offLaticeParameters);

        OffLatice offLatice = new OffLatice();
        Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);

        simOffLatice.run(offLaticeParameters);

        EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);
        OffLaticeCsvWorker csvWorker = new OffLaticeCsvWorker(handler.getArgument("-O") + ".csv", offLaticeParameters);
        Thread thread1 = new Thread(new QueueWorkerHandler(csvWorker, queue));
        thread1.start();
    }
}