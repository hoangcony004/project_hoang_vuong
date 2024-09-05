package hoang_vuong.project.doan.admin.quangcao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class QuangCaoController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private QuangCaoService dvl;

    @GetMapping("/quang-cao")
    public String getDuyet(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            HttpServletRequest request) {

        // Kiểm tra xem nhân viên đã đăng nhập chưa
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Tính toán chỉ số trang (pageIndex)
        int pageIndex = page - 1;

        // Lấy dữ liệu phân trang từ dịch vụ
        Page<QuangCao> quangCaoPage = dvl.duyetQuangCao(PageRequest.of(pageIndex, pageSize));

        List<QuangCao> dsQuangCao = dvl.dsQuangCao();

        model.addAttribute("dsQuangCao", dsQuangCao);
        // Cập nhật mô hình với dữ liệu phân trang
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", quangCaoPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ds", quangCaoPage.getContent());
        model.addAttribute("dl", new QuangCao());
        model.addAttribute("title", "Quản Lý Quảng Cáo");
        model.addAttribute("title_duyet", "Quảng Cáo");
        model.addAttribute("title_btn_add", "Thêm Quảng Cáo");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("action", "/admin/quang-cao/them");
        model.addAttribute("content", "admin/quangcao/duyet.html");

        return "layouts/layout-admin.html";
    }

    @PostMapping("/quang-cao/them")
    public String postAdd(@ModelAttribute("QuangCao") QuangCao dl,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            dvl.them(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/quang-cao";
    }

    @PostMapping("/quang-cao/xoa")
    public String postDelete(@RequestParam("id") int id,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

            try {
                this.dvl.xoa(id);
                redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã xóa thành công !");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                        "Không thể xóa. Mã lỗi: " + e.getMessage());
            }

        return "redirect:/admin/quang-cao";
    }

}
