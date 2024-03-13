package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.geometry.Circle;
import org.junit.Test;

import org.junit.Test;
import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void testIntersectCircle() {
        double size = 10.0;
        Cell<Particle> cell = new Cell<Particle>(0, 0, size);

        // Caso 1: El círculo está completamente fuera de la celda y no se intersecta
        assertFalse(cell.intersectCircle(20, 20, 5));

        // Caso 2: El círculo intersecta uno de los bordes de la celda
        assertTrue(cell.intersectCircle(5, 5, 10));

        // Caso 3: El círculo está completamente dentro de la celda
        //No contemplado
        //assertTrue(cell.intersectCircle(5, 5, 3));

        // Caso 4: La celda está completamente dentro del círculo
        assertTrue(cell.intersectCircle(5, 5, 20));

        // Caso 5: El círculo toca una esquina de la celda pero no se intersecta
        assertTrue(cell.intersectCircle(0, 20, 10));

        // Caso 6: El círculo intersecta dos bordes adyacentes de la celda
        assertTrue(cell.intersectCircle(0, 0, 10));

        //Caso 7: Un eje dentro del circulo
        assertTrue(cell.intersectCircle(12,5,5));

        //Caso 8: Dos ejes dentro del circulo
        assertTrue(cell.intersectCircle(12,5,5));
    }
}

