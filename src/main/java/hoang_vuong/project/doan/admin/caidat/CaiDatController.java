package hoang_vuong.project.doan.admin.caidat;

import java.util.List;

// Thư viện web: Java Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import hoang_vuong.project.doan.qdl.Qdl;

@Controller
@RequestMapping("/admin")
public class CaiDatController {
    @Autowired
    private CaiDatService dvl;

    @Autowired
    private HttpServletRequest request;

    @GetMapping({ "/cai-dat" })
    public String getDuyet(Model model) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        List<CaiDat> list = dvl.duyetCaiDat();

        model.addAttribute("ds", list);
        model.addAttribute("dl", new CaiDat());
        model.addAttribute("title", "Quản Lý Cài Đặt");
        model.addAttribute("title_duyet", "Cài Đặt");
        model.addAttribute("title_btn_add", "Thêm Cài Đặt");
        model.addAttribute("action", "/admin/cai-dat/them");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("content", "admin/caidat/duyet.html");
        
        return "layouts/layout-admin.html";
    }

    @PostMapping("/cai-dat/them")
    public String postThem(@ModelAttribute("CaiDat") CaiDat dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        try {
            dvl.luuCaiDat(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR","Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/cai-dat";
    }

    @PostMapping("/cai-dat/xoa")
    public String postXoa(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes)
    {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        // Xoá dữ liệu
        try {
            this.dvl.xoaCaiDat(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã xóa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa. Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/cai-dat";
    }
}