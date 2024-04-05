package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.OffLaticeUtils;
import ar.edu.itba.ss.output.OffLaticeVaTimeCsvWorker;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorkerHandler;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class VaTimeParticlesMain {

    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/va_time_particles/va_time_particles";

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String formattedDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter);

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH + "_" + formattedDate)
                .addArgument("-C",(v)->true,true,CONFIG_PATH)
                .addArgument("--particles-start",ArgumentHandler::validateInt,true,"200")
                .addArgument("--particles-max",ArgumentHandler::validateInt,true,"4000")
                .addArgument("--particles-step",ArgumentHandler::validateInt,true,"200");
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);

        int particles = handler.getIntArgument("--particles-start");
        int step = handler.getIntArgument("--particles-step");
        int max = handler.getIntArgument("--particles-max");
        while (particles <= max){
            offLaticeParameters.particles = OffLaticeUtils.initializeParticles(offLaticeParameters);
            OffLaticeParameters aux = new OffLaticeParameters(offLaticeParameters);
            aux.cimParameters.n = particles;

            OffLatice offLatice = new OffLatice();
            Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);


            simOffLatice.run(aux);
            EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);

            OffLaticeVaTimeCsvWorker timeCsvWorker = new OffLaticeVaTimeCsvWorker(handler.getArgument("-O")+'_'+offLaticeParameters.cimParameters.l +'_' + aux.cimParameters.n + '_' + aux.etha + ".csv",aux);
            Thread thread = new Thread(new QueueWorkerHandler(timeCsvWorker,queue));
            thread.start();

            particles += step;
        }

    }
}
