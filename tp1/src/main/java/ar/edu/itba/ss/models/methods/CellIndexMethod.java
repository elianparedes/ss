package ar.edu.itba.ss.models.methods;

import ar.edu.itba.ss.models.Cell;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SquareGrid;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CellIndexMethod {

    public static void calculate(int l, int m, int n, double r, double rc, List<SurfaceEntity<Particle>> particles) {

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
                neighbours.stream().flatMap(c -> c.getEntities().stream()).toList();

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
