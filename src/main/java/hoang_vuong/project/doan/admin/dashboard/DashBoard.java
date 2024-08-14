package hoang_vuong.project.doan.admin.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


public class DashBoard {
    @Controller
    @RequestMapping("/admin")
    public class adminController {

        @GetMapping("/dashboard")
        public String getDangNhap(Model model) {
            // Lấy ra bản ghi theo id
            model.addAttribute("content", "admin/dashboard/dashboard.html");

            // ...được đặt vào bố cục chung của toàn website
            // return "layout.html";
            return "layouts/layout-admin.html";
        }
    }
}
