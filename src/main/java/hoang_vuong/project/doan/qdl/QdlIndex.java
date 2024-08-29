package hoang_vuong.project.doan.qdl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("admin")
public class QdlIndex {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TimKiemService dvl;

    @GetMapping({ "/index" })
    public String indexAction() {
        return "Home Page-Java Spring Boot.";
    }

    @GetMapping("/hello")
    public String helloAction() {
        return "Hello Java Web Spring Boot !";
    }

    @GetMapping("/tim-kiem-in-app")
    public String getTimKiemApp(@RequestParam("search") String search, Model model, HttpServletRequest request) {
        if (Qdl.NhanVienChuaDangNhap(request)) {
            return "redirect:/admin/dang-nhap";
        }

        if (search != null && !search.isEmpty()) {
            List<TimKiem> results = dvl.timKiemChucNang(search);
            model.addAttribute("results", results);
            model.addAttribute("searchQuery", search);
        }

        model.addAttribute("title", "Quản Lý Tìm Kiếm");
        model.addAttribute("content", "admin/timkiem/tim-kiem-result.html");

        // Trả về trang giao diện hiển thị kết quả tìm kiếm
        return "layouts/layout-admin.html";
    }

}
