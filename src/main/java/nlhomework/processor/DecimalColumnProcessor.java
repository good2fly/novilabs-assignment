package nlhomework.processor;

import java.text.DecimalFormat;

/**
 * Column processor that parses decimal values, using {@link Double} as internal representation.
 * Note, that if the document is huge this may eventually overflow, and will need to be replaced w/ BigDecimal.
 */
class DecimalColumnProcessor extends AbstractDefaultingColumnProcessor<Double> {

    // Note: the accumulator/average logic could be further abstracted and moved into the base class.
    private double accumulator = 0.0;
    private long rowCnt = 0L;

    private final int precision = 8; // TODO for more flexibility this can be exposed as configurable
    private final DecimalFormat decimalFormat = createDecimalFormat(precision);

    @Override
    protected String processNonEmptyColumn(int rowNum, int colIndex, String[] values) {
        try {
            Double value = Double.valueOf(values[colIndex]);
            accumulator += value;
            ++rowCnt;
            return doubleToString(value);
        } catch(NumberFormatException ex) {
            // Ambiguity in requirements: should we also populate this with defaults or leave them null?
            return null;
        }
    }

    @Override
    protected String getReplacementValue() {
        Double avg = rowCnt > 0 ? accumulator / rowCnt : null;
        return doubleToString(avg);
    }

    private DecimalFormat createDecimalFormat(int precision) {
        return new DecimalFormat("0.0" + "#".repeat(Math.max(0, precision-1)));
    }

    private String doubleToString(Double value) {
        return value != null ? decimalFormat.format(value) : null;
    }
}
