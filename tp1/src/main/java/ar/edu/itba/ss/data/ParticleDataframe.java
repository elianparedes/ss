package ar.edu.itba.ss.data;

import ar.edu.itba.ss.models.Cell;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.*;
import java.util.stream.Collectors;

public class ParticleDataframe {

    private final SurfaceEntity<Particle> particle;
    private final Map<SurfaceEntity<Particle>,Double> neighbours;
    private final List<Cell<Particle>> cells;

    private final List<Cell<Particle>> neighbourCells;

    public ParticleDataframe(SurfaceEntity<Particle> particle){
        this.particle = particle;
        this.neighbours = new HashMap<>();
        this.cells = new ArrayList<>();
        this.neighbourCells = new ArrayList<>();
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

    public List<SurfaceEntity<Particle>> getNotNeighbours(List<SurfaceEntity<Particle>> particles) {
        Set<SurfaceEntity<Particle>> neighbours = new HashSet<>(this.neighbours.keySet());
        List<SurfaceEntity<Particle>> notNeighbours = new ArrayList<>(particles);
        notNeighbours.removeAll(neighbours);

        return new ArrayList<>(notNeighbours);
    }

    public void addCell(Cell<Particle> cell){
        this.cells.add(cell);
    }

    public void addCells(List<Cell<Particle>> cells) {
        this.cells.addAll(cells);
    }

    public void addNeighbourCell(Cell<Particle> cell){
        this.neighbourCells.add(cell);
    }

    public void addNeighbourCells(List<Cell<Particle>> cells) {
        this.neighbourCells.addAll(cells);
    }

    public List<Cell<Particle>> getCells() {
        List<Cell<Particle>> otherCells = new ArrayList<>(cells);
        otherCells.removeAll(neighbourCells);
        return otherCells;
    }

    public List<Cell<Particle>> getNeighbourCells() {
        return neighbourCells;
    }

    @Override
    public String toString(){
        String neighboursStr = neighbours.keySet().stream().map(p->p.getEntity().getId().toString())
                .collect(Collectors.joining(" "));
        return String.format("[%d%s%s]",particle.getEntity().getId(),neighbours.keySet().isEmpty() ? "":" ", neighboursStr);
    }
}
