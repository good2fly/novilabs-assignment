package nlhomework.io;

import java.io.IOException;
import java.io.Writer;

/**
 * Produce a {@link Writer} instance.
 */
@FunctionalInterface
public interface CsvWriterFactory {
    Writer buildWriter() throws IOException;
}
