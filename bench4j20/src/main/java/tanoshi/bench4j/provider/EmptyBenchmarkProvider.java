package tanoshi.bench4j.provider;


import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class EmptyBenchmarkProvider<T> implements IBenchmarkProvider {

    private boolean hasNext;

    public EmptyBenchmarkProvider() {
        hasNext = true;
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public String next() {
        hasNext = false;
        return "default provider";
    }
}
