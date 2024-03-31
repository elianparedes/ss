package ar.edu.itba.ss.output;

import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import ar.edu.itba.ss.simulation.worker.QueueWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OffLaticeVaTimeCsvWorker implements QueueWorker {
    private final String outputPath;
    private final OffLaticeParameters parameters;

    public OffLaticeVaTimeCsvWorker(String outputPath, OffLaticeParameters parameters) {
        this.outputPath = outputPath;
        this.parameters = parameters;
    }

    @Override
    public void execute(EventsQueue queue) {
        CSVBuilder builder = new CSVBuilder();
        String outputPath = this.outputPath;
        try {
            builder.appendLine(outputPath,"n", "l", "etha","time","va");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Event<?> e : queue) {
            OffLaticeState state = (OffLaticeState) e.getPayload();
            try {
                builder.appendLine(outputPath,
                        String.valueOf(parameters.cimParameters.n),
                        String.valueOf(parameters.cimParameters.l),
                        String.valueOf(parameters.etha),
                        String.valueOf(state.getTime()),
                        String.valueOf(state.getVa()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
