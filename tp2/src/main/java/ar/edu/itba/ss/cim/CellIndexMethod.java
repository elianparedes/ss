package ar.edu.itba.ss.cim;

import ar.edu.itba.ss.cim.config.TraversalOffset;
import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.geometry.Cell;
import ar.edu.itba.ss.cim.geometry.Grid;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventListener;

import java.util.*;

public class CellIndexMethod implements Algorithm<CellIndexMethodParameters> {

    @Override
    public void calculate(CellIndexMethodParameters params, EventListener eventListener) {
        Grid<Particle> grid = new Grid<>(params.l, params.m);
        Map<SurfaceEntity<Particle>, List<SurfaceEntity<Particle>>> particlesNeighbours = new LinkedHashMap<>();
        // Populate grid with particles
        for (SurfaceEntity<Particle> particle : params.particles) {
            grid.place(particle);
            particlesNeighbours.put(particle,new ArrayList<>());
        }

        for (SurfaceEntity<Particle> currentParticle : params.particles) {
            Cell<Particle> cell = grid.locate(currentParticle.getX(), currentParticle.getY());
            List<Cell<Particle>> neighbourCells = grid.getPeriodicNeighbours(cell, TraversalOffset.L_NEIGHBOURS);
            List<SurfaceEntity<Particle>> currentParticleNeighbours = particlesNeighbours.getOrDefault(currentParticle, new ArrayList<>());

            for (Cell<Particle> c: neighbourCells) {
                for (SurfaceEntity<Particle> neighbourCandidate : c.getEntities()) {
                    double distance = (neighbourCandidate.distanceTo(currentParticle) - neighbourCandidate.getEntity().getRadius()) - currentParticle.getEntity().getRadius();
                    if (distance <= params.rc && !currentParticle.equals(neighbourCandidate)) {

                        // Set current particle neighbour
                        currentParticleNeighbours.add(neighbourCandidate);
                        particlesNeighbours.putIfAbsent(currentParticle, currentParticleNeighbours);

                        // Set current particle as neighbour of candidate
                        List<SurfaceEntity<Particle>> neighbourCandidateNeighbours = particlesNeighbours.getOrDefault(neighbourCandidate, new ArrayList<>());
                        particlesNeighbours.putIfAbsent(neighbourCandidate, neighbourCandidateNeighbours);

                    }
                }
            }
        };
        CIMNeighboursMap map = new CIMNeighboursMap(particlesNeighbours);
        eventListener.emit(new Event<>(map));
    }
}
