package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.List;

public class MolecularDynamicsParameters extends AlgorithmParameters {

    public List<MovableSurfaceEntity<Particle>> particles;
    public  int maxIterations;

    public MolecularDynamicsParameters(List<MovableSurfaceEntity<Particle>> particles, int maxIterations) {
        this.particles = particles;
        this.maxIterations = maxIterations;
    }
}
