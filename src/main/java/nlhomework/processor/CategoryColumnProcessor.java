package nlhomework.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Column processor to process 'category' columns.
 */
class CategoryColumnProcessor extends AbstractDefaultingColumnProcessor<String> {

    // Note: to make processing parallel we have to switch to thread-safe building blocks
    // (e.g. ConcurrentHashMap, AtomicInteger, etc.)
    private final Map<String, Integer> frequencyMap = new HashMap<>();
    private int maxFrequency = 0;
    private String mostFrequentCat = null;

    @Override
    protected String processNonEmptyColumn(int rowNum, int colIndex, String[] values) {
        var value = values[colIndex];
        var freq = frequencyMap.compute(value, (k, v) -> v == null ? 1 : v + 1);
        if (freq > maxFrequency) {
            maxFrequency = freq;
            mostFrequentCat = value;
        }
        return value;
    }


    @Override
    protected String getReplacementValue() {
        return mostFrequentCat;
    }
}
