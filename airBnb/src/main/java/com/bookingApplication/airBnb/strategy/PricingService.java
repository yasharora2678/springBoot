package com.bookingApplication.airBnb.strategy;

import com.bookingApplication.airBnb.entity.InventoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingService {
    public BigDecimal calculateDynamicPricing(InventoryEntity inventoryEntity) {
        return BigDecimal.ZERO;
    }

    //    Return the sum of price of this inventory list
    public BigDecimal calculateTotalPrice(List<InventoryEntity> inventoryList) {
        return inventoryList.stream()
                .map(this::calculateDynamicPricing)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
