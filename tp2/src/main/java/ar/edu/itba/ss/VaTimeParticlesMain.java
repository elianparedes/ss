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

public class VaTimeParticlesMain {

    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/va_time_particles/va_time_particles";

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH)
                .addArgument("-C",(v)->true,true,CONFIG_PATH);
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);

        int particles = 400;
        int step = particles;
        int max = 4000;
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
