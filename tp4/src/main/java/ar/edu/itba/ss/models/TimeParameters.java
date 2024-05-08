package ar.edu.itba.ss.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeParameters {

    @JsonProperty("dt")
    private double dt;

    @JsonProperty("dt_units")
    private String dtUnits;

    @JsonProperty("stop")
    private double stop;

    @JsonProperty("stop_units")
    private String stopUnits;

    @JsonProperty("start")
    private double start;

    @JsonProperty("start_units")
    private String startUnits;

    public TimeParameters() {
    }

    public double getDt() {
        return TimeUnits.fromString(dtUnits).toSeconds(dt);
    }

    public String getDtUnits() {
        return dtUnits;
    }

    public double getStop() {
        double stop = TimeUnits.fromString(stopUnits).toSeconds(this.stop);
        double dt = getDt();
        if(dt > stop)
            throw new RuntimeException("dt must be less than stop");
        return stop;
    }

    public String getStopUnits() {
        return stopUnits;
    }

    public double getStart() {
        double start = TimeUnits.fromString(startUnits).toSeconds(this.start);
        double dt = getDt();
        if(dt > start)
            throw new RuntimeException("dt must be less than start");
        return start;
    }

    public String getStartUnits() {
        return startUnits;
    }
}
