package ar.edu.itba.ss.offLatice;


import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OffLaticeParameters extends AlgorithmParameters {
    @JsonProperty("cim")
    public CellIndexMethodParameters cimParameters;
    @JsonProperty("speed")
    public double speed;

    @JsonProperty("max_iterations")
    public  int maxIter;

    public List<MovableSurfaceEntity<Particle>> particles;
    public boolean hasPeriodicBoundaryConditions = true;

    @JsonProperty("etha")
    public double etha;

    public OffLaticeParameters() {
    }

    public OffLaticeParameters(OffLaticeParameters offLaticeParameters) {
        this.etha = offLaticeParameters.etha;
        this.maxIter = offLaticeParameters.maxIter;
        this.speed = offLaticeParameters.speed;
        this.cimParameters = new CellIndexMethodParameters(offLaticeParameters.cimParameters);
        this.particles = new ArrayList<>(offLaticeParameters.particles);
        this.hasPeriodicBoundaryConditions = offLaticeParameters.hasPeriodicBoundaryConditions;
    }


    public OffLaticeParameters(CellIndexMethodParameters cimParameters, List<MovableSurfaceEntity<Particle>> particles, double etha) {
        this.cimParameters = cimParameters;
        this.particles = particles;
        this.etha = etha;
    }
}
