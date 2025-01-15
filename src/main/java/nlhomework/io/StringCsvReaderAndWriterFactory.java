package nlhomework.io;

import java.io.*;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Implementation of the {@link CsvReaderFactory} and {@link CsvWriterFactory} interfaces that creates
 * readers and writers that read from and write to strings, respectively.
 */
public class StringCsvReaderAndWriterFactory implements CsvReaderFactory, CsvWriterFactory {

    private final String inputString;
    private final Consumer<StringBuffer> outputStringConsumer;

    public StringCsvReaderAndWriterFactory(String inputString, Consumer<StringBuffer> outputStringConsumer) {
        Objects.requireNonNull(inputString, "inputString must not be null");

        this.inputString = inputString;
        this.outputStringConsumer = outputStringConsumer;
    }

    @Override
    public Reader buildReader() throws IOException {
        return new StringReader(inputString);
    }

    @Override
    public Writer buildWriter() throws IOException {
        var writer = new StringWriter();
        if (outputStringConsumer != null) outputStringConsumer.accept(writer.getBuffer());
        return writer;
    }
}
