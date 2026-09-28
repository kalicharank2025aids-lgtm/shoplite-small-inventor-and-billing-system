package com.shoplite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "Product category is required")
    private String category;
    @NotNull(message = "Price is required") @Positive(message = "Price must be greater than 0")
    private BigDecimal price;
    @NotNull(message = "Stock quantity is required") @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
    @NotNull(message = "Reorder threshold is required") @PositiveOrZero(message = "Reorder threshold cannot be negative")
    private Integer reorderThreshold;
    @NotBlank(message = "Product unit is required")
    private String unit;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer s) { this.stockQuantity = s; }
    public Integer getReorderThreshold() { return reorderThreshold; }
    public void setReorderThreshold(Integer t) { this.reorderThreshold = t; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
