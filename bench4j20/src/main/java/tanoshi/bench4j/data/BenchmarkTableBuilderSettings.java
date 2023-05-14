package tanoshi.bench4j.data;

import tanoshi.utils.tables.settings.TableSettings;
import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.units.time.converter.TimeConverter;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BenchmarkTableBuilderSettings {
    private final int size;
    private final String[] headers;
    private TableSettings tableSettings;

    public BenchmarkTableBuilderSettings() {
        headers = new String[]{"name", "avr", "max", "min"};
        size = headers.length;
        tableSettings = new TableSettings();
    }

    public int size() {
        return size;
    }

    public String[] getHeaders() {
        return headers;
    }

    public List<BatchResultSet> getOrderedExecutionResults(Collection<BatchResultSet> values) {
        return values.stream().sorted(
                Comparator.comparing(BatchResultSet::getAvr)
        ).collect(Collectors.toList());
    }

    public String[][] getTableContent(List<BatchResultSet> execResults) {
        String[][] table = new String[execResults.size()][];

        for (int i = 0; i < execResults.size(); i++) {
            BatchResultSet execResult = execResults.get(i);
            table[i] = getAsRow(execResult);
        }

        return table;
    }

    private String[] getAsRow(BatchResultSet execResult) {
        String[] values = new String[size];
        // TODO: 17/04/2023 get ordered via options!
        values[0] = execResult.getName();
        values[1] = TimeConverter.humanizedStr(execResult.getAvr(), TimeUnits.NANOSECONDS);
        values[2] = TimeConverter.humanizedStr(execResult.getMax(), TimeUnits.NANOSECONDS);
        values[3] = TimeConverter.humanizedStr(execResult.getMin(), TimeUnits.NANOSECONDS);
        return values;
    }

    public TableSettings getTableSettings() {
        return tableSettings;
    }

    public BenchmarkTableBuilderSettings withTableSettings(TableSettings tableSettings) {
        this.tableSettings = tableSettings;
        return this;
    }
}