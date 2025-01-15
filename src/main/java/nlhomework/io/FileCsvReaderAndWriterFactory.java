package nlhomework.io;

import java.io.*;

/**
 * Implementation of the {@link CsvReaderFactory} and {@link CsvWriterFactory} interfaces that creates
 * readers and writers that read from and write to files, respectively.
 */
public class FileCsvReaderAndWriterFactory implements CsvReaderFactory, CsvWriterFactory {

    private final String inputFileName;
    private final String outputFileName;

    public FileCsvReaderAndWriterFactory(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    @Override
    public Reader buildReader() throws IOException {
        return new FileReader(inputFileName);
    }

    @Override
    public Writer buildWriter()  throws IOException {
        return new FileWriter(outputFileName);
    }
}
