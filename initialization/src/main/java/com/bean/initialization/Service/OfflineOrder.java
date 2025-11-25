package com.bean.initialization.Service;

// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.stereotype.Service;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

// @Primary
// @Qualifier("offlineOrder")
// @Service
@Component
@ConditionalOnProperty(name = "isOnlineOrder.enabled", havingValue = "false", matchIfMissing = false)
public class OfflineOrder implements Order {

    public OfflineOrder() {
        System.out.println("Offline Order initialization done");
    }

    public void createOrder() {
        System.out.println("Created Offline Order");
    }
}
