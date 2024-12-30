package nlhomework.processor;

import ch.qos.logback.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractDefaultingColumnProcessor<T> implements ColumnProcessor<T> {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * List of row-column pairs (as 2 element int array) for all cells needing post-processing.
     * Note, that technically we could just store the index of the column in the processor itself,
     * but wanted to keep the design open to the possibility of combining or sharing column processors if
     * the average values are across columns, not per column.
     */
    protected final List<int[]> defaultsNeeded = new ArrayList();

    @Override
    public String processColumn(int rowNum, int colIndex, String[] values) {
        var value = values[colIndex];
        if (StringUtil.isNullOrEmpty(value)) {
            // Remember the cells that need default value during post-processing
            defaultsNeeded.add(new int[]{rowNum, colIndex});
            return null;
        } else {
            return processNonEmptyColumn(rowNum, colIndex, values);
        }
    }

    @Override
    public void processEnded(int colIndex, List<String[]> outputRows) {
        String replacementValue = getReplacementValue();
        for (int[] rowAndCol : defaultsNeeded) {
            outputRows.get(rowAndCol[0])[rowAndCol[1]] = replacementValue;
        }
    }

    /**
     * Template method to calculate the replacement value for initially empty cells. The exact
     * nature of this calculation is left to the concrete subclasses.
     *
     * @return The replacement value represented as string (may be null)
     */
    protected abstract String getReplacementValue();

    /**
     * Template method to process a column with a (non necessarily valid) non-empty value.
     *
     * @param rowNum Row number
     * @param colIndex Column index
     * @param values Parsed, but un-processed row values
     * @return The processed value converted into a string (may be null).
     */
    abstract protected String processNonEmptyColumn(int rowNum, int colIndex, String[] values);
}
