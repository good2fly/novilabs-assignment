package nlhomework.processor;

class StringColumnProcessor implements ColumnProcessor<String> {

    @Override
    public String processColumn(int rowNum, int colIndex, String[] values) {
        return values[colIndex];
    }
}
