package com.shoplite.controller;

import com.shoplite.dto.BillRequest;
import com.shoplite.model.Bill;
import com.shoplite.model.Product;
import com.shoplite.service.BillService;
import com.shoplite.service.ProductService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BillController {
    private final BillService service;
    private final ProductService products;

    public BillController(BillService service, ProductService products) {
        this.service = service;
        this.products = products;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        List<Product> allProducts = products.findAll();
        List<Product> lowStock = products.lowStock();
        long categoryCount = allProducts.stream().map(product -> product.getCategory()).distinct().count();
        Map<String, Object> sales = service.dailySales(LocalDate.now());
        return Map.of(
            "totalProducts", allProducts.size(),
            "totalCategories", categoryCount,
            "lowStockCount", lowStock.size(),
            "lowStockProducts", lowStock,
            "todaySales", sales.get("totalSales"),
            "todayBills", sales.get("billCount")
        );
    }

    @PostMapping("/bills")
    @ResponseStatus(HttpStatus.CREATED)
    public Bill create(@Valid @RequestBody BillRequest req) { return service.create(req); }

    @PutMapping("/bills/{id}")
    public Bill updateDraft(@PathVariable Long id, @Valid @RequestBody BillRequest req) {
        return service.updateDraft(id, req);
    }

    @DeleteMapping("/bills/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDraft(@PathVariable Long id) { service.deleteDraft(id); }

    @PostMapping("/bills/{id}/finalize")
    public Bill finalizeBill(@PathVariable Long id) { return service.finalizeBill(id); }

    @GetMapping("/bills")
    public List<Bill> all() { return service.findAll(); }

    @GetMapping("/bills/{id}")
    public Bill one(@PathVariable Long id) { return service.get(id); }

    /** GET /api/sales/daily?date=2026-09-28  (defaults to today) */
    @GetMapping("/sales/daily")
    public Map<String, Object> daily(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.dailySales(date != null ? date : LocalDate.now());
    }
}
