package hoang_vuong.project.doan.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/xin-chao")
    public String hello(Model model) {
        model.addAttribute("message", "Hello World");
        return "hello.html";
    }
}
