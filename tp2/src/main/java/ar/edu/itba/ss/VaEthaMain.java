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

public class VaEthaMain {

    public final static String CONFIG_PATH = "config.json";

    public final static String OUTPUT_PATH = "output/va_etha/va_etha";

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH)
                .addArgument("-C", (v) -> true, true, CONFIG_PATH)
                .addArgument("--etha-step", ArgumentHandler::validateDouble, true, "0.1")
                .addArgument("--etha-start", ArgumentHandler::validateDouble, true, "0")
                .addArgument("--etha-max", ArgumentHandler::validateDouble, true, "5")
                .addArgument("--time-start", ArgumentHandler::validateInt, true, "500")
                .addArgument("--time-end", ArgumentHandler::validateInt, true, "900");
        handler.parse(args);

        OffLaticeParameters offLaticeParameters = configReader.readConfig(handler.getArgument("-C"), OffLaticeParameters.class);

        int timeStart = handler.getIntArgument("--time-start");
        int timeEnd = handler.getIntArgument("--time-end");

        double etha = handler.getDoubleArgument("--etha-step");
        double ethaStep = handler.getDoubleArgument("--etha-start");
        double ethaMax = handler.getDoubleArgument("--etha-max");

        while (etha <= ethaMax) {
            offLaticeParameters.etha = etha;
            offLaticeParameters.particles = OffLaticeUtils.initializeParticles(offLaticeParameters);

            OffLatice offLatice = new OffLatice();
            Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);

            simOffLatice.run(offLaticeParameters);
            EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);

            OffLaticeVaEthaCsvWorker ethaCsvWorker = new OffLaticeVaEthaCsvWorker(handler.getArgument("-O") + '_' + offLaticeParameters.cimParameters.l + '_' + offLaticeParameters.cimParameters.n + ".csv", timeStart, timeEnd, offLaticeParameters);
            Thread thread = new Thread(new QueueWorkerHandler(ethaCsvWorker, queue));
            thread.start();

            etha += ethaStep;
        }

    }
}
