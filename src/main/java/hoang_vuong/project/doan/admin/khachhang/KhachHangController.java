package hoang_vuong.project.doan.admin.khachhang;

import java.time.LocalDate;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.qdl.Qdl;

@Controller
@RequestMapping("/admin")
public class KhachHangController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private KhachHangService dvlKhachHang;

    @Autowired
    private KhachHangService dvl;

    @GetMapping("/khach-hang")
    public String getDuyet(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "tenDayDu") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Chuyển đổi page từ 1-based thành 0-based
        int pageIndex = page - 1;

        // Thiết lập sắp xếp
        Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(sortDirection, sort));

        // Lấy dữ liệu phân trang và sắp xếp
        Page<KhachHang> khachHangPage = dvlKhachHang.duyetKhachHang(pageable);
        List<KhachHang> list = khachHangPage.getContent();

        // Thêm các thuộc tính cần thiết vào model để hiển thị trong view
        model.addAttribute("ds", list);
        model.addAttribute("page", khachHangPage);
        model.addAttribute("dl", new KhachHang());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", khachHangPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("sortDirection", direction.equals("asc") ? "asc" : "desc");

        model.addAttribute("action", "/admin/khach-hang/them");
        model.addAttribute("title_body", "Thêm Khách Hàng");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title", "Quản Lý Khách Hàng");
        model.addAttribute("title_duyet", "Khách Hàng");
        model.addAttribute("title_btn_add", "Thêm khách Hàng");
        model.addAttribute("content", "admin/khachhang/duyet.html");

        return "layouts/layout-admin.html";
    }


    @PostMapping("/khach-hang/them")
    public String postThem(@ModelAttribute("KhachHang") KhachHang dl,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Mã hóa mật khẩu
        var inputPassword = dl.getMatKhau();
        var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));

        dl.setMatKhau(hash);
        dl.setNgayTao(LocalDate.now());

        dvl.luuKhachHang(dl);

        redirectAttributes.addFlashAttribute("THONG_BAO", "Đã thêm mới thành công!");

        return "redirect:/admin/khach-hang";
    }

    @GetMapping("/khach-hang/sua")
    public String getSuaAjax(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        var dl = dvl.xemKhachHang(id);
        model.addAttribute("title_body", "Sửa Khách Hàng");
        model.addAttribute("title_sm", "Cập nhật");
        // Gửi đối tượng dữ liệu sang bên view
        model.addAttribute("dl", dl);
        model.addAttribute("action", "/admin/khach-hang/sua");

        return "admin/khachhang/form-bs4-kh.html";

    }

    @PostMapping("/khach-hang/sua")
    public String postSua(@ModelAttribute("KhachHang") KhachHang dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        var hash = BCrypt.hashpw(dl.getMatKhau(), BCrypt.gensalt(12));

        dl.setMatKhau(hash);

        dvl.luuKhachHang(dl);

        // Gửi thông báo thành công từ view Add/Edit sang view List
        redirectAttributes.addFlashAttribute("THONG_BAO", "Đã sửa thành công !");

        return "redirect:/admin/khach-hang";
    }
}

// }
