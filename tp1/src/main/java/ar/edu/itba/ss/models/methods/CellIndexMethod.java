package ar.edu.itba.ss.models.methods;

import ar.edu.itba.ss.data.ParticleDataframe;
import ar.edu.itba.ss.models.Cell;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SquareGrid;
import ar.edu.itba.ss.models.TraversalOffset;
import ar.edu.itba.ss.models.entity.Entity;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.*;
import java.util.stream.Collectors;


public class CellIndexMethod {

    public static Map<SurfaceEntity<Particle>, ParticleDataframe> calculateWithRadius(int l, int m, double rc, final List<SurfaceEntity<Particle>> particles){
        SquareGrid<Particle> grid = new SquareGrid<>(l, m);
        for (SurfaceEntity<Particle> particle : particles) {
            grid.place(particle, particle.getX(),particle.getY(), particle.getEntity().getRadius());
            grid.place(particle);
        }

        return CellIndexMethod.cellIndexMethod(grid,rc);
    }
    public static Map<SurfaceEntity<Particle>, ParticleDataframe> calculate(int l, int m, int n, double rc, final List<SurfaceEntity<Particle>> particles) {

        SquareGrid<Particle> grid = new SquareGrid<>(l, m);

        for (SurfaceEntity<Particle> particle : particles) {
            grid.place(particle);
        }

        return CellIndexMethod.cellIndexMethod(grid,rc);
    }

    private static Map<SurfaceEntity<Particle>,ParticleDataframe> cellIndexMethod(SquareGrid<Particle> grid, double rc){
        Map<SurfaceEntity<Particle>, ParticleDataframe> results = new TreeMap<>();
        List<List<Cell<Particle>>> rows = grid.getCells();

        for (List<Cell<Particle>> columns : rows) {
            for (Cell<Particle> cell : columns) {
                for (SurfaceEntity<Particle> particle : cell.getEntities()) {
                    ParticleDataframe df = new ParticleDataframe(particle);

                    List<Cell<Particle>> neighbours = grid.getNeighbours(cell, TraversalOffset.L_NEIGHBOURS);

                    df.addNeighbourCells(neighbours);

                    List<SurfaceEntity<Particle>> neighbourCellsParticles =
                            neighbours.stream().flatMap(c -> c.getEntities().stream()).collect(Collectors.toList());

                    neighbourCellsParticles.addAll(cell.getEntities());
                    neighbourCellsParticles.remove(particle);

                    for (SurfaceEntity<Particle> neighbour : neighbourCellsParticles) {
                        double distance = (particle.distanceTo(neighbour) - neighbour.getEntity().getRadius()) - particle.getEntity().getRadius();
                        if (distance <= rc && !particle.equals(neighbour)) {
                            df.addNeighbour(neighbour, distance);
                            ParticleDataframe neighbourDf = results.getOrDefault(neighbour, new ParticleDataframe(neighbour));
                            neighbourDf.addNeighbour(particle, distance);
                        }
                    }

                    for (List<Cell<Particle>> cols : rows) {
                        for (Cell<Particle>  c : cols){
                            df.addCell(c);
                        }
                    }

                    results.put(particle, df);
                }
            }
        }

        return results;

        //TODO: Esto estaba para devolver en orden, deberia no ser necesario
        //Map<Particle, ParticleDataframe> newResult = new LinkedHashMap<>();
        //for (SurfaceEntity<Particle> particle : particles) {
        //    newResult.put(particle.getEntity(), results.get(particle.getEntity()));
        //}

        //return newResult;
    }

    public static void calculateForSingle(int l, int m, int n, double r, double rc, List<SurfaceEntity<Particle>> particles) {

        SquareGrid<Particle> grid = new SquareGrid<>(l, m);

        for (SurfaceEntity<Particle> particle : particles) {
            grid.place(particle.getEntity(), particle.getX(), particle.getY());
        }

        SurfaceEntity<Particle> particle = particles.get(0);

        System.out.printf("search for: %s\n\n", particle);

        Cell<Particle> cell = grid.locate(particle.getX(), particle.getY());

        System.out.printf("found cell: %s\n\n", cell);

        List<Cell<Particle>> neighbours = grid.getNeighbours(cell);

        System.out.printf("cell neighbours: %s\n\n",    Arrays.toString(neighbours.toArray()));

        List<SurfaceEntity<Particle>> neighbourCellsParticles =
                neighbours.stream().flatMap(c -> c.getEntities().stream()).collect(Collectors.toList());

        System.out.printf("neighbours cells particles: %s\n\n",
                Arrays.toString(neighbourCellsParticles.toArray())
        );

        List<SurfaceEntity<Particle>> neighbourParticles = new ArrayList<>();

        for (SurfaceEntity<Particle> neighbour : neighbourCellsParticles) {
            if (particle.distanceTo(neighbour) + particle.getEntity().getRadius() <= rc)
                neighbourParticles.add(neighbour);
        }

        System.out.printf("neighbours particles: %s\n\n",
                Arrays.toString(neighbourParticles.toArray())
        );

        grid.print();

    }


}
