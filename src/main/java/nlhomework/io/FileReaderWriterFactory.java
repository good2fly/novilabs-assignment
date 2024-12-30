package nlhomework.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Implementation of the {@link ReaderFactory} and {@link WriterFactory} interfaces that creates
 * readers and writers that read from and write to files, respectively.
 */
public class FileReaderWriterFactory implements ReaderFactory, WriterFactory {

    private final String inputFileName;
    private final String outputFileName;

    public FileReaderWriterFactory(String inputFileName, String outputFileName) {
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
