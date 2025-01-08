package nlhomework.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringColumnProcessorTest {

    private StringColumnProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new StringColumnProcessor();
    }

    @Test
    void testProcessValidDecimal() {
        String[] row1 = { "foobar" };
        String[] row2 = { "" };
        assertEquals(row1[0], processor.processColumn(0, 0, row1));
        assertEquals(row2[0], processor.processColumn(1, 0, row2));
    }

    @Test
    void testProcessEmptyValue() {
        String[] row = { null };
        String result = processor.processColumn(0, 0, row);
        assertNull(result);
    }
}
    