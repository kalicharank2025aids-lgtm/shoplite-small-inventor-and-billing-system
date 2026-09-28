package com.shoplite.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record BillRequest(@NotEmpty(message = "A bill needs at least one item") List<@Valid Line> items) {
    public record Line(
            @NotNull(message = "productId is required") Long productId,
            @NotNull(message = "quantity is required") @Positive(message = "quantity must be greater than 0") Integer quantity) {}
}
