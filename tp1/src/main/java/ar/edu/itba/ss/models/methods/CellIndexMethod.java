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

        for (SurfaceEntity<Particle> currentParticle : particles) {
            Cell<Particle> cell = grid.locate(currentParticle.getX(), currentParticle.getY());
            List<Cell<Particle>> neighbourCells = grid.getPeriodicNeighbours(cell,TraversalOffset.L_NEIGHBOURS);
            ParticleDataframe currentParticleDf = results.getOrDefault(currentParticle, new ParticleDataframe(currentParticle));

            for (Cell<Particle> c: neighbourCells) {
                for (SurfaceEntity<Particle> neighbourCandidate : c.getEntities()) {
                    double distance = (neighbourCandidate.distanceTo(currentParticle) - neighbourCandidate.getEntity().getRadius()) - currentParticle.getEntity().getRadius();
                    if (distance <= rc && !currentParticle.equals(neighbourCandidate)) {
                        currentParticleDf.addNeighbour(neighbourCandidate, distance);

                        ParticleDataframe neighbourDf = results.getOrDefault(neighbourCandidate, new ParticleDataframe(neighbourCandidate));
                        neighbourDf.addNeighbour(currentParticle, distance);
                        results.put(neighbourCandidate, neighbourDf);
                    }
                }
            }

            results.put(currentParticle, currentParticleDf);
        }

        return results;
    }

    private static double calculatePeriodicDistance(SurfaceEntity<Particle> p1, SurfaceEntity<Particle> p2, SquareGrid<Particle> grid) {
        double dx = Math.abs(p1.getX() - p2.getX());
        double dy = Math.abs(p1.getY() - p2.getY());
        double size = grid.getSize();

        if (dx > size / 2) {
            dx = size - dx;
        }
        if (dy > size / 2) {
            dy = size - dy;
        }

        double radiusSum = p1.getEntity().getRadius() + p2.getEntity().getRadius();
        return Math.sqrt(dx * dx + dy * dy) - radiusSum;
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
