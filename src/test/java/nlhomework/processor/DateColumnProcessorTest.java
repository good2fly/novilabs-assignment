package nlhomework.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DateColumnProcessorTest {

    private DateColumnProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new DateColumnProcessor();
    }

    @Test
    void testProcessValidDecimal() {
        String[] row1 = { "2021-12-31" };
        String[] row2 = { "2025-12-31" };
        String[] row3 = { "2023-12-01" };
        assertEquals(row1[0], processor.processColumn(0, 0, row1));
        assertEquals(row2[0], processor.processColumn(1, 0, row2));
        assertEquals(row3[0], processor.processColumn(1, 0, row3));
        assertEquals("2023-12-21", processor.getReplacementValue());
    }

    @Test
    void testProcessInvalidValue() {
        var result = processor.processColumn(0, 0, new String[] { "abc "});
        assertNull(result);

        result = processor.processColumn(0, 0, new String[] { "2024-13-49"});
        assertNull(result);
    }

    @Test
    void testProcessEmptyValue() {
        String[] row = { null };
        String result = processor.processColumn(0, 0, row);
        assertNull(result);
    }
}
