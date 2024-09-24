package hoang_vuong.project.doan.admin.anhsanpham;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AnhSanPhamController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AnhSanPhamService dvl;

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/anh-san-pham")
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
        Page<AnhSanPham> anhSanPhamPage = dvl.duyetAnhSanPham(PageRequest.of(pageIndex, pageSize));

        List<SanPham> dsSanPham = sanPhamService.dsSanPham();

        model.addAttribute("dsSanPham", dsSanPham);
        // Cập nhật mô hình với dữ liệu phân trang
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", anhSanPhamPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ds", anhSanPhamPage.getContent());
        model.addAttribute("dl", new AnhSanPham());
        model.addAttribute("title", "Quản Lý Ảnh Sản Phẩm");
        model.addAttribute("title_duyet", "Ảnh Sản Phẩm");
        model.addAttribute("title_btn_add", "Thêm Ảnh Sản Phẩm");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("action", "/admin/anh-san-pham/them");
        model.addAttribute("content", "admin/anhsanpham/duyet.html");

        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        return "layouts/layout-admin.html";
    }

    @PostMapping("/anh-san-pham/them")
    public String postAdd(@ModelAttribute("AnhSanPham") AnhSanPham dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        dl.setNgayTao(LocalDate.now());
        try {
            dvl.them(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/anh-san-pham";
    }

    @GetMapping("/anh-san-pham/sua")
    public String getEdit(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            var dl = dvl.xem(id);
            List<SanPham> dsSanPham = sanPhamService.dsSanPham();
            model.addAttribute("dsSanPham", dsSanPham);
            model.addAttribute("title_btn_add", "Sửa Ảnh Sản Phẩm");
            model.addAttribute("title_sm", "Cập nhật");
            model.addAttribute("dl", dl);
            model.addAttribute("action", "/admin/anh-san-pham/sua");
        } catch (Exception e) {
            model.addAttribute("THONG_BAO_ERROR", "Không thể xem. Mã lỗi: " + e.getMessage());
        }
        return "admin/anhsanpham/form-bs4-asp.html";

    }

    @PostMapping("/anh-san-pham/sua")
    public String postEdit(@ModelAttribute("AnhSanPham") AnhSanPham dl,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        dl.setNgaySua(LocalDate.now());
        try {
            dvl.sua(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã sửa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể sửa. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/anh-san-pham";
    }

    @PostMapping("/anh-san-pham/xoa")
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

        return "redirect:/admin/anh-san-pham";
    }

}
