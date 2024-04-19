package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.utils.entity.Entity;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Ball;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.List;

public class MolecularDynamicsParameters extends AlgorithmParameters {

    public List<MovableSurfaceEntity<Particle>> particles;

    public List<SurfaceEntity<Border>> fixedObjects;

    public MovableSurfaceEntity<Particle> ball;

    public boolean movable;

    public  int maxIterations;

    public MolecularDynamicsParameters(List<MovableSurfaceEntity<Particle>> particles,  List<SurfaceEntity<Border>> fixedObjects, MovableSurfaceEntity<Particle> ball, int maxIterations, boolean movable) {
        this.particles = particles;
        this.fixedObjects = fixedObjects;
        this.maxIterations = maxIterations;
        this.ball = ball;
        this.movable = movable;
    }
}
