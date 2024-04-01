package ar.edu.itba.ss.bf;

import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;

import java.util.List;

public class BruteForceParameters extends AlgorithmParameters {
    public List<? extends SurfaceEntity<Particle>> particles;
    public double rc;

    public double l;

    public BruteForceParameters(List<? extends SurfaceEntity<Particle>> particles, double rc, double l) {
        this.particles = particles;
        this.rc = rc;
        this.l = l;
    }
}
