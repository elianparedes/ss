package ar.edu.itba.ss.input;

import ar.edu.itba.ss.Main;
import ar.edu.itba.ss.models.geometry.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DynamicFile {
    private double time = 0;
    private final List<Point> positions = new ArrayList<>();

    public DynamicFile(String filename) throws FileNotFoundException {

        if(filename == null)
            throw  new FileNotFoundException();

        Optional<URL> resourceOptional = Optional.ofNullable(Main.class.getClassLoader().getResource("input/"+filename));
        String path = resourceOptional.map(URL::getFile)
                .orElseThrow(FileNotFoundException::new);
        Scanner scanner = new Scanner(new File(path));

        if (scanner.hasNextLine()) {
            time = Double.parseDouble(scanner.nextLine().trim());
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2) {
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                positions.add(new Point(x, y));
            }
        }
    }

    public double getTime() {
        return time;
    }

    public List<Point> getPositions() {
        return positions;
    }
}
