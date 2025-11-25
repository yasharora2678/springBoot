package com.bean.initialization.Controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.initialization.Service.OfflineOrder;
import com.bean.initialization.Service.OnlineOrder;
// import com.bean.initialization.Service.Order;

@RestController
@RequestMapping(value = "/api")
public class UserController {
    // @Qualifier("offlineOrder")
    // @Autowired
    // Order offlineOrder;

    // @Qualifier("onlineOrder")
    // @Autowired
    // Order onlineOrder;

    // @Autowired
    // Order order;

    @Autowired(required = false)
    OnlineOrder onlineOrder;

    @Autowired(required = false)
    OfflineOrder offlineOrder;

    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(
        // @Value("${isOnlineOrder}") boolean isOnlineOrder
        ) {
        // if (isOnlineOrder) {
        // onlineOrder.createOrder();
        // return ResponseEntity.status(HttpStatus.OK).body("Order Created");
        // } else {
        // offlineOrder.createOrder();
        // return ResponseEntity.status(HttpStatus.OK).body("Order Created");
        // }
        // order.createOrder();
        System.out.println("Online order intialization value is null - " + Objects.isNull(onlineOrder));
        System.out.println("Offline order intialization value is null - " + Objects.isNull(offlineOrder));
        return ResponseEntity.status(HttpStatus.OK).body("Order Created");
    }
}
