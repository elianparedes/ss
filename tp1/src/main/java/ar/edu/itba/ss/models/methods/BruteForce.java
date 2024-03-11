package ar.edu.itba.ss.models.methods;

import ar.edu.itba.ss.data.ParticleDataframe;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteForce {

    public static List<ParticleDataframe> calculate(List<SurfaceEntity<Particle>> particles, double rc){

        List<ParticleDataframe> results = new ArrayList<>();

        for (SurfaceEntity<Particle> particle:particles) {
            ParticleDataframe df = new ParticleDataframe(particle);

            for(SurfaceEntity<Particle> other:particles){
                double distance = (particle.distanceTo(other) - other.getEntity().getRadius()) - particle.getEntity().getRadius();
                if(distance <= rc && !particle.equals(other)){
                    df.addNeighbour(other, distance);
                }
            }

            results.add(df);
        }

        return results;
    }
}
