package com.shoplite;

import com.shoplite.model.Product;
import com.shoplite.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DemoProductInitializer implements CommandLineRunner {
    private final ProductRepository products;

    public DemoProductInitializer(ProductRepository products) {
        this.products = products;
    }

    @Override
    @Transactional
    public void run(String... args) {
        backfillLegacyProducts();
        for (Product product : catalog()) {
            if (!products.existsByNameIgnoreCase(product.getName())) {
                products.save(product);
            }
        }
    }

    private void backfillLegacyProducts() {
        for (Product product : products.findAll()) {
            boolean changed = false;
            if (product.getCategory() == null || product.getCategory().isBlank()) {
                product.setCategory("General");
                changed = true;
            }
            if (product.getUnit() == null || product.getUnit().isBlank()) {
                product.setUnit("piece");
                changed = true;
            }
            if (changed) products.save(product);
        }
    }

    private List<Product> catalog() {
        return List.of(
            product("Ponni Rice", "Rice & Grains", "70", 50, 10, "kg"),
            product("Idli Rice", "Rice & Grains", "65", 32, 8, "kg"),
            product("Basmati Rice", "Rice & Grains", "120", 18, 6, "kg"),
            product("Sona Masoori Rice", "Rice & Grains", "75", 27, 9, "kg"),
            product("Brown Rice", "Rice & Grains", "110", 14, 7, "kg"),
            product("Wheat", "Rice & Grains", "45", 45, 12, "kg"),
            product("Wheat Flour", "Rice & Grains", "55", 40, 10, "kg"),
            product("Rava", "Rice & Grains", "60", 25, 8, "kg"),
            product("Maida", "Rice & Grains", "50", 35, 10, "kg"),
            product("Poha", "Rice & Grains", "70", 22, 7, "kg"),
            product("Toor Dal", "Pulses & Dal", "160", 18, 10, "kg"),
            product("Moong Dal", "Pulses & Dal", "140", 22, 8, "kg"),
            product("Urad Dal", "Pulses & Dal", "150", 13, 7, "kg"),
            product("Chana Dal", "Pulses & Dal", "100", 30, 12, "kg"),
            product("Masoor Dal", "Pulses & Dal", "110", 9, 10, "kg"),
            product("Green Gram", "Pulses & Dal", "130", 16, 8, "kg"),
            product("Black Chana", "Pulses & Dal", "110", 12, 7, "kg"),
            product("White Chickpeas", "Pulses & Dal", "120", 19, 9, "kg"),
            product("Sunflower Oil", "Cooking Oil & Ghee", "140", 20, 10, "litre"),
            product("Groundnut Oil", "Cooking Oil & Ghee", "180", 11, 8, "litre"),
            product("Coconut Oil", "Cooking Oil & Ghee", "220", 6, 8, "litre"),
            product("Gingelly Oil", "Cooking Oil & Ghee", "200", 15, 7, "litre"),
            product("Palm Oil", "Cooking Oil & Ghee", "125", 18, 10, "litre"),
            product("Ghee", "Cooking Oil & Ghee", "650", 7, 9, "litre"),
            product("Turmeric Powder", "Spices & Masala", "35", 8, 10, "100g"),
            product("Chilli Powder", "Spices & Masala", "40", 14, 8, "100g"),
            product("Coriander Powder", "Spices & Masala", "35", 22, 9, "100g"),
            product("Garam Masala", "Spices & Masala", "55", 10, 6, "100g"),
            product("Cumin Seeds", "Spices & Masala", "45", 18, 8, "100g"),
            product("Mustard Seeds", "Spices & Masala", "25", 32, 12, "100g"),
            product("Black Pepper", "Spices & Masala", "70", 4, 7, "100g"),
            product("Cardamom", "Spices & Masala", "150", 9, 6, "50g"),
            product("Sugar", "Sugar, Salt & Sweeteners", "50", 40, 15, "kg"),
            product("Crystal Salt", "Sugar, Salt & Sweeteners", "25", 52, 20, "kg"),
            product("Rock Salt", "Sugar, Salt & Sweeteners", "40", 14, 8, "kg"),
            product("Jaggery", "Sugar, Salt & Sweeteners", "70", 19, 9, "kg"),
            product("Brown Sugar", "Sugar, Salt & Sweeteners", "90", 8, 10, "kg"),
            product("Parle-G Biscuits", "Biscuits & Snacks", "10", 90, 20, "pack"),
            product("Marie Biscuits", "Biscuits & Snacks", "30", 28, 10, "pack"),
            product("Good Day Biscuits", "Biscuits & Snacks", "30", 35, 12, "pack"),
            product("Cream Biscuits", "Biscuits & Snacks", "20", 40, 15, "pack"),
            product("Potato Chips", "Biscuits & Snacks", "20", 22, 10, "pack"),
            product("Banana Chips", "Biscuits & Snacks", "50", 12, 8, "pack"),
            product("Mixture", "Biscuits & Snacks", "50", 11, 7, "pack"),
            product("Murukku", "Biscuits & Snacks", "50", 16, 8, "pack"),
            product("Salted Peanuts", "Biscuits & Snacks", "40", 18, 9, "pack"),
            product("Coca-Cola", "Beverages", "40", 24, 12, "bottle"),
            product("Pepsi", "Beverages", "40", 8, 10, "bottle"),
            product("Frooti", "Beverages", "20", 42, 15, "bottle"),
            product("Sprite", "Beverages", "40", 20, 10, "bottle"),
            product("Tea Powder", "Beverages", "120", 15, 8, "250g"),
            product("Coffee Powder", "Beverages", "150", 11, 7, "200g"),
            product("Horlicks", "Beverages", "250", 9, 10, "500g"),
            product("Drinking Water", "Beverages", "20", 50, 20, "bottle"),
            product("Milk", "Dairy & Refrigerated", "30", 40, 20, "500ml"),
            product("Curd", "Dairy & Refrigerated", "35", 18, 10, "500g"),
            product("Butter", "Dairy & Refrigerated", "60", 12, 8, "100g"),
            product("Paneer", "Dairy & Refrigerated", "90", 7, 9, "200g"),
            product("Cheese", "Dairy & Refrigerated", "120", 13, 8, "200g"),
            product("Flavoured Milk", "Dairy & Refrigerated", "35", 9, 10, "bottle")
        );
    }

    private Product product(String name, String category, String price, int stock, int reorderThreshold, String unit) {
        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setPrice(new BigDecimal(price));
        product.setStockQuantity(stock);
        product.setReorderThreshold(reorderThreshold);
        product.setUnit(unit);
        return product;
    }
}