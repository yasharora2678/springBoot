package com.bookingApplication.airBnb.service;

import com.bookingApplication.airBnb.entity.HotelEntity;
import com.bookingApplication.airBnb.entity.HotelMinPriceEntity;
import com.bookingApplication.airBnb.entity.InventoryEntity;
import com.bookingApplication.airBnb.repository.HotelMinPriceRepository;
import com.bookingApplication.airBnb.repository.HotelRepository;
import com.bookingApplication.airBnb.repository.InventoryRepository;
import com.bookingApplication.airBnb.strategy.PricingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateService {
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService  pricingService;

    @Scheduled(cron = "0 0 * * * *")
    public void updatePrices() {
        System.out.println("Scheduler running: " + LocalDateTime.now());
        int page = 0;
        int batchSize = 100;

        while(true) {
            Page<HotelEntity> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            System.out.println("It is working fine" + hotelPage);
            if(hotelPage.isEmpty()) {
                break;
            }
            hotelPage.getContent().forEach(this::updateHotelPrices);

            page++;
        }
    }

    private void updateHotelPrices(HotelEntity hotel) {
        log.info("Updating hotel prices for hotel ID: {}", hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<InventoryEntity> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);
        System.out.println(inventoryList + "Before updation");
        updateInventoryPrices(inventoryList);
        System.out.println(inventoryList + "After updation");
        updateHotelMinPrice(hotel, inventoryList, startDate, endDate);
    }

    private void updateHotelMinPrice(HotelEntity hotel, List<InventoryEntity> inventoryList, LocalDate startDate, LocalDate endDate) {
        // Compute minimum price per day for the hotel
        Map<LocalDate, BigDecimal> dailyMinPrices = new HashMap<>();

        inventoryList.forEach(inventory -> {
            LocalDate date = inventory.getDate();
            BigDecimal price = inventory.getPrice();
            if (!dailyMinPrices.containsKey(date)) {
                dailyMinPrices.put(date, price);
            } else {
                BigDecimal currentMin = dailyMinPrices.get(date);
                if (price.compareTo(currentMin) < 0) {
                    dailyMinPrices.put(date, price);
                }
            }
        });
        System.out.println(dailyMinPrices + "Daily MIN PRICES");
        // Prepare HotelPrice entities in bulk
        List<HotelMinPriceEntity> hotelPrices = new ArrayList<>();
        dailyMinPrices.forEach((date, price) -> {
            HotelMinPriceEntity hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date)
                    .orElse(new HotelMinPriceEntity(hotel, date));
            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        });

        // Save all HotelPrice entities in bulk
        hotelMinPriceRepository.saveAll(hotelPrices);
    }

    private void updateInventoryPrices(List<InventoryEntity> inventoryList) {
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }

}
