package ar.edu.itba.ss.models;

public enum TimeUnits {
    SECONDS("s"){
        @Override
        public double toSeconds(double value) {
            return value;
        }
    },
    MINUTES("m") {
        @Override
        public double toSeconds(double value) {
            return value * 60;
        }
    },
    HOURS("h"){
        @Override
        public double toSeconds(double value) {
            return value * 3600;
        }
    },
    DAYS("d"){
        @Override
        public double toSeconds(double value) {
            return value * 86400;
        }
    },
    YEARS("y"){
        @Override
        public double toSeconds(double value) {
            return value * 31536000;
        }
    };

    private final String unit;

    TimeUnits(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public static TimeUnits fromString(String unit) {
        for (TimeUnits timeUnit : TimeUnits.values()) {
            if (timeUnit.unit.equals(unit)) {
                return timeUnit;
            }
        }
        throw new IllegalArgumentException("Invalid time unit: " + unit);
    }

    public abstract double toSeconds(double value);
}
