package tanoshi.testdata;

public interface ITestDataProvider<T> {

    T getTestData();
    String getName();

}
