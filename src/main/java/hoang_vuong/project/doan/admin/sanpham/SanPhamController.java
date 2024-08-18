package hoang_vuong.project.doan.admin.sanpham;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class SanPhamController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SanPhamService dvl;

    @Autowired
    private SanPhamService dvlSanPhamService;

    @GetMapping("/san-pham") 
    public String getDuyet(Model model) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        List<SanPham> list = dvl.duyet();

        model.addAttribute("ds", list);
        model.addAttribute("dl", new SanPham());
        model.addAttribute("title", "Quản Lý Sản Phẩm");
        model.addAttribute("title_duyet", "Sản Phẩm");
        model.addAttribute("title_btn_add", "Thêm Sản Phẩm");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("action", "/admin/san-pham/them");
        model.addAttribute("content", "admin/sanpham/duyet.html");

        return "layouts/layout-admin.html";

    }

}
