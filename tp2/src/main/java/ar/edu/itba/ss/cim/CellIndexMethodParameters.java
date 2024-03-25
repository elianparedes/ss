package ar.edu.itba.ss.cim;

import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;

import java.util.List;

public class CellIndexMethodParameters extends AlgorithmParameters {

    public final int l;
    public final int m;
    public final int n;

    public final double rc;

    public final List<? extends SurfaceEntity<Particle>> particles;


    public CellIndexMethodParameters(int l, int m, int n, double rc, final List<? extends SurfaceEntity<Particle>> particles) {
        this.l = l;
        this.m = m;
        this.n = n;
        this.rc = rc;
        this.particles = particles;
    }
}
