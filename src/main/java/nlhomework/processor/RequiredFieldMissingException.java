package nlhomework.processor;

/**
 * Exception throws when a required column (currently only ID) value is absent.
 */
public class RequiredFieldMissingException extends RuntimeException {

    private final int columnIndex;

    public RequiredFieldMissingException(String message, int column) {
        super(message);
        this.columnIndex = column;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
