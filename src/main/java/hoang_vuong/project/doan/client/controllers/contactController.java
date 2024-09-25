package hoang_vuong.project.doan.client.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.admin.lienhe.LienHe;
import hoang_vuong.project.doan.admin.lienhe.LienHeService;


@Controller
public class contactController {
    @Autowired
    LienHeService lhsv;    
    @GetMapping("/apps/contact")
    public String getcontact(Model model){
        model.addAttribute("content", "client/contact.html");
     return "layouts/layout-client";
    }
    @PostMapping("/apps/contact")
    public String postcontact(RedirectAttributes redirectAttributes,
    @RequestParam("nameCT") String name,
    @RequestParam("emailCT") String email,
    @RequestParam("phoneCT") String phone,
    @RequestParam("addressCT") String address,
    @RequestParam("subjectCT") String subject,
    @RequestParam("messageCT") String message
    ){
      String  web="Molla";
      LocalDate ngaytao= LocalDate.now();
        var lh = new LienHe(null, name, email, subject, message, phone, address, web, ngaytao, null);
        boolean llh =lhsv.luulh(lh);
        if(llh){
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Bạn đã gửi liên hệ thành công.");
            return "redirect:/apps/contact";
        }
        redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Lỗi gửi liên hệ không thành công.");
        return "redirect:/apps/contact";
    }
    
}
