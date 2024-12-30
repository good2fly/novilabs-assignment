package nlhomework.io;

import java.io.*;
import java.util.function.Consumer;

/**
 * Implementation of the {@link ReaderFactory} and {@link WriterFactory} interfaces that creates
 * readers and writers that read from and write to strings, respectively.
 */
public class StringReaderWriterFactory implements ReaderFactory, WriterFactory {

    private final String inputString;
    private final Consumer<StringBuffer> outputStringConsumer;

    public StringReaderWriterFactory(String inputString, Consumer<StringBuffer> outputStringConsumer) {
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
        outputStringConsumer.accept(writer.getBuffer());
        return writer;
    }
}
