package ar.edu.itba.ss.moleculardynamics;

import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MolecularDynamicsParameters extends AlgorithmParameters {

    public List<MovableSurfaceEntity<Particle>> particles;

    public List<SurfaceEntity<Border>> fixedObjects;

    public MovableSurfaceEntity<Particle> ball;

    @JsonProperty("movable")
    public boolean movable;

    @JsonProperty("iterations")
    public int maxIterations;

    @JsonProperty("n")
    public int n;
    @JsonProperty("l")
    public double l;

    @JsonProperty("rp")
    public double rp;

    @JsonProperty("rb")
    public double rb;

    @JsonProperty("mass_p")
    public double massP;

    @JsonProperty("mass_b")
    public double massB;

    @JsonProperty("speed")
    public double speed;

    public MolecularDynamicsParameters() {
    }

    public MolecularDynamicsParameters(boolean movable, int maxIterations, int n, double l, double rp, double rb, double massP, double massB, double speed) {
        this.movable = movable;
        this.maxIterations = maxIterations;
        this.n = n;
        this.l = l;
        this.rp = rp;
        this.rb = rb;
        this.massP = massP;
        this.massB = massB;
        this.speed = speed;
    }

}
