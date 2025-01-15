package nlhomework.processor;

import nlhomework.CsvFileProcessorApp;
import nlhomework.io.StringCsvReaderAndWriterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvProcessorTest {

    private final static Logger LOG = LoggerFactory.getLogger(CsvProcessorTest.class);

    private StringBuffer output;
    private CsvProcessor csvProcessor;

    @BeforeEach
    void setUp() {
        csvProcessor = new CsvProcessor(CsvFileProcessorApp.buildDefaultColumnsConfig());
    }

    @Test
    public void processEmptyDoc() throws IOException {

        var input = "id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8\n";
        processAndVerifyOutput(input, input);
    }

    @Test
    public void processMissingValuesWithOtherRows() throws IOException {

        var input = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            row2,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            row3,0.7,3,0.0,2015-08-31,-1800,0,A,ZZ
            row1,,,,,,,,
            row0,0.5,1,-1.001,2015-08-20,-1200,0,B,ZZ
            """;
        var expected = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            row0,0.5,1,-1.001,2015-08-20,-1200,0,B,ZZ
            row1,0.9,2,0.0,2015-08-25,-1666,0,A,ZZ
            row2,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            row3,0.7,3,0.0,2015-08-31,-1800,0,A,ZZ
            """;
        processAndVerifyOutput(input, expected);
    }

    @Test
    public void outputIsOrderedByID() throws IOException {

        var input = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            row93,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            row11,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            bar,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            foo,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            """;
        var expected = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            bar,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            foo,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            row11,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            row93,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            """;
        processAndVerifyOutput(input, expected);
    }

    @Test
    public void allValuesAreMissing() throws IOException {

        var input = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            row1,,,,,,,,
            row2,,,,,,,,
            row3,,,,,,,,
            row4,,,,,,,,
            """;
        var expected = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            row1,,,,,,,,
            row2,,,,,,,,
            row3,,,,,,,,
            row4,,,,,,,,
            """;
        processAndVerifyOutput(input, expected);
    }

    @Test
    public void rowWithMissingIdIsSkipped() throws IOException {

        var input = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            ,3.1415926,10000000,9999.9999,2021-08-24,-20000,0,B,YY
            """;
        var expected = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            """;
        processAndVerifyOutput(input, expected);
    }

    @Test
    public void noValidIdsInDocument() throws IOException {

        var input = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            ,3.1415926,10000000,9999.9999,2021-08-24,-20000,0,B,YY
            ,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            ,0.7,3,0.0,2015-08-31,-1800,0,A,ZZ
            """;
        var expected = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            """;
        processAndVerifyOutput(input, expected);
    }

    @Test
    public void commentsAreSkipped() throws IOException {

        var input = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            row0,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            # Some comment before row1
            row1,0.7,3,0.0,2015-08-31,-1800,0,A,ZZ
            """;
        var expected = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            row0,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            row1,0.7,3,0.0,2015-08-31,-1800,0,A,ZZ
            """;
        processAndVerifyOutput(input, expected);
    }

    @Test
    public void rowWithMissingIdDoesNotContribute() throws IOException {

        var input = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            row0,0.5,1,-1.001,2015-08-20,-1200,0,B,ZZ
            row1,,,,,,,,
            row2,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            ,3.1415926,10000000,9999.9999,2021-08-24,-20000,0,B,YY
            ,3.1415926,10000000,9999.9999,2021-08-24,-20000,0,B,YY
            ,3.1415926,10000000,9999.9999,2021-08-24,-20000,0,B,YY
            ,3.1415926,10000000,9999.9999,2021-08-24,-20000,0,B,YY
            row4,0.7,3,0.0,2015-08-31,-1800,0,A,ZZ
            """;
        var expected = """
            id,column_1,column_2,column_3,column_4,column_5,column_6,column_7,column_8
            row0,0.5,1,-1.001,2015-08-20,-1200,0,B,ZZ
            row1,0.9,2,0.0,2015-08-25,-1666,0,A,ZZ
            row2,1.5,2,1.001,2015-08-24,-2000,0,A,YY
            row4,0.7,3,0.0,2015-08-31,-1800,0,A,ZZ
            """;
        processAndVerifyOutput(input, expected);
    }

    private void processAndVerifyOutput(String input, String expected) throws IOException {

        var f = new StringCsvReaderAndWriterFactory(input, sb -> output = sb);
        csvProcessor.process(f, f);
        assertEquals(expected, output.toString());
    }
}
