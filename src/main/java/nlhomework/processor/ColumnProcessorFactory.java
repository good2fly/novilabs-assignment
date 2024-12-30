package nlhomework.processor;

import nlhomework.ColumnDataType;

public class ColumnProcessorFactory {
    public static ColumnProcessor createColumnProcessor(ColumnDataType columnDataType) {
        return switch(columnDataType) {
            case ID -> new IdColumnProcessor();
            case STRING -> new StringColumnProcessor();
            case INTEGER -> new LongColumnProcessor();
            case DECIMAL -> new DecimalColumnProcessor();
            case DATE -> new DateColumnProcessor();
            case CATEGORY -> new CategoryColumnProcessor();
        };
    }
}
