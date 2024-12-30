package nlhomework;

import nlhomework.io.FileReaderWriterFactory;
import nlhomework.processor.CsvProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CsvFileProcessorApp {

    private final static String DEFAULT_INPUT_FILE_NAME = "novi-labs-java-assignment-data.csv";
    private final static String DEFAULT_OUTPUT_FILE_NAME = "novi-labs-java-assignment-data-out.csv";

    public static void main(String[] args) throws IOException {

        String inputFileName = args.length > 0 ? args[0] : DEFAULT_INPUT_FILE_NAME;
        String outputFileName = args.length > 1 ? args[1] : DEFAULT_OUTPUT_FILE_NAME;

        var ioFactory = new FileReaderWriterFactory(inputFileName, outputFileName);
        try(var reader = ioFactory.buildReader(); var writer = ioFactory.buildWriter()) {
            var processor = new CsvProcessor(buildDefaultColumnsConfig());
            processor.process(reader, writer);
            System.out.println(outputFileName);
        }
    }

    // TODO This could be externalized so we won't be stuck with a hard-coded column config.
    public static List<ColumnDataType> buildDefaultColumnsConfig() {
        return Arrays.asList(
                ColumnDataType.ID,
                ColumnDataType.DECIMAL,
                ColumnDataType.INTEGER,
                ColumnDataType.DECIMAL,
                ColumnDataType.DATE,
                ColumnDataType.INTEGER,
                ColumnDataType.INTEGER,
                ColumnDataType.CATEGORY,
                ColumnDataType.CATEGORY
        );
    }

}
