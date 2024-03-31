package ar.edu.itba.ss.output;

import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OffLaticeVaEthaCsvWorker implements QueueWorker {
    private final String outputPath;
    private final int start;
    private final int end;

    private final OffLaticeParameters parameters;

    public OffLaticeVaEthaCsvWorker(String outputPath, int start, int end, OffLaticeParameters parameters) {
        this.outputPath = outputPath;
        this.start = start;
        this.end = end;
        this.parameters = parameters;
    }

    @Override
    public void execute(EventsQueue queue) {
        CSVBuilder builder = new CSVBuilder();
        String outputPath = this.outputPath;
        try {
            builder.appendLine(outputPath,"n", "l", "etha", "va","stDev");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Double> values = new ArrayList<>();
        double sumVa = 0;

        for (Event<?> e : queue) {
            OffLaticeState state = (OffLaticeState) e.getPayload();
            if(state.getTime() >= start && state.getTime() <= end){
                sumVa += state.getVa();
                values.add(state.getVa());
            }
        }

        double media = sumVa/values.size();
        double varSum = 0;
        for (Double va:values) {
            varSum += Math.pow(va-media,2);
        }
        double stDev = Math.sqrt(varSum/values.size());
        try {
            builder.appendLine(outputPath,
                    String.valueOf(parameters.cimParameters.n),
                    String.valueOf(parameters.cimParameters.l),
                    String.valueOf(parameters.etha),
                    String.valueOf(media),
                    String.valueOf(stDev));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
