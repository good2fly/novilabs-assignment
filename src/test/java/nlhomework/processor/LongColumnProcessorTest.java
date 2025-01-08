
        package nlhomework.processor;

        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.junit.jupiter.api.Assertions.assertNull;

        public class LongColumnProcessorTest {

            private LongColumnProcessor processor;

            @BeforeEach
            void setUp() {
                processor = new LongColumnProcessor();
            }

            @Test
            void testProcessValidDecimal() {
                String[] row1 = { "400" };
                String[] row2 = { "-400" };
                String[] row3 = { "111" };
                assertEquals(row1[0], processor.processColumn(0, 0, row1));
                assertEquals(row2[0], processor.processColumn(1, 0, row2));
                assertEquals(row3[0], processor.processColumn(2, 0, row3));
                assertEquals("37", processor.getReplacementValue());
            }

            @Test
            void testProcessInvalidDecimal() {
                String[] row = { "abc" };
                String result = processor.processColumn(0, 0, row);
                assertNull(result);
            }

            @Test
            void testProcessEmptyValue() {
                String[] row = { null };
                String result = processor.processColumn(0, 0, row);
                assertNull(result);
            }
        }
    