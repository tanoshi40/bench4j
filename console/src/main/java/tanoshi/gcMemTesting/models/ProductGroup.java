package tanoshi.gcMemTesting.models;

import java.util.ArrayList;
import java.util.List;

public class ProductGroup {

    private List<AbstractProduct> products;

    public ProductGroup() {
        products = new ArrayList<>();
    }


    public void add(AbstractProduct product) {
        products.add(product);
    }

    public List<AbstractProduct> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "{products=" + products.size() + '}';
    }
}
