package ar.edu.itba.ss.offLatice;

import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;

import java.util.List;

public class OffLaticeState {
    private final List<MovableSurfaceEntity<Particle>> particles;
    private final int time;

    private final double va;
    public OffLaticeState(List<MovableSurfaceEntity<Particle>> particles, int time, double va) {
        this.particles = particles;
        this.time = time;
        this.va = va;
    }

    public List<MovableSurfaceEntity<Particle>> getParticles() {
        return particles;
    }

    public int getTime() {
        return time;
    }

    public double getVa() {
        return va;
    }
}


