package com.shoplite.service;

import com.shoplite.dto.BillRequest;
import com.shoplite.exception.BusinessRuleException;
import com.shoplite.exception.ResourceNotFoundException;
import com.shoplite.model.*;
import com.shoplite.repository.BillRepository;
import com.shoplite.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillService {
    private final BillRepository bills;
    private final ProductRepository products;

    public BillService(BillRepository bills, ProductRepository products) {
        this.bills = bills;
        this.products = products;
    }

    /** Creates a DRAFT bill. Validates stock but does NOT deduct it. */
    @Transactional
    public Bill create(BillRequest req) {
        Bill bill = new Bill();
        replaceItems(bill, req);
        return bills.save(bill);
    }

    @Transactional
    public Bill updateDraft(Long id, BillRequest req) {
        Bill bill = get(id);
        requireDraft(bill);
        replaceItems(bill, req);
        return bills.save(bill);
    }

    @Transactional
    public void deleteDraft(Long id) {
        Bill bill = get(id);
        requireDraft(bill);
        bills.delete(bill);
    }

    private void requireDraft(Bill bill) {
        if (bill.getStatus() != BillStatus.DRAFT) {
            throw new BusinessRuleException("Finalized bills cannot be changed or deleted");
        }
    }

    private void replaceItems(Bill bill, BillRequest req) {
        bill.getItems().clear();
        Map<Long, Integer> requested = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;
        for (BillRequest.Line line : req.items()) {
            long productId = Objects.requireNonNull(line.productId(), "Product ID must not be null");
            Product p = products.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + productId + " not found"));
                int quantity = line.quantity();
                int totalForProduct = requested.merge(p.getId(), quantity,
                    (existing, added) -> existing.intValue() + added.intValue());
            if (totalForProduct > p.getStockQuantity()) {
                throw new BusinessRuleException("Insufficient stock for '" + p.getName() + "': requested "
                        + totalForProduct + ", available " + p.getStockQuantity());
            }
            BillItem item = new BillItem();
            item.setBill(bill);
            item.setProduct(p);
            item.setQuantity(line.quantity());
            item.setUnitPrice(p.getPrice());
            item.setLineTotal(p.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
            bill.getItems().add(item);
            total = total.add(item.getLineTotal());
        }
        bill.setTotalAmount(total);
    }

    /** Finalizes a DRAFT bill: re-checks stock, then deducts it. All-or-nothing. */
    @Transactional
    public Bill finalizeBill(Long id) {
        Bill bill = get(id);
        if (bill.getStatus() == BillStatus.FINALIZED) {
            throw new BusinessRuleException("Bill " + id + " is already finalized");
        }
        Map<Long, Integer> needed = new TreeMap<>();
        for (BillItem item : bill.getItems()) {
                needed.merge(item.getProduct().getId(), item.getQuantity(),
                    (existing, quantity) -> existing.intValue() + quantity.intValue());
        }
        for (Map.Entry<Long, Integer> e : needed.entrySet()) {
            Long productId = Objects.requireNonNull(e.getKey(), "Product ID must not be null");
            Product p = products.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            if (e.getValue() > p.getStockQuantity()) {
                throw new BusinessRuleException("Insufficient stock for '" + p.getName() + "': needed "
                        + e.getValue() + ", available " + p.getStockQuantity());
            }
            p.setStockQuantity(p.getStockQuantity() - e.getValue());
            products.save(p);
        }
        bill.setStatus(BillStatus.FINALIZED);
        bill.setFinalizedAt(LocalDateTime.now());
        return bills.save(bill);
    }

    @Transactional(readOnly = true)
    public Bill get(Long id) {
        long billId = Objects.requireNonNull(id, "Bill ID must not be null");
        return bills.findById(billId).orElseThrow(() -> new ResourceNotFoundException("Bill " + billId + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Bill> findAll() { return bills.findAll(); }

    public Map<String, Object> dailySales(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return Map.of("date", date.toString(),
                "totalSales", bills.totalSales(start, end),
                "billCount", bills.countFinalized(start, end));
    }
}
