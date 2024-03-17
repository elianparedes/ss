package ar.edu.itba.ss.data;

import ar.edu.itba.ss.models.Cell;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.*;
import java.util.stream.Collectors;

public class ParticleDataframe {

    private final SurfaceEntity<Particle> particle;
    private final Map<SurfaceEntity<Particle>,Double> neighbours;

    public ParticleDataframe(SurfaceEntity<Particle> particle){
        this.particle = particle;
        this.neighbours = new HashMap<>();
    }

    public SurfaceEntity<Particle> getParticle() {
        return particle;
    }

    public Map<SurfaceEntity<Particle>, Double> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(final SurfaceEntity<Particle> neighbour, final double distance){
        neighbours.put(neighbour,distance);
    }

    public static String collectionToString(Collection<ParticleDataframe> collection){
        StringBuilder builder = new StringBuilder();
        for (ParticleDataframe df:collection) {
            builder.append(df.toString());
            builder.append('\n');
        }
        return builder.toString();
    }

    @Override
    public String toString(){
        String neighboursStr = neighbours.keySet().stream().map(p->p.getEntity().getId().toString())
                .collect(Collectors.joining(" "));
        return String.format("[%d%s%s]",particle.getEntity().getId(),neighbours.keySet().isEmpty() ? "":" ", neighboursStr);
    }
}
