
        package nlhomework.processor;

        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.junit.jupiter.api.Assertions.assertNull;

        public class DecimalColumnProcessorTest {

            private DecimalColumnProcessor processor;

            @BeforeEach
            void setUp() {
                processor = new DecimalColumnProcessor();
            }

            @Test
            void testProcessValidDecimal() {
                String[] row1 = { "40.55" };
                String[] row2 = { "43.45" };
                assertEquals(row1[0], processor.processColumn(0, 0, row1));
                assertEquals(row2[0], processor.processColumn(1, 0, row2));
                assertEquals("42.0", processor.getReplacementValue());
            }

            @Test
            void testProcessInvalidDecimal() {
                String[] row = { "abc" };
                String result = processor.processColumn(0, 0, row);
                assertNull(result);
            }
        }
    