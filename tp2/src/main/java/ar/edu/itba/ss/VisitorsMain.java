package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.OffLaticeUtils;
import ar.edu.itba.ss.output.OffLaticeVaTimeCsvWorker;
import ar.edu.itba.ss.output.OffLatticeVisitorsCsvWorker;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorkerHandler;

import java.io.IOException;
import java.math.BigDecimal;

public class VisitorsMain {

    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/visitors/visitors";

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH)
                .addArgument("-C",(v)->true,true,CONFIG_PATH);
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);

        offLaticeParameters.particles = OffLaticeUtils.initializeParticles(offLaticeParameters);
        OffLaticeParameters aux = new OffLaticeParameters(offLaticeParameters);

        OffLatice offLatice = new OffLatice();
        Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);

        simOffLatice.run(aux);
        EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);

        OffLatticeVisitorsCsvWorker timeCsvWorker = new OffLatticeVisitorsCsvWorker(handler.getArgument("-O")+'_'+offLaticeParameters.cimParameters.l +'_' + offLaticeParameters.cimParameters.n + '_' + aux.etha + ".csv", 1,aux);
        Thread thread = new Thread(new QueueWorkerHandler(timeCsvWorker,queue));
        thread.start();
    }
}
