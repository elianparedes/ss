package ar.edu.itba.ss.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CIMConfig {


    private ResultsConfig results;
    private ParametersConfig parameters;

    public static CIMConfig getConfig(String file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(file), CIMConfig.class);
    }

    public ResultsConfig getResults() {
        return results;
    }

    public ParametersConfig getParameters() {
        return parameters;
    }

    public static class ResultsConfig{
        private boolean time;
        private boolean ovito_file;
        private List<String> algorithms;
        private String output_folder;

        public boolean isTime() {
            return time;
        }

        public boolean isOvito_file() {
            return ovito_file;
        }

        public String getOutput_folder() {
            return output_folder;
        }

        public List<String> getAlgorithms() {
            return algorithms;
        }
    }
    public static class ParametersConfig{
        private int l, n, m;

        private double rc,r;

        public int getL() {
            return l;
        }

        public int getN() {
            return n;
        }

        public int getM() {
            return m;
        }

        public double getRc() {
            return rc;
        }

        public double getR() {
            return r;
        }
    }


}
