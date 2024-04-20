package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.List;

public class MolecularDynamicsState {

    private final List<MovableSurfaceEntity<Particle>> particles;

    private final double time;

    public MolecularDynamicsState(List<MovableSurfaceEntity<Particle>> particles, double time) {
        this.particles = particles;
        this.time = time;
    }

    public List<MovableSurfaceEntity<Particle>> getParticles() {
        return particles;
    }

    public double getTime() {
        return time;
    }

}
