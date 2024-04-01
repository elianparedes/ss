package ar.edu.itba.ss.bf;

import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.models.Particle;

import java.util.List;
import java.util.Map;

public class BFNeighboursMap {

    private final Map<SurfaceEntity<Particle>, List<SurfaceEntity<Particle>>> particlesNeighbours;

    public BFNeighboursMap(Map<SurfaceEntity<Particle>, List<SurfaceEntity<Particle>>> particlesNeighbours) {
        this.particlesNeighbours = particlesNeighbours;
    }

    public Map<SurfaceEntity<Particle>, List<SurfaceEntity<Particle>>> getParticlesNeighbours() {
        return particlesNeighbours;
    }
}
