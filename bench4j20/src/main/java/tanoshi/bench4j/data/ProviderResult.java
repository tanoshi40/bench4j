package tanoshi.bench4j.data;

import tanoshi.utils.tables.TableView;
import tanoshi.utils.tables.models.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderResult {

    private final String providerName;
    private final Map<String, BatchResultSet> executorResults;
    private int targetBatchSize;

    public ProviderResult(String providerName) {
        this.providerName = providerName;
        executorResults = new HashMap<>();
    }

    public String getProviderName() {
        return providerName;
    }

    public void addExecutorRes(ExecutorResult execRes) {
        executorResults.put(execRes.getName(), execRes);
    }

    public BatchResultSet getExecutorResults(String name) {
        return executorResults.get(name);
    }

    public void setTargetBatchSize(int targetBatchSize) {
        this.targetBatchSize = targetBatchSize;
    }

    public int getTargetBatchSize() {
        return targetBatchSize;
    }

    public Table toTable(BenchmarkTableBuilderSettings options) {
        List<BatchResultSet> execResults = options.getOrderedExecutionResults(executorResults.values());

        String[] headers = options.getHeaders();
        String[][] content = options.getTableContent(execResults);

        return new Table(headers, content, providerName);
    }

    public String toView(BenchmarkTableBuilderSettings options) {
        return TableView.build(toTable(options), options.getTableSettings()).toView();
    }
}
