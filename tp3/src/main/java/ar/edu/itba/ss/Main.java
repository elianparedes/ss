package ar.edu.itba.ss;

import ar.edu.itba.ss.moleculardynamics.MolecularDynamicsAlgorithm;
import ar.edu.itba.ss.moleculardynamics.MolecularDynamicsParameters;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.geometry.Point;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int N = 50;
    private static final double L = 0.1;

    private static final double RP = 0.001;

    private static final double MASS = 1;

    private static final double SPEED = 1;

    private static final int MAX_IT = 3000;

    public static void main(String[] args) {

        List<SurfaceEntity<Border>> fixedObjects = new ArrayList<>();
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(0, 0), new Point(0, L)), 0, 0));
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(L, 0), new Point(L, L)), L, L / 2));
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(0, 0), new Point(L, 0)), L / 2, 0));
        fixedObjects.add(new SurfaceEntity<>(new Border(new Point(0, L), new Point(L, L)), L / 2, L));

        List<MovableSurfaceEntity<Particle>> particles = MolecularDynamicsAlgorithm.generateRandomParticles(
                L, N, RP, SPEED, MASS
        );

        MolecularDynamicsParameters params = new MolecularDynamicsParameters(
                particles,
                fixedObjects,
                1000
        );

        MolecularDynamicsAlgorithm algorithm = new MolecularDynamicsAlgorithm();
        algorithm.calculate(params, (e) -> {});
    }
}