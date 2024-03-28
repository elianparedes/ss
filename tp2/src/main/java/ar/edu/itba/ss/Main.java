package ar.edu.itba.ss;

import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.output.CSVBuilder;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {

    public final static String CONFIG_PATH = "config.json";
    public final static String OUTPUT_PATH = "output/offlatice";

    public static void main(String[] args) throws IOException {

        JsonConfigReader configReader = new JsonConfigReader();

        OffLaticeParameters offLaticeParameters = configReader.readConfig(CONFIG_PATH, OffLaticeParameters.class);
        CellIndexMethodParameters cimParameters = offLaticeParameters.cimParameters;

        Random random = new Random();
        List<MovableSurfaceEntity<Particle>> particles = new ArrayList<>();
        for (int i = 0; i < cimParameters.n; i++) {
            double x = random.nextDouble() * cimParameters.l;
            double y = random.nextDouble() * cimParameters.l;

            particles.add(new MovableSurfaceEntity<>(new Particle(cimParameters.r),
                    x,
                    y,
                    offLaticeParameters.speed,
                    random.nextDouble() * 2 * Math.PI));
        }

        OffLatice offLatice = new OffLatice();
        Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);
        offLaticeParameters.particles = particles;

        simOffLatice.run(offLaticeParameters);
        EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);

        CSVBuilder builder = new CSVBuilder();
        builder.addLine("time", "id", "x", "y", "radius", "speed", "angle");
        for (Event<?> e : queue) {
            OffLaticeState state = (OffLaticeState) e.getPayload();
            List<MovableSurfaceEntity<Particle>> results = state.getParticles();
            for (MovableSurfaceEntity<Particle> movable : results) {
                builder.addLine(String.valueOf(state.getTime()),
                        String.valueOf(movable.getEntity().getId()),
                        String.valueOf(movable.getX()),
                        String.valueOf(movable.getY()),
                        String.valueOf(movable.getEntity().getRadius()),
                        String.valueOf(movable.getSpeed()),
                        String.valueOf(movable.getAngle()));
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String formattedDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter);

        ArgumentHandler handler = new ArgumentHandler()
                .addArgument("-O", (v) -> true, true, OUTPUT_PATH + "_" + formattedDate + ".csv");
        handler.parse(args);
        builder.build(handler.getArgument("-O"));
    }
}