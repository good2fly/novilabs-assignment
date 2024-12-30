package nlhomework.processor;

import java.util.List;

public interface ColumnProcessor<T> {

    /**
     * Called by the row processor for each parsed line, this method is responsible for handling a cell (row+column)
     * in a column data type specific manner.
     *
     * @param rowNum The row number in the output row list
     * @param colIndex The column index of the cell
     * @param values The full, parsed (but un-processed) row
     * @return The value of the cell after processing. This can be further overridden in {@link #processEnded}
     *         as a post-processing step.
     */
    String processColumn(int rowNum, int colIndex, String[] values);

    /**
     * Called by the processor when all individual rows have been processed, and it is time for post-processing.
     * @param colIndex Index of the column
     * @param rows Processed list of rows
     */
    default void processEnded(int colIndex, List<String[]> rows) {}
}
