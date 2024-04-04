package ar.edu.itba.ss.offLatice;

import ar.edu.itba.ss.cim.CIMNeighboursMap;
import ar.edu.itba.ss.cim.CellIndexMethod;
import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventListener;
import ar.edu.itba.ss.simulation.events.EventProcessor;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class OffLatice implements Algorithm<OffLaticeParameters> {

    @Override
    public void calculate(OffLaticeParameters params, EventListener eventListener) {

        List<MovableSurfaceEntity<Particle>> particles = OffLaticeUtils.initializeParticles(params);
        //TODO: Hay que emitir las particulas iniciales

        for (int i = 0; i < params.maxIter; i++) {
            Algorithm<CellIndexMethodParameters> cim = new CellIndexMethod();
            CellIndexMethodParameters parameters = new CellIndexMethodParameters(params.cimParameters.l, params.cimParameters.m, params.cimParameters.n,
                    params.cimParameters.rc, params.cimParameters.r, particles);

            EventsQueue queue = new EventsQueue();
            EventProcessor processor = new EventProcessor();
            List<MovableSurfaceEntity<Particle>> newParticles = new ArrayList<>();
            AtomicReference<Double> va = new AtomicReference<>((double) 0);

            processor.registerHandler(CIMNeighboursMap.class, (map) -> {

                double speedXSum = 0;
                double speedYSum = 0;

                for (Map.Entry<SurfaceEntity<Particle>, Set<SurfaceEntity<Particle>>> entry : map.getParticlesNeighbours().entrySet()) {
                    double sinAngleSum = Math.sin(((MovableSurfaceEntity<Particle>) entry.getKey()).getAngle());
                    double cosAngleSum = Math.cos(((MovableSurfaceEntity<Particle>) entry.getKey()).getAngle());
                    int neighboursCount = 1;

                    for (SurfaceEntity<Particle> p : entry.getValue()) {
                        sinAngleSum += Math.sin(((MovableSurfaceEntity<Particle>) p).getAngle());
                        cosAngleSum += Math.cos(((MovableSurfaceEntity<Particle>) p).getAngle());
                        neighboursCount++;
                    }

                    double sinAvg = sinAngleSum / neighboursCount;
                    double cosAvg = cosAngleSum / neighboursCount;
                    double atan2Avg = Math.atan2(sinAvg, cosAvg);

                    double randomAngle = Math.random() * params.etha - params.etha / 2;

                    double newAngle;
                    newAngle = randomAngle + atan2Avg;

                    MovableSurfaceEntity<Particle> current = (MovableSurfaceEntity<Particle>) entry.getKey();
                    double newX = (current.getX() + current.getXSpeed()) % params.cimParameters.l;
                    double newY = (current.getY() + current.getYSpeed()) % params.cimParameters.l;

                    newX = newX < 0 ? newX + params.cimParameters.l : newX;
                    newY = newY < 0 ? newY + params.cimParameters.l : newY;

                    speedXSum += current.getXSpeed();
                    speedYSum += current.getYSpeed();

                    newParticles.add(new MovableSurfaceEntity<>(entry.getKey().getEntity(), newX, newY, current.getSpeed(), newAngle));
                }

                double speed = Math.sqrt(Math.pow(speedXSum, 2) + Math.pow(speedYSum, 2));
                va.set(speed / (params.cimParameters.n * params.speed));

            });
            cim.calculate(parameters, queue::add);
            for (Event<?> e : queue) {
                processor.processEvent(e);
            }
            particles = newParticles;
            eventListener.emit(new Event<>(new OffLaticeState(newParticles, i, va.get())));
        }
    }
}
