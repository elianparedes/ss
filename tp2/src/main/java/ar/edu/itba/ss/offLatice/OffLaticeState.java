package ar.edu.itba.ss.offLatice;

import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;

import java.util.List;

public class OffLaticeState {
    private final List<MovableSurfaceEntity<Particle>> particles;

    public OffLaticeState(List<MovableSurfaceEntity<Particle>> particles) {
        this.particles = particles;
    }

    public List<MovableSurfaceEntity<Particle>> getParticles() {
        return particles;
    }
}
