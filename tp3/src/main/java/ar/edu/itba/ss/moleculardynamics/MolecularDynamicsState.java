package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.List;

public class MolecularDynamicsState {

    private final List<MovableSurfaceEntity<Particle>> particles;

    private final MovableSurfaceEntity<Particle> ball;
    private final double time;

    public MolecularDynamicsState(List<MovableSurfaceEntity<Particle>> particles, MovableSurfaceEntity<Particle> ball, double time) {
        this.particles = particles;
        this.ball = ball;
        this.time = time;
    }

    public List<MovableSurfaceEntity<Particle>> getParticles() {
        return particles;
    }

    public double getTime() {
        return time;
    }

    public MovableSurfaceEntity<Particle> getBall() {
        return ball;
    }
}
