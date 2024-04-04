package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.OffLaticeUtils;
import ar.edu.itba.ss.output.OffLaticeVaEthaCsvWorker;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorkerHandler;

import java.io.IOException;

public class VaRhoMain {

    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/va_rho/va_rho";

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH)
                .addArgument("-C",(v)->true,true,CONFIG_PATH);
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);

        int particles = (int) (Math.ceil(Math.pow(offLaticeParameters.cimParameters.l,2))/10);
        int step = particles;
        int max = particles * 100;
        while (particles <= max){
            offLaticeParameters.particles = OffLaticeUtils.initializeParticles(offLaticeParameters);
            OffLaticeParameters aux = new OffLaticeParameters(offLaticeParameters);

            aux.cimParameters.n = particles;

            OffLatice offLatice = new OffLatice();
            Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);


            simOffLatice.run(aux);
            EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);

            OffLaticeVaEthaCsvWorker ethaCsvWorker = new OffLaticeVaEthaCsvWorker(handler.getArgument("-O")+'_'+offLaticeParameters.cimParameters.l +'_' + offLaticeParameters.etha + ".csv", 3000,5000,aux);
            Thread thread = new Thread(new QueueWorkerHandler(ethaCsvWorker,queue));
            thread.start();

            particles += step;
        }

    }
}
