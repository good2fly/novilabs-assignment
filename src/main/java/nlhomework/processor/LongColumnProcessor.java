package nlhomework.processor;

import java.util.List;

class LongColumnProcessor extends AbstractDefaultingColumnProcessor {

    private long accumulator = 0L;
    private long rowCnt = 0L;

    @Override
    protected String processNonEmptyColumn(int rowNum, int colIndex, String[] values) {
        try {
            Long value = Long.valueOf(values[colIndex]);
            accumulator += value;
            ++rowCnt;
            return longToString(value);
        } catch(NumberFormatException ex) {
            // Ambiguity in requirements: should we also populate this with defaults or leave them null?
            return null;
        }
    }

    @Override
    protected String getReplacementValue() {
        Long avg = rowCnt > 0 ? accumulator / rowCnt : null;
        return longToString(avg);
    }

    private String longToString(Long value) {
        return value != null ? value.toString() : null;
    }
}
