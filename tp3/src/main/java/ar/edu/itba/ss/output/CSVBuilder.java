package ar.edu.itba.ss.output;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVBuilder {

    private final List<String[]> lines = new ArrayList<>();
    private CSVWriter writer;

    public CSVBuilder addLine(String... values) {
        lines.add(values);
        return this;
    }

    public void build(String filePath) throws IOException {
        initializeWriter(filePath);
        try {
            writer.writeAll(lines);
        } finally {
            closeWriter();
            lines.clear();
        }
    }

    public void appendLine(String filePath, String... values) throws IOException {
        initializeWriter(filePath, true);
        try {
            writer.writeNext(values);
        } finally {
            closeWriter();
        }
    }

    private void initializeWriter(String filePath) throws IOException {
        initializeWriter(filePath, false);
    }

    private void initializeWriter(String filePath, boolean append) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(filePath, append);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        writer = new CSVWriter(osw);
    }

    private void closeWriter() throws IOException {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }
}
