package nlhomework.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IdColumnProcessorTest {

    private IdColumnProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new IdColumnProcessor();
    }

    @Test
    void testProcessValidDecimal() {
        String[] row1 = { "Zacc" };
        String[] row2 = { "PooD-39739872892" };
        assertEquals(row1[0], processor.processColumn(0, 0, row1));
        assertEquals(row2[0], processor.processColumn(1, 0, row2));
    }

    @Test
    void testProcessEmptyValue() {
        String[] row = { "foo", null };
        var ex = assertThrows(RequiredFieldMissingException.class,
                              () -> processor.processColumn(0, 1, row));
        assertEquals("ID column must not be empty.", ex.getMessage());
        assertEquals(1, ex.getColumnIndex());
    }
}
