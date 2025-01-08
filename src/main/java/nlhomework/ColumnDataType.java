package nlhomework;

public enum ColumnDataType {
    /** Identifier column: non-empty value must be present */
    ID,
    /** String column; empty value is valid. */
    STRING,
    /** Integer column; empty values will be replaced with average value from same column. */
    INTEGER,
    /** Decimal column; empty values will be replaced with average value from same column. */
    DECIMAL,
    /** Date column; empty values will be replaced with average value from same column. */
    DATE,
    /** Category column; empty values will be replaced with most common value from same column. */
    CATEGORY
}
