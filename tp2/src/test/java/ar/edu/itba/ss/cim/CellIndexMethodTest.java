package ar.edu.itba.ss.cim;

import ar.edu.itba.ss.cim.entity.SurfaceEntity;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;

public class CellIndexMethodTest {

    private List<SurfaceEntity<Particle>> particles = new ArrayList<>();
    @Before
    public void setUp() throws Exception {

        String fileName = "Dynamic100.txt"; // Reemplaza con el nombre de tu archivo dentro de resources

        InputStream inputStream = CellIndexMethodTest.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            System.err.println("No se pudo encontrar el archivo: " + fileName);
            return;
        }

        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split(" "); // Asumimos que las columnas están separadas por un espacio
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                particles.add(new SurfaceEntity<>(new Particle(0.37), x, y));
            }

        }
    }

    @Test
    public void testCellIndexMethod() {
        CellIndexMethod cim = new CellIndexMethod();
        CellIndexMethodParameters params = new CellIndexMethodParameters(100,14,100,6,0.37,particles);
        EventsQueue queue = new EventsQueue();
        cim.calculate(params, queue::add);

        for (Event<?> e:queue) {
            CIMNeighboursMap map = (CIMNeighboursMap) e.getPayload();
            for (Map.Entry<SurfaceEntity<Particle>,Set<SurfaceEntity<Particle>>> entry:map.getParticlesNeighbours().entrySet()) {
                StringBuilder builder = new StringBuilder();
                builder.append(entry.getKey().getEntity().getId());
                entry.getValue().forEach((neighbour)->builder.append(' ').append(neighbour.getEntity().getId()));
                System.out.println(builder);
            }
        }
    }
}
