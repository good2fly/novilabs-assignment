package nlhomework.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryColumnProcessorTest {

    private CategoryColumnProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new CategoryColumnProcessor();
    }

    @Test
    void testProcessValidCategory() {
        String[] row = { "A" };
        String result = processor.processColumn(0, 0, row);
        assertEquals("A", result);
    }

    @Test
    void testReplacementValue() {
        processor.processColumn(0, 0, new String[]{ "A" });
        processor.processColumn(1, 0, new String[]{ "B" });
        processor.processColumn(1, 0, new String[]{ "C" });
        processor.processColumn(2, 0, new String[]{ "A" });
        String replacement = processor.getReplacementValue();
        assertEquals("A", replacement);
    }

    @Test
    void testProcessEmptyValue() {
        String[] row = { null };
        String result = processor.processColumn(0, 0, row);
        assertNull(result);
    }
}
