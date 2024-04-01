package ar.edu.itba.ss.bf;

import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteForce implements Algorithm<BruteForceParameters> {

    @Override
    public void calculate(BruteForceParameters params, EventListener eventListener) {
        Map<SurfaceEntity<Particle>, List<SurfaceEntity<Particle>>> particlesNeighbours = new HashMap<>();
        List<? extends SurfaceEntity<Particle>> particles = params.particles;
        for (SurfaceEntity<Particle> particle:particles) {
            particlesNeighbours.putIfAbsent(particle,new ArrayList<>());
            for (SurfaceEntity<Particle> other:particles) {
                double dx = Math.abs(particle.getX() - other.getX());
                double dy = Math.abs(particle.getY() - other.getY());
                double complDx = Math.min(dx, params.l - dx);
                double complDy = Math.min(dy, params.l - dy);
                double complDistance = Math.sqrt(complDy * complDy + complDx * complDx);

                double distance = (particle.distanceTo(other) - other.getEntity().getRadius()) - particle.getEntity().getRadius();
                if((distance <= params.rc || complDistance <=params.rc) && !particle.equals(other)){
                    List<SurfaceEntity<Particle>> neighbours = particlesNeighbours.get(particle);
                    neighbours.add(other);
                    particlesNeighbours.put(particle,neighbours);
                }
            }
        }
        BFNeighboursMap map = new BFNeighboursMap(particlesNeighbours);
        eventListener.emit(new Event<>(map));
    }

}
