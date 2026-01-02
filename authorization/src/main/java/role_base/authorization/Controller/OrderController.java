package role_base.authorization.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import role_base.authorization.DTO.OrderDTO;

@RestController
@RequestMapping("/api")
public class OrderController {
    @GetMapping("/orders")
    @PreAuthorize("hasRole('USER') and hasAuthority('ORDER_READ')")
    public ResponseEntity<String> getOrders() {
        return ResponseEntity.ok("All orders have been fetched successfully");
    }

    @GetMapping("/read-orders")
    @PreAuthorize("hasRole('USER') and hasAuthority('ORDER_READ')")
    @PostAuthorize("returnObject.userId == authentication.principal.id")
    public OrderDTO readOrders() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1l);
        orderDTO.setOrderId(10000l);
        return orderDTO;
    }
}
