package com.bookingApplication.airBnb.strategy;

import com.bookingApplication.airBnb.entity.InventoryEntity;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy {
    private final PricingStrategy pricingStrategy;

    public BigDecimal calculatePrice(InventoryEntity inventory) {
        BigDecimal price = pricingStrategy.calculatePrice(inventory);

        double occupancyRate = (double) inventory.getBookCount() / inventory.getTotalCount();
        if(occupancyRate > 0.8) {
            price = price.multiply(BigDecimal.valueOf(1.2));
        }

        return price;
    }
}
