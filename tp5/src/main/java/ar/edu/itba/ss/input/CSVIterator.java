package ar.edu.itba.ss.input;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CSVIterator implements Iterator<String[]> {

    private CSVReader csvReader;
    private String[] nextLine;

    public CSVIterator(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        this.csvReader = new CSVReader(bufferedReader);
        try {
            // Skip the header row
            csvReader.readNext();
            // Read the first data row
            this.nextLine = csvReader.readNext();
        } catch (CsvValidationException e) {
            throw new IOException("Error initializing CSVReader", e);
        }
    }
    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public String[] next() {
        if (nextLine == null) {
            throw new NoSuchElementException();
        }
        String[] currentLine = nextLine;
        try {
            nextLine = csvReader.readNext();
        } catch (IOException | CsvValidationException e) {
            nextLine = null;
        }
        return currentLine;
    }

    public void close() throws IOException {
        csvReader.close();
    }

}
