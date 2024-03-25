package ar.edu.itba.ss;

import ar.edu.itba.ss.cim.CellIndexMethod;
import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.OffLatice;
import ar.edu.itba.ss.offLatice.OffLaticeParameters;
import ar.edu.itba.ss.offLatice.OffLaticeState;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.simulation.Simulation;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventProcessor;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        CellIndexMethodParameters cimParameters = new CellIndexMethodParameters(10, 4, 50, 1.0, new ArrayList<>());

        Random random = new Random();
        List<MovableSurfaceEntity<Particle>> particles = new ArrayList<>();
        for (int i = 0; i < cimParameters.n; i++) {
            particles.add(new MovableSurfaceEntity<>(new Particle(0.2),
                    random.nextDouble()* cimParameters.l,
                    random.nextDouble()* cimParameters.l,
                    0.3,
                    random.nextDouble()*2*Math.PI));
        }

        OffLatice offLatice = new OffLatice();
        Simulation<OffLaticeParameters> simOffLatice = new Simulation<>(offLatice);
        OffLaticeParameters offLaticeParameters = new OffLaticeParameters(cimParameters,particles,1);

        simOffLatice.run(offLaticeParameters);
        EventsQueue queue = simOffLatice.getEventQueue(OffLaticeState.class);
        System.out.println(queue.size());
        for (Event<?> e:queue) {
            OffLaticeState state = (OffLaticeState) e.getPayload();
            List<MovableSurfaceEntity<Particle>> results = state.getParticles();
            for (MovableSurfaceEntity<Particle> movable:results) {
                System.out.println(movable);
            }
        }
    }
}