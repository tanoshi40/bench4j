package tanoshi.gcMemTesting;

import tanoshi.gcMemTesting.models.*;

public class ProductManager {
    public void createObjects(int amount) {
        ProductGroup productGroup = new ProductGroup();
        for (int i = 0; i < amount; i++) {
            productGroup.add(createProduct());
        }
    }

    private AbstractProduct createProduct() {
        return switch ((int) Math.round(Math.random() * 10)) {
            case 0 -> new ElectronicGood();
            case 2 -> new GroceryProduct();
            case 3 -> new LuxuryGood();
            default -> new BrandedProduct();
        };

    }

}
