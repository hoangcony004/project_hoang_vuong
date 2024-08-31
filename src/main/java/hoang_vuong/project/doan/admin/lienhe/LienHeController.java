package hoang_vuong.project.doan.admin.lienhe;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuat;
import hoang_vuong.project.doan.qdl.Qdl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin")
public class LienHeController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LienHeService dvl;

    @GetMapping("/lien-he")
    public String getDuyetLienHe(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            HttpServletRequest request) {

        // Kiểm tra xem nhân viên đã đăng nhập chưa
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Tính toán chỉ số trang (pageIndex)
        int pageIndex = page - 1;

        // Lấy dữ liệu phân trang từ dịch vụ
        Page<LienHe> lienHePage = dvl.duyetLienHe(PageRequest.of(pageIndex, pageSize));

        List<LienHe> dsLienHe = dvl.dsLienHe();

        model.addAttribute("dsLienHe", dsLienHe);
        // Cập nhật mô hình với dữ liệu phân trang
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lienHePage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ds", lienHePage.getContent());
        model.addAttribute("dl", new LienHe());
        model.addAttribute("title", "Quản Lý Liên Hệ");
        model.addAttribute("phan_trang", "lien-he");
        model.addAttribute("title_duyet", "Liên Hệ");
        model.addAttribute("title_btn_add", "Thêm Liên Hệ");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("action", "/admin/lien-he/them");
        model.addAttribute("content", "admin/lienhe/duyet.html");

        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        return "layouts/layout-admin.html";
    }

    @PostMapping("/lien-he/them")
    public String postAddLienHe(@ModelAttribute("LienHe") LienHe dl,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";
        dl.setNgayTao(LocalDate.now());
        try {
            dvl.them(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/lien-he";
    }

    @GetMapping("/lien-he/sua")
    public String getEditLienHe(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        var dl = dvl.xem(id);
        model.addAttribute("title_body", "Sửa Liên Hệ");
        model.addAttribute("title_sm", "Cập nhật");
        model.addAttribute("dl", dl);
        model.addAttribute("action", "/admin/lien-he/sua");

        return "admin/lienhe/form-bs4-lh.html";

    }

    @PostMapping("/lien-he/sua")
    public String postMethodName(@ModelAttribute("LienHe") LienHe dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        dl.setNgaySua(LocalDate.now());
        try {
            dvl.sua(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã sửa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể sửa. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/lien-he";
    }

    @PostMapping("/lien-he/xoa")
    public String postMethodName(@RequestParam("id") int id,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            this.dvl.xoa(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã xóa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa. Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/lien-he";
    }

}
