package hoang_vuong.project.doan.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class contactController {
    @GetMapping("/apps/contact")
    public String getcontact(Model model){
        model.addAttribute("content", "client/contact.html");
     return "layouts/layout-client";
    }
    
}
