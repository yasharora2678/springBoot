package com.bookingApplication.airBnb.strategy;

import com.bookingApplication.airBnb.entity.InventoryEntity;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(InventoryEntity inventory);
}
