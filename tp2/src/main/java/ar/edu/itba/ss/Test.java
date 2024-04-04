package ar.edu.itba.ss;

import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.Event;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        OffLaticeParameters parameters = new OffLaticeParameters();
        parameters.etha = 2.0;
        parameters.maxIter = 5000;
        parameters.speed = 0.03;

        CellIndexMethodParameters cimParameters = new CellIndexMethodParameters();
        cimParameters.l = 20;
        cimParameters.rc = 1;
        cimParameters.r = 0;
        cimParameters.m = 20;

        parameters.cimParameters = cimParameters;

        int n = 200;
        int step = 200;
        int max = 4000;

        System.out.println("n,l,etha,rho,va,stdev");

        while(n <= max){

            cimParameters.n = n;

            OffLatice offLatice = new OffLatice();
            Simulation<OffLaticeParameters> sim = new Simulation<>(offLatice);

            sim.run(parameters);

            List<Double> vaValues = new ArrayList<>();
            double vaSum = 0;
            double count = 0;

            for (Event<?> e: sim.getEventQueue(OffLaticeState.class)) {
                OffLaticeState state = (OffLaticeState) e.getPayload();
                if(state.getTime() >= 3000 && state.getTime() <= 5000) {
                    vaSum += state.getVa();
                    count++;
                    vaValues.add(state.getVa());
                }
            }

            double vaMean = vaSum/count;

            vaSum = 0;
            for (Double va:vaValues) {
                vaSum+= Math.pow(va-vaMean,2);
            }

            double stdev = Math.sqrt(vaSum/(vaValues.size()-1));

            System.out.printf("%d,%.6f,%.2f,%.2f,%.8f,%.8f\n",parameters.cimParameters.n,parameters.cimParameters.l,parameters.etha,cimParameters.n/Math.pow(cimParameters.l,2),vaMean,stdev);

            n+=step;
        }
    }
}
