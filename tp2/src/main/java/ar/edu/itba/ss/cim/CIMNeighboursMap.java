package ar.edu.itba.ss.cim;

import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.models.Particle;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CIMNeighboursMap {

    private final Map<SurfaceEntity<Particle>, List<SurfaceEntity<Particle>>> particlesNeighbours;

    public CIMNeighboursMap(Map<SurfaceEntity<Particle>, List<SurfaceEntity<Particle>>> particlesNeighbours) {
        this.particlesNeighbours = particlesNeighbours;
    }

    public Map<SurfaceEntity<Particle>, List<SurfaceEntity<Particle>>> getParticlesNeighbours() {
        return particlesNeighbours;
    }
}
