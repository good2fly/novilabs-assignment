package nlhomework.io;

import java.io.IOException;
import java.io.Writer;

/**
 * Produce a {@link Writer} instance.
 */
public interface WriterFactory {
    Writer buildWriter() throws IOException;
}
