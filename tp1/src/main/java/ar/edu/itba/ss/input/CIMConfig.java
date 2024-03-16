package ar.edu.itba.ss.input;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CIMConfig {

    private int l, n, m;

    private double rc,r;
    public static CIMConfig getConfig(String file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(file), CIMConfig.class);
    }

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
