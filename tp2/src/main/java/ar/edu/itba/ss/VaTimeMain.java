package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.OffLaticeUtils;
import ar.edu.itba.ss.output.OffLaticeCsvWorker;
import ar.edu.itba.ss.output.OffLaticeVaTimeCsvWorker;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorkerHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class VaTimeMain {

    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/va-time/va-time";

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String formattedDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter);

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH + "_" + formattedDate)
                .addArgument("-C", (v) -> true, true, CONFIG_PATH)
                .addArgument("--etha-step", ArgumentHandler::validateDouble, true, "0.1")
                .addArgument("--etha-start", ArgumentHandler::validateDouble, true, "0")
                .addArgument("--etha-max", ArgumentHandler::validateDouble, true, "5");
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);

        BigDecimal etha = BigDecimal.valueOf(handler.getDoubleArgument("--etha-start"));
        BigDecimal ethaStep = BigDecimal.valueOf(handler.getDoubleArgument("--etha-step"));
        BigDecimal ethaMax = BigDecimal.valueOf(handler.getDoubleArgument("--etha-max"));
        while (etha.compareTo(ethaMax) <= 0) {
            offLaticeParameters.particles = OffLaticeUtils.initializeParticles(offLaticeParameters);
            OffLaticeParameters aux = new OffLaticeParameters(offLaticeParameters);
            aux.etha = etha.doubleValue();

            OffLatice offLatice = new OffLatice();
            Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);

            simOffLatice.run(aux);
            EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);

            OffLaticeCsvWorker csvWorker = new OffLaticeCsvWorker(handler.getArgument("-O") + ".csv", offLaticeParameters);
            Thread thread1 = new Thread(new QueueWorkerHandler(csvWorker, queue));
            thread1.start();

            OffLaticeVaTimeCsvWorker timeCsvWorker = new OffLaticeVaTimeCsvWorker(handler.getArgument("-O") + '_' + offLaticeParameters.cimParameters.l + '_' + offLaticeParameters.cimParameters.n + '_' + aux.etha + ".csv", aux);
            Thread thread = new Thread(new QueueWorkerHandler(timeCsvWorker, queue));
            thread.start();

            etha = etha.add(ethaStep);
        }

    }
}
