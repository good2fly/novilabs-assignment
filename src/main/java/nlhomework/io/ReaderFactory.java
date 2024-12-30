package nlhomework.io;

import java.io.IOException;
import java.io.Reader;

/**
 * Produce a {@link Reader} instance.
 */
public interface ReaderFactory {
    Reader buildReader() throws IOException;
}
