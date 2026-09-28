package com.shoplite.service;

import com.shoplite.exception.ResourceNotFoundException;
import com.shoplite.model.Product;
import com.shoplite.repository.BillRepository;
import com.shoplite.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository repo;
    private final BillRepository bills;

    public ProductService(ProductRepository repo, BillRepository bills) {
        this.repo = repo;
        this.bills = bills;
    }

    public List<Product> findAll() { return repo.findAll(); }

    public Product get(Long id) {
        return repo.findById(java.util.Objects.requireNonNull(id, "id"))
            .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));
    }

    public List<Product> lowStock() { return repo.findBelowReorderThreshold(); }

    public Product create(Product p) {
        p.setId(null);
        return repo.save(p);
    }

    public Product update(Long id, Product in) {
        Product p = get(id);
        p.setName(in.getName());
        p.setCategory(in.getCategory());
        p.setPrice(in.getPrice());
        p.setStockQuantity(in.getStockQuantity());
        p.setReorderThreshold(in.getReorderThreshold());
        p.setUnit(in.getUnit());
        return repo.save(p);
    }

    @Transactional
    public void delete(Long id) {
        Product product = get(id);
        if (bills.existsByItems_Product_Id(product.getId())) {
            throw new com.shoplite.exception.BusinessRuleException(
                "Product '" + product.getName() + "' is used by a bill and cannot be deleted");
        }
        repo.delete(product);
    }
}
