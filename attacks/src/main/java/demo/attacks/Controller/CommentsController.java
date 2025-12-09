package demo.attacks.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CommentsController {

    private List<String> comments = new ArrayList<>();

    @GetMapping("/xss")
    public String getComments(Model model) {
        model.addAttribute("comments", comments);
        return "xss";
    }

    @PostMapping("/comments")
    public String addComment(@RequestParam("comment") String comment) {
        comments.add(comment);
        return "redirect:/xss";
    }
}
