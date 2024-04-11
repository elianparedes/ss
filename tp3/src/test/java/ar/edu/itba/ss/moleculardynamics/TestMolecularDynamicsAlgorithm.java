package ar.edu.itba.ss.moleculardynamics;

import org.junit.Test;

public class TestMolecularDynamicsAlgorithm {

    private static final int N = 200;
    private static final double L = 0.1;

    private static final double RP = 0.001;

    private static final double MASS = 1;

    private static final double SPEED = 1;

    private static final int MAX_IT= 3000;

    @Test
    public void testAlgorithmCollisionTime(){
        MolecularDynamicsParameters parameters = new MolecularDynamicsParameters(MolecularDynamicsAlgorithm.generateRandomParticles(L,N,RP,SPEED,MASS),MAX_IT);
        MolecularDynamicsAlgorithm algorithm = new MolecularDynamicsAlgorithm();
        algorithm.calculate(parameters,event -> {});
    }
}
