package nlhomework.processor;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.RowProcessorErrorHandler;
import com.univocity.parsers.common.processor.AbstractRowProcessor;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import nlhomework.ColumnDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.Writer;
import java.util.*;
import java.util.stream.IntStream;

/**
 * The main CSV processor that reads, parses and processes the input, and writes out
 * the result. Used the Univocity CSV parser under the hood for fast processing.
 */
public class CsvProcessor {

    private final static Logger LOG = LoggerFactory.getLogger(CsvProcessor.class);

    /** Type definition for each column (order must match the order of columns). */
    private final List<ColumnDataType> columnsConfig;

    /**
     * Create a new CsvProcessor with the specified column configuration.
     *
     * @param columnsConfig Ordered list of column data types
     */
    public CsvProcessor(List<ColumnDataType> columnsConfig) {
        this.columnsConfig = new ArrayList<>(columnsConfig);
    }

    /**
     * Perform the processing of the input CSV and write the processed result into the output as CSV.
     *
     * @param inputReader The input reader. Required
     * @param outputWriter The output write. Required.
     */
    public void process(Reader inputReader, Writer outputWriter) {

        Objects.requireNonNull(inputReader, "inputReader must not be null");
        Objects.requireNonNull(outputWriter, "outputWriter must not be null");

        CustomRowListProcessor rowProcessor = new CustomRowListProcessor(columnsConfig);
        CsvParser csvParser = createParser(rowProcessor);
        CsvWriter csvWriter = createWriter(outputWriter);
        csvParser.parse(inputReader);
        csvWriter.writeHeaders((csvParser.getContext().headers()));
        List<Object[]> result = rowProcessor.getOutputRowsAsObjArray();
        csvWriter.writeRowsAndClose(result);
    }

    /**
     * Create a {@link CsvParser} instance and configure it for our use.
     * @param rowProcessor
     * @return CsvParser instance
     */
    protected CsvParser createParser(RowProcessor rowProcessor) {

        // Set up the univocity parser
        var settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.setProcessor(rowProcessor);
        settings.setProcessorErrorHandler((RowProcessorErrorHandler) (error, inputRow, context) -> {
            LOG.error("Error processing row: {}", Arrays.toString(inputRow));
            LOG.error("Error details: column '{}' (index {}) has value '{}",
                      error.getColumnName(), error.getColumnIndex(), inputRow[error.getColumnIndex()]);
        });
        return new CsvParser(settings);
    }

    protected CsvWriter createWriter(Writer writer) {
        CsvWriterSettings writerSettings = new CsvWriterSettings();
        return new CsvWriter(writer, writerSettings);
    }


    /**
     * Our custom Univocity-based row processor performing the parsing of a single row, while accumulating the
     * results (all valid rows) into a list.
     */
    private class CustomRowListProcessor extends AbstractRowProcessor {

        private final List<String[]> outputRows = new ArrayList<>(1024); // TODO make starting size as optional input
        private final List<ColumnProcessor> columnProcessors;
        private final int noOfColumns;

        CustomRowListProcessor(List<ColumnDataType> columnsConfig) {
            this.columnProcessors = columnsConfig.stream()
                    .map(t -> ColumnProcessorFactory.createColumnProcessor(t))
                    .toList();
            this.noOfColumns = columnsConfig.size();
        }

        /**
         * Handle each data row already parsed by the parser.
         * We delegate the actual processing of each column value to the corresponding column processors.
         *
         * @param inputRow the data extracted by the parser for an individual record. Note that:
         * <ul>
         * <li>it will never by null. </li>
         * <li>it will never be empty unless explicitly configured on the CsvParser</li>
         * <li>it won't contain lines identified by the parser as comments.</li>
         * </ul>
         * @param context A contextual object with information and controls over the current state of the parsing process
         */
        @Override
        public void rowProcessed(String[] inputRow, ParsingContext context) {

            var newRow = new String[noOfColumns];
            boolean rowValid = true;
            for (int i = 0; i < noOfColumns; ++i) {
                try {
                    newRow[i] = columnProcessors.get(i).processColumn(outputRows.size(), i, inputRow);
                } catch(RequiredFieldMissingException ex) {
                    // We skip the whole line if the ID column value is missing.
                    // TODO due to time constraint I assumed the first columns is always the ID, otherwise this won't work
                    LOG.warn("Required field value is missing at line #{}, column: {}", context.currentLine(), i, inputRow);
                    rowValid = false;
                    break;
                } catch(Exception ex) {
                    // any other random errors should abort the whole processing(this is an arbitrary choice, since
                    // it was not specified in the requirements).
                    LOG.error("Unrecognized error while processing the document, aborting.", ex);
                    throw ex;
                }
            }
            if (rowValid) outputRows.add(newRow);
        }

        /**
         * Notify all column processors that the parsing has ended, and it's time for post-processing.
         *
         * @param context A contextual object with information and controls over the current state of the parsing process
         */
        @Override
        public void processEnded(ParsingContext context) {
            postProcessColumns();
            sortOutputRows();
        }

        private void postProcessColumns() {
            for (int i = 0; i < noOfColumns; ++i) {
                // We have a choice here to let any exception terminate the processing, or catch and continue.
                // I opted for the former.
                columnProcessors.get(i).processEnded(i, outputRows);
            }
        }

        /** Sort output rows by the (first) ID column, if present. */
        private void sortOutputRows() {
            IntStream.range(0, columnsConfig.size())
                    .filter(i -> columnsConfig.get(i) == ColumnDataType.ID)
                    .findFirst()
                    .ifPresent(idColumn -> outputRows.sort(Comparator.comparing(row -> row[idColumn])));
        }

        /**
         * Retrieve the list of parsed and processed output rows.
         *
         * @return Output rows represented as a list of string arrays
         */
        List<String[]> getOutputRows() {
            return outputRows;
        }

        /**
         * Unfortunately univocity only supports {@code List<Object>} (and similar) argument types for its {@link CsvWriter}
         * methods, and I did not want to lose type safety in {@link #outputRows}, hence this somewhat ugly conversion
         * (due to the invariance of generic types). To preserve type safety, we return an unmodifiable list, so
         * no new elements of the wrong type (e.g. an array of Integers) can be added to the returned list.
         */
        private List<Object[]> getOutputRowsAsObjArray() {
            return Collections.unmodifiableList((List<Object[]>) (List<?>) outputRows);
        }
    }
}
