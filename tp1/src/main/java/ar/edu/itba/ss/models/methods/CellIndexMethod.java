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

    public static Map<SurfaceEntity<Particle>, ParticleDataframe> calculate(int l, int m, double rc, final List<SurfaceEntity<Particle>> particles){
        SquareGrid<Particle> grid = new SquareGrid<>(l, m);

        for (SurfaceEntity<Particle> particle:particles) {
            grid.place(particle);
        }

        return CellIndexMethod.cellIndexMethod(particles,grid,rc);
    }

    private static Map<SurfaceEntity<Particle>,ParticleDataframe> cellIndexMethod(List<SurfaceEntity<Particle>> particles, SquareGrid<Particle> grid ,double rc){
        Map<SurfaceEntity<Particle>, ParticleDataframe> results = new LinkedHashMap<>();

        for (SurfaceEntity<Particle> p: particles) {
            Cell<Particle> cell = grid.locate(p.getX(),p.getY());
            List<Cell<Particle>> cells = grid.getNeighbours(cell,TraversalOffset.L_NEIGHBOURS);
            ParticleDataframe df = results.getOrDefault(p, new ParticleDataframe(p));

            for (Cell<Particle> c: cells) {
                for (SurfaceEntity<Particle> particle:c.getEntities()) {
                    double distance = (particle.distanceTo(p) - particle.getEntity().getRadius()) - p.getEntity().getRadius();
                    if (distance <= rc && !p.equals(particle)) {
                        df.addNeighbour(particle, distance);
                        ParticleDataframe neighbourDf = results.getOrDefault(particle, new ParticleDataframe(particle));
                        neighbourDf.addNeighbour(particle, distance);
                        results.putIfAbsent(particle, neighbourDf);
                    }
                }
            }

            results.putIfAbsent(p, df);
        }

        return results;
    }

    public static void calculateForSingle(int l, int m, int n, double r, double rc, List<SurfaceEntity<Particle>> particles) {

        SquareGrid<Particle> grid = new SquareGrid<>(l, m);

        for (SurfaceEntity<Particle> particle : particles) {
            grid.place(particle);
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
