package hoang_vuong.project.doan.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apps")
public class ShopController {

    @GetMapping("/shop")
    public String getShop(Model model) {

        model.addAttribute("title", "Shop");
        model.addAttribute("content", "client/shop.html");
        return "layouts/layout-client";
    }

}
