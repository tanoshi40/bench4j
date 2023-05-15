package tanoshi.utils.testdata;

public interface ITestDataProvider<T> {

    T getTestData();
    String getName();

}
