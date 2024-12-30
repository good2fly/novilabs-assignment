package nlhomework.processor;

class IdColumnProcessor implements ColumnProcessor<String> {

    @Override
    public String processColumn(int rowNum, int colIndex, String[] values) {
        var value = values[colIndex];
        if (value == null || value.isBlank()) {
            throw new RequiredFieldMissingException("ID column must not be empty.", colIndex);
        }
        return value;
    }
}
