package ar.edu.itba.ss.output.ovito;

import ar.edu.itba.ss.data.ParticleDataframe;
import ar.edu.itba.ss.models.Cell;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SquareGrid;
import ar.edu.itba.ss.models.TraversalOffset;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.*;
import java.util.stream.Collectors;

public class OvitoDataFrame {
    private final ParticleDataframe particleDataframe;
    private final SquareGrid<Particle> grid;

    public OvitoDataFrame(ParticleDataframe particleDataframe, SquareGrid<Particle> grid) {
        this.particleDataframe = particleDataframe;
        this.grid = grid;
    }

    public Set<SurfaceEntity<Particle>> getNeighbours(){
        Set<SurfaceEntity<Particle>> neighbours = particleDataframe.getNeighbours().keySet();
        neighbours.remove(particleDataframe.getParticle());
        return neighbours;
    }

    public List<SurfaceEntity<Particle>> getNotNeighbours(List<SurfaceEntity<Particle>> particles){
        Set<SurfaceEntity<Particle>> neighbours = getNeighbours();
        List<SurfaceEntity<Particle>> notNeighbours = new ArrayList<>(particles);
        notNeighbours.removeAll(neighbours);
        notNeighbours.remove(particleDataframe.getParticle());
        return notNeighbours;
    }

    public Cell<Particle> getCell(){
        SurfaceEntity<Particle> particle = particleDataframe.getParticle();
        return grid.locate(particle.getX(),particle.getY());
    }

    public List<Cell<Particle>> getNeighbourCells(){
        return grid.getNeighbours(getCell(),TraversalOffset.L_NEIGHBOURS);
    }

    public List<Cell<Particle>> getNotNeighbourCells(){
        List<Cell<Particle>> notNeighbours = new ArrayList<>(getAllCells());
        notNeighbours.removeAll(getNeighbourCells());
        return notNeighbours;
    }

    public List<Cell<Particle>> getAllCells(){
        return grid.getCells().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public SurfaceEntity<Particle> getParticle(){
        return particleDataframe.getParticle();
    }

    public static Map<SurfaceEntity<Particle>, OvitoDataFrame> toOvitoDataframeMap(Map<SurfaceEntity<Particle>, ParticleDataframe> df, SquareGrid<Particle> grid){
        Map<SurfaceEntity<Particle>, OvitoDataFrame> map = new LinkedHashMap<>();
        for (Map.Entry<SurfaceEntity<Particle>,ParticleDataframe> entry: df.entrySet()) {
            map.put(entry.getKey(),new OvitoDataFrame(entry.getValue(),grid));
        }
        return map;
    }
}
