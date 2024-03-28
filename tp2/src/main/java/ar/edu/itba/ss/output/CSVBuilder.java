package ar.edu.itba.ss.output;

import com.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVBuilder {

    private final List<String[]> lines = new ArrayList<>();

    public CSVBuilder addLine(String... values) {
        lines.add(values);
        return this;
    }

    public void build(String filePath) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(filePath);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             CSVWriter writer = new CSVWriter(osw)) {
            writer.writeAll(lines);
        } finally {
            lines.clear();
        }
    }
}
