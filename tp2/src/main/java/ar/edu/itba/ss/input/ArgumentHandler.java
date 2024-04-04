package ar.edu.itba.ss.input;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ArgumentHandler {
    private static class Argument {
        Function<String, Boolean> validator;
        boolean optional;
        String defaultValue;

        Argument(Function<String, Boolean> validator, boolean optional, String defaultValue) {
            this.validator = validator;
            this.optional = optional;
            this.defaultValue = defaultValue;
        }
    }

    private final Map<String, Argument> argumentValidators = new HashMap<>();
    private final Map<String, String> parsedArguments = new HashMap<>();

    public ArgumentHandler addArgument(String name, Function<String, Boolean> validator, boolean optional, String defaultValue) {
        argumentValidators.put(name, new Argument(validator, optional, defaultValue));
        return this;
    }

    public boolean parse(String[] args) {
        for (String arg : args) {
            String[] parts = arg.split("=");
            if (parts.length != 2 || !argumentValidators.containsKey(parts[0])) {
                return false;
            }
            if (!argumentValidators.get(parts[0]).validator.apply(parts[1])) {
                return false;
            }
            parsedArguments.put(parts[0], parts[1]);
        }

        for (Map.Entry<String, Argument> entry : argumentValidators.entrySet()) {
            String key = entry.getKey();
            Argument argument = entry.getValue();
            if (!parsedArguments.containsKey(key) && !argument.optional) {
                return false;
            }
            if (!parsedArguments.containsKey(key) && argument.optional && argument.defaultValue != null) {
                parsedArguments.put(key, argument.defaultValue);
            }
        }

        return true;
    }

    public String getArgument(String name) {
        return parsedArguments.get(name);
    }

    public double getDoubleArgument(String name) {
        return Double.parseDouble(parsedArguments.get(name));
    }

    public int getIntArgument(String name) {
        return Integer.parseInt(parsedArguments.get(name));
    }

    public static boolean validateDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
