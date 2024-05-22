package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.FootballSimulationConfig;
import ar.edu.itba.ss.input.JsonConfigReader;

import java.io.IOException;

public class VaryingTauMain {
    public static void main(String[] args) throws IOException {

        JsonConfigReader configReader = new JsonConfigReader();
        FootballSimulationConfig config = configReader.readConfig("config.json", FootballSimulationConfig.class);

        ArgumentHandler argumentHandler = new ArgumentHandler();
        argumentHandler.addArgument("-O", (v) -> true, true, "output/tau/");
        argumentHandler.parse(args);

        double step = 0.1;
        double max = 1.0;

        int maxI = (int) (max / step);
        for (int i = 1; i <= maxI; i++) {
            config.setTau(i*step);

            // Initialize CSV File
            String fileName = String.format(argumentHandler.getArgument("-O") + "varying_tau-vd%.2f-tau%.2f", config.getDesiredSpeed(), config.getTau());
            FootballSimulation.simulate(fileName,config);
        }

    }
}
