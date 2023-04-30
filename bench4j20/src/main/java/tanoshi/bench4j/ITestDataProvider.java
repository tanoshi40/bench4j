package tanoshi.bench4j;

public interface ITestDataProvider<T> {

    T getTestData();
    String getName();

}
