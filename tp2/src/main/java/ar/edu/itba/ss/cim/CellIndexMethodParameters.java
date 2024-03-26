package ar.edu.itba.ss.cim;

import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CellIndexMethodParameters extends AlgorithmParameters {

    @JsonProperty("l")
    public int l;
    @JsonProperty("m")
    public int m;

    @JsonProperty("n")
    public int n;

    @JsonProperty("rc")
    public double rc;

    @JsonProperty("r")
    public double r;

    public List<? extends SurfaceEntity<Particle>> particles;

    public CellIndexMethodParameters() {
    }

    public CellIndexMethodParameters(int l, int m, int n, double rc, double r, final List<? extends SurfaceEntity<Particle>> particles) {
        this.l = l;
        this.m = m;
        this.n = n;
        this.rc = rc;
        this.r = r;
        this.particles = particles;
    }
}
