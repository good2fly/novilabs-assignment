package nlhomework;

import nlhomework.io.FileCsvReaderAndWriterFactory;
import nlhomework.processor.CsvProcessor;

import java.io.IOException;
import java.util.List;

public class CsvFileProcessorApp {

    private final static String DEFAULT_INPUT_FILE_NAME = "novi-labs-java-assignment-data.csv";
    private final static String DEFAULT_OUTPUT_FILE_NAME = "novi-labs-java-assignment-data-out.csv";

    /**
     * Run the application from the command line. Perform the CSV processing and print the output file name.
     * It takes 2 optional arguments:
     * <ul>
     *     <li>If the first argument is specified, it becomes the input file name, otherwise a default is used.</li>
     *     <li>If the second argument is specified, it becomes the output file name, otherwise a default is used.</li>
     * </ul>
     *
     * @param args Optional input and output file names. Defaults will be used if not provided
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        var inputFileName = args.length > 0 ? args[0] : DEFAULT_INPUT_FILE_NAME;
        var outputFileName = args.length > 1 ? args[1] : DEFAULT_OUTPUT_FILE_NAME;

        var ioFactory = new FileCsvReaderAndWriterFactory(inputFileName, outputFileName);
        var processor = new CsvProcessor(buildDefaultColumnsConfig());
        processor.process(ioFactory, ioFactory);
        System.out.println(outputFileName);
    }

    /**
     * Build a list of column types for the CSV to be parsed.
     *
     * TODO This could be externalized (CLI options, file, etc.) so we won't be stuck with a hard-coded column config.
     *
     * @return
     */
    public static List<ColumnDataType> buildDefaultColumnsConfig() {
        return List.of(
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
