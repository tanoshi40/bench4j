package tanoshi.bench4j.data;

import tanoshi.utils.tables.TableView;
import tanoshi.utils.tables.models.Table;

import java.util.HashMap;
import java.util.Map;

public class BenchmarkingRunResult {
    private final Map<String, ProviderResult> providerResults;

    public BenchmarkingRunResult() {
        providerResults = new HashMap<>();
    }

    public void addProviderResult(ProviderResult providerResult) {
        providerResults.put(providerResult.getProviderName(), providerResult);
    }

    public ProviderResult getProviderResult(String name) {
        return providerResults.get(name);
    }

    @Override
    public String toString() {
        return "BenchmarkingResult{" +
                "providerResults=" + providerResults +
                '}';
    }

    public Table[] toTables(BenchmarkTableBuilderSettings options) {
        Table[] tables = new Table[providerResults.size()];

        int i = 0;
        for (ProviderResult providerResult : providerResults.values()) {
            tables[i] = providerResult.toTable(options);
            i++;
        }

        return tables;
    }

    public String toView(BenchmarkTableBuilderSettings options) {
        return TableView.build(toTables(options), options.getHeaders(), null, options.getTableSettings()).toString();
    }
}
