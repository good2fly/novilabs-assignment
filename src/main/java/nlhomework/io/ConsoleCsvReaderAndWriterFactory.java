package nlhomework.io;

import java.io.*;

/**
 * Implementation of the {@link CsvReaderFactory} and {@link CsvWriterFactory} interfaces that creates
 * readers and writers that read from and write to the console, respectively.
 */
public class ConsoleCsvReaderAndWriterFactory implements CsvReaderFactory, CsvWriterFactory {

    @Override
    public Reader buildReader() throws IOException {
        return new InputStreamReader(System.in);
    }

    @Override
    public Writer buildWriter() throws IOException {
        return new OutputStreamWriter(System.out);
    }
}
