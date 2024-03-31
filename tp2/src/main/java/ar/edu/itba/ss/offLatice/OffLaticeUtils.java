package ar.edu.itba.ss.offLatice;

import ar.edu.itba.ss.cim.CellIndexMethodParameters;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.offLatice.entity.MovableSurfaceEntity;

import java.util.ArrayList;
import java.util.List;

public class OffLaticeUtils {
    public static List<MovableSurfaceEntity<Particle>> initializeParticles(OffLaticeParameters parameters){
        CellIndexMethodParameters cimParameters = parameters.cimParameters;
        List<MovableSurfaceEntity<Particle>> particles = new ArrayList<>();
        for (int i = 0; i < cimParameters.n; i++) {
            double x = Math.random() * cimParameters.l;
            double y = Math.random() * cimParameters.l;

            particles.add(new MovableSurfaceEntity<>(new Particle(cimParameters.r),
                    x,
                    y,
                    parameters.speed,
                    Math.random() * 2 * Math.PI));
        }
        return particles;
    }
}
