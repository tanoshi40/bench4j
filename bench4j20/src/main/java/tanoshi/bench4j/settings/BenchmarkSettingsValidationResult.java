package tanoshi.bench4j.settings;

public class BenchmarkSettingsValidationResult {
    private boolean hasDataProviders;
    private boolean hasExecutors;

    public boolean isValid() {
        return hasDataProviders
                && hasExecutors;
    }

    public void setHasDataProviders(boolean hasDataProviders) {
        this.hasDataProviders = hasDataProviders;
    }

    public boolean isHasDataProviders() {
        return hasDataProviders;
    }

    public void setHasExecutors(boolean hasExecutors) {
        this.hasExecutors = hasExecutors;
    }

    public boolean isHasExecutors() {
        return hasExecutors;
    }

    @Override
    public String toString() {
        return "BenchmarkSettingsValidationResult{" +
                "hasDataProviders=" + hasDataProviders +
                ", hasExecutors=" + hasExecutors +
                '}';
    }
}
