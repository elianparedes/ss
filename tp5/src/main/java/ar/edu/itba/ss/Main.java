package ar.edu.itba.ss;

import ar.edu.itba.ss.input.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        JsonConfigReader configReader = new JsonConfigReader();
        FootballSimulationConfig config = configReader.readConfig("config.json", FootballSimulationConfig.class);

        ArgumentHandler argumentHandler = new ArgumentHandler();
        argumentHandler.addArgument("-O", (v) -> true, true, "output/");
        argumentHandler.parse(args);

        // Initialize CSV File
        String fileName = String.format(argumentHandler.getArgument("-O") + "futball-vd%.2f-tau%.2f.csv", config.getDesiredSpeed(), config.getTau());

        FootballSimulation.simulate(fileName,config);
    }
}