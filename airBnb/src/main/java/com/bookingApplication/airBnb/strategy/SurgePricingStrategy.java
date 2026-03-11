package com.bookingApplication.airBnb.strategy;

import com.bookingApplication.airBnb.entity.InventoryEntity;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class SurgePricingStrategy implements PricingStrategy {
    private final PricingStrategy pricingStrategy;

    public BigDecimal calculatePrice(InventoryEntity inventoryEntity) {
        BigDecimal price = pricingStrategy.calculatePrice(inventoryEntity);

        return price.multiply(inventoryEntity.getSurgeFactor());
    }
}
