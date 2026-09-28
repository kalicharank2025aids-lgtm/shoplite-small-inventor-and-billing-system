package com.shoplite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class BillItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JsonIgnore
    private Bill bill;
    @ManyToOne(optional = false)
    private Product product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    public Long getId() { return id; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer q) { this.quantity = q; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal p) { this.unitPrice = p; }
    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal l) { this.lineTotal = l; }
}
