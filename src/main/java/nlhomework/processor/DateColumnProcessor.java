package nlhomework.processor;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Column processor for date columns. It uses TZ-independent {@link LocalDate} as internal representation.
 */
class DateColumnProcessor extends AbstractDefaultingColumnProcessor<LocalDate> {

    private long accumulator = 0L;
    private long rowCnt = 0L;

    @Override
    protected String processNonEmptyColumn(int rowNum, int colIndex, String[] values) {
        try {
            // no explicit formatter needed as input matches the default format for LocalDate
            var localDate = LocalDate.parse(values[colIndex]);
            accumulator += localDate.toEpochDay();
            ++rowCnt;
            return dateToString(localDate);
        } catch(DateTimeParseException ex) {
            return null;
        }
    }

    @Override
    protected String getReplacementValue() {
        LocalDate date = rowCnt > 0 ? LocalDate.ofEpochDay(accumulator / rowCnt) : null;
        return dateToString(date);
    }

    private String dateToString(LocalDate date) {
        return date != null ? date.toString() : null;
    }
}
