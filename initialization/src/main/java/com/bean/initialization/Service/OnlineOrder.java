package com.bean.initialization.Service;

// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.context.annotation.Primary;
// import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

// @Primary
// @Qualifier("onlineOrder")
// @Service
@Component
@ConditionalOnProperty(name = "isOnlineOrder.enabled", havingValue = "true", matchIfMissing = false)
public class OnlineOrder implements Order {

    public OnlineOrder() {
        System.out.println("Online Order initialization done");
    }

    public void createOrder() {
        System.out.println("Created Online Order");
    }
}
