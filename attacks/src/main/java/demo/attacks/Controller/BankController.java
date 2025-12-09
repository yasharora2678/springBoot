package demo.attacks.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class BankController {

    @PostMapping("/bank/transfer")
    public String transferAmount(@RequestParam String toAccount, @RequestParam int amount) {
        System.out.println("Transfer Done To: " + toAccount + " Amount: " + amount);
        return "Money transferred to " + toAccount + " successfully!";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }
}

