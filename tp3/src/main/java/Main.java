import ar.edu.itba.ss.MolecularDynamics;
import ar.edu.itba.ss.MolecularParams;
import ar.edu.itba.ss.Particle;

public class Main {
    public static void main(String[] args) {
        MolecularDynamics molecularDynamics = new MolecularDynamics();

        Particle fixedParticle = new Particle(0.1/2,0.1/2,0,0,0.005,1);

        MolecularParams params = new MolecularParams(200,0.1,1,1,0.001,1000, fixedParticle, 1, 1);
        molecularDynamics.calculate(params,null);
    }
}
