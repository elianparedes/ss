package ar.edu.itba.ss.offLatice;


import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;

import java.util.List;

public class OffLaticeParameters extends AlgorithmParameters {
    public final CellIndexMethodParameters cimParameters;
    public static final double SPEED = 0.3;

    public static final int MAX_ITERATIONS = 10;

    public final List<MovableSurfaceEntity<Particle>> particles;

    public final double ETHA;

    public OffLaticeParameters(CellIndexMethodParameters cimParameters, List<MovableSurfaceEntity<Particle>> particles, double etha) {
        this.cimParameters = cimParameters;
        this.particles = particles;
        this.ETHA = etha;
    }
}
