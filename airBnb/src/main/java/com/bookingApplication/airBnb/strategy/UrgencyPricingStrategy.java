package com.bookingApplication.airBnb.strategy;

import com.bookingApplication.airBnb.entity.InventoryEntity;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy {
    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(InventoryEntity inventory) {
        BigDecimal price = pricingStrategy.calculatePrice(inventory);

        LocalDate today = LocalDate.now();
        if (!inventory.getDate().isBefore(today) && !inventory.getDate().isAfter(today.plusDays(7))) {
            price = price.multiply(BigDecimal.valueOf(1.2));
        }
        return price;
    }
}
