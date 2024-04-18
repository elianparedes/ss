import ar.edu.itba.ss.MolecularDynamics;
import ar.edu.itba.ss.MolecularParams;

public class Main {
    public static void main(String[] args) {
        MolecularDynamics molecularDynamics = new MolecularDynamics();

        MolecularParams params = new MolecularParams(200,0.1,1,1,0.001,1000);
        molecularDynamics.calculate(params,null);
    }
}
