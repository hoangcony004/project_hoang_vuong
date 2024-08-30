package hoang_vuong.project.doan.admin.sanpham;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuat;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuatService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin")
public class SanPhamController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SanPhamService dvl;

    @Autowired
    private SanPhamService dvlSanPhamService;

    @Autowired
    private NhaSanXuatService nhaSanXuatService;

    @GetMapping("/san-pham")
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
        Page<SanPham> sanPhamPage = dvlSanPhamService.duyetSanPham(PageRequest.of(pageIndex, pageSize));

        // Cập nhật mô hình với dữ liệu phân trang
        List<NhaSanXuat> dsNhaSanXuat = nhaSanXuatService.dsNhaSanXuat();

        model.addAttribute("dsNhaSanXuat", dsNhaSanXuat);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhamPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ds", sanPhamPage.getContent());
        model.addAttribute("dl", new SanPham());
        model.addAttribute("title", "Quản Lý Sản Phẩm");
        model.addAttribute("title_duyet", "Sản Phẩm");
        model.addAttribute("title_body", "Thêm Sản Phẩm");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("phan_trang", "san-pham");
        model.addAttribute("action", "/admin/san-pham/them");
        model.addAttribute("content", "admin/sanpham/duyet.html");

        return "layouts/layout-admin.html";
    }

    @PostMapping("/san-pham/them")
    public String postAddSanPham(@ModelAttribute("SanPham") SanPham dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        dl.setNgayTao(LocalDate.now());
        try {
            dvl.them(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR","Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/san-pham";
    }

    @GetMapping("/san-pham/xem")
    public String getShowSP(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            var dl = dvl.xem(id);model.addAttribute("title_body", "Xem Sản Phẩm");
            model.addAttribute("dl", dl);
            model.addAttribute("action", "/admin/san-pham/xem");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR","Không thể xem. Mã lỗi: " + e.getMessage());
        }

        return "admin/sanpham/form-xem-sp-bs4.html";

    }

    @GetMapping("/san-pham/sua")
    public String getEditSP(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        List<NhaSanXuat> dsNhaSanXuat = nhaSanXuatService.dsNhaSanXuat();
        var dl = dvl.xem(id);

        model.addAttribute("dsNhaSanXuat", dsNhaSanXuat);
        model.addAttribute("title_body", "Sửa Sản Phẩm");
        model.addAttribute("title_sm", "Cập nhật");
        model.addAttribute("dl", dl);
        model.addAttribute("action", "/admin/san-pham/sua");

        return "admin/sanpham/form-bs4-sp.html";

    }

    @PostMapping("/san-pham/sua")
    public String postEditSP(@ModelAttribute("SanPham") SanPham dl,
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

        return "redirect:/admin/san-pham";
    }

    @PostMapping("/san-pham/xoa")
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

        return "redirect:/admin/san-pham";
    }

}
