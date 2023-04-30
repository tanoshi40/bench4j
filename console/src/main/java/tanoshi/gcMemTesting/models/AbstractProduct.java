package tanoshi.gcMemTesting.models;

public class AbstractProduct {
    private static int idCounter = 0;

    private final int price = (int) Math.round(Math.random());
    private final String id;

    public AbstractProduct() {
        id = "product-" + idCounter++;
    }

    public int getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }
}
