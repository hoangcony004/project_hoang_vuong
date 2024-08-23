package hoang_vuong.project.doan.admin.nhasanxuat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import hoang_vuong.project.doan.admin.nhanvien.NhanVien;
// import ch.qos.logback.core.model.Model;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin")
public class NhaSanXuatController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private NhaSanXuatService dvl;

    @Autowired
    private NhaSanXuatService dvlNhaSanXuatService;

    @GetMapping("/nha-san-xuat")
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
        Page<NhaSanXuat> nhaSanXuatPage = dvlNhaSanXuatService.duyetNhaSanXuat(PageRequest.of(pageIndex, pageSize));

        // Cập nhật mô hình với dữ liệu phân trang
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", nhaSanXuatPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ds", nhaSanXuatPage.getContent()); // Danh sách nhà sản xuất
        model.addAttribute("dl", new NhaSanXuat()); // Đối tượng mới để thêm
        model.addAttribute("title", "Quản Lý Nhà Sản Xuất");
        model.addAttribute("title_duyet", "Nhà Sản Xuất");
        model.addAttribute("title_btn_add", "Thêm Nhà Sản Xuất");
        model.addAttribute("phan_trang", "nha-san-xuat");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("action", "/admin/nha-san-xuat/them");
        model.addAttribute("content", "admin/nhasanxuat/duyet.html");

        return "layouts/layout-admin.html";
    }

    @PostMapping("/nha-san-xuat/them")
    public String postThemNSX(@ModelAttribute("NhaSanXuat") NhaSanXuat dl,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        dvl.them(dl);

        redirectAttributes.addFlashAttribute("THONG_BAO", "Đã thêm mới thành công!");

        return "redirect:/admin/nha-san-xuat";
    }

    @PostMapping("/nha-san-xuat/xoa")
    public String postXoa(@RequestParam("id") int id,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            this.dvl.xoa(id);
            redirectAttributes.addFlashAttribute("THONG_BAO", "Đã xóa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa. Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nha-san-xuat";
    }

}
