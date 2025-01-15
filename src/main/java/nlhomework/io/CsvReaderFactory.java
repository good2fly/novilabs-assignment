package nlhomework.io;

import java.io.IOException;
import java.io.Reader;

/**
 * Produce a {@link Reader} instance.
 */
@FunctionalInterface
public interface CsvReaderFactory {
    Reader buildReader() throws IOException;
}
