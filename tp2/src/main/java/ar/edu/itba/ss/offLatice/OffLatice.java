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
import java.util.Random;

public class OffLatice implements Algorithm<OffLaticeParameters> {
    @Override
    public void calculate(OffLaticeParameters params, EventListener eventListener) {
        Algorithm<CellIndexMethodParameters> cim = new CellIndexMethod();
        List<MovableSurfaceEntity<Particle>> particles = new ArrayList<>(params.particles);

        for (int i = 0; i < params.MAX_ITERATIONS; i++) {
            CellIndexMethodParameters parameters = new CellIndexMethodParameters(params.cimParameters.l,params.cimParameters.m,params.cimParameters.n,
                    params.cimParameters.rc,particles);

            EventsQueue queue = new EventsQueue();
            EventProcessor processor = new EventProcessor();
            List<MovableSurfaceEntity<Particle>> newParticles = new ArrayList<>();
            processor.registerHandler(CIMNeighboursMap.class,(map)->{
                for (Map.Entry<SurfaceEntity<Particle>,List<SurfaceEntity<Particle>>> entry:map.getParticlesNeighbours().entrySet()) {
                    double sinAngleSum=0;
                    double cosAngleSum=0;
                    int neighboursCount=0;
                    for (SurfaceEntity<Particle> p:entry.getValue()) {
                        sinAngleSum += Math.sin(((MovableSurfaceEntity<Particle>)p).getAngle());
                        cosAngleSum += Math.cos(((MovableSurfaceEntity<Particle>)p).getAngle());
                        neighboursCount++;
                    }
                    double sinAvg = sinAngleSum/neighboursCount;
                    double cosAvg = cosAngleSum/neighboursCount;
                    double atan2Avg = Math.atan2(sinAvg,cosAvg);

                    Random random = new Random();
                    double randomAngle = random.nextDouble() * params.ETHA- params.ETHA / 2;

                    double newAngle;
                    if(entry.getValue().size() < 1){
                        newAngle = randomAngle;
                    } else{
                        newAngle = randomAngle + atan2Avg;
                    }
                    MovableSurfaceEntity<Particle> current = (MovableSurfaceEntity<Particle>)entry.getKey();
                    double newX = entry.getKey().getX() + Math.cos(newAngle)*(current.getSpeed());
                    double newY = entry.getKey().getY() + Math.sin(newAngle)*(current.getSpeed());

                    newParticles.add(new MovableSurfaceEntity<>(entry.getKey().getEntity(),newX,newY,current.getSpeed(),newAngle));
                }
            });
            cim.calculate(parameters, queue::add);
            for (Event<?> e:queue) {
                processor.processEvent(e);
            }
            particles = newParticles;
            eventListener.emit(new Event<>(new OffLaticeState(newParticles)));
        }
    }
}
