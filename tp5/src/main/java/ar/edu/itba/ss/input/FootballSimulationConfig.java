package ar.edu.itba.ss.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FootballSimulationConfig {

    @JsonProperty("lunatic_mass")
    private double lunaticMass;

    @JsonProperty("lunatic_radius")
    private double lunaticRadius;

    @JsonProperty("desired_speed")
    private double desiredSpeed;

    @JsonProperty("tau")
    private double tau;

    public FootballSimulationConfig() {
    }

    public double getLunaticMass() {
        return lunaticMass;
    }

    public double getLunaticRadius() {
        return lunaticRadius;
    }

    public double getDesiredSpeed() {
        return desiredSpeed;
    }

    public double getTau() {
        return tau;
    }

    public void setTau(double tau) {
        this.tau = tau;
    }

    public void setDesiredSpeed(double desiredSpeed) {
        this.desiredSpeed = desiredSpeed;
    }
}
