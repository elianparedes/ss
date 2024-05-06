package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;

import java.util.List;

public class MarsVoyageState {

    private final List<Particle> particlesState;
    private final double dt;
    private final double time;

    public MarsVoyageState(List<Particle> particlesState, double dt, double time) {
        this.particlesState = particlesState;
        this.dt = dt;
        this.time = time;
    }

    public List<Particle> getParticlesState() {
        return particlesState;
    }

    public double getDt() {
        return dt;
    }

    public double getTime() {
        return time;
    }
}
