package ar.edu.itba.ss;

import java.util.List;

public class MolecularState {

    private List<Particle> particles;

    private double time;

    public MolecularState(List<Particle> particles, double time) {
        this.particles = particles;
        this.time = time;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public double getTime() {
        return time;
    }
}
