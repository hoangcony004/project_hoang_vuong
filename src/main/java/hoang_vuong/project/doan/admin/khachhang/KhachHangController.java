package hoang_vuong.project.doan.admin.khachhang;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

        // lấy số lượng sản phẩm đan có
        Long totalUsers = dvl.getTotalUsers();
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("role", "1");

        model.addAttribute("action", "/admin/khach-hang/them");
        model.addAttribute("title_body", "Thêm Khách Hàng");
        model.addAttribute("phan_trang", "khach-hang");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title", "Quản Lý Khách Hàng");
        model.addAttribute("title_duyet", "Khách Hàng");
        model.addAttribute("title_btn_add", "Thêm khách Hàng");
        model.addAttribute("content", "admin/khachhang/duyet.html");

        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        return "layouts/layout-admin.html";
    }

    @PostMapping("/khach-hang/them")
    public String postAdd(@ModelAttribute("KhachHang") KhachHang dl,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Kiểm tra xem email đã tồn tại hay chưa
        List<KhachHang> khachHangList = dvl.timKiemTheoEmail(dl.getEmail());
        if (!khachHangList.isEmpty()) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Email đã tồn tại. Vui lòng sử dụng email khác.");
            return "redirect:/admin/khach-hang";
        }

        // Kiểm tra xem tên đăng nhập đã tồn tại hay chưa
        KhachHang khachHangByTenDangNhap = dvl.timKhachHangTheoTenDangNhap(dl.getTenDangNhap());
        if (khachHangByTenDangNhap != null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Tên đăng nhập đã tồn tại. Vui lòng sử dụng tên đăng nhập khác.");
            return "redirect:/admin/khach-hang";
        }

        // Kiểm tra xem số điện thoại đã tồn tại
        List<KhachHang> khachHangDienThoaiList = dvl.timKiemTheoDienThoai(dl.getDienThoai());
        if (!khachHangDienThoaiList.isEmpty()) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Số điện thoại đã tồn tại. Vui lòng sử dụng số điện thoại khác.");
            return "redirect:/admin/khach-hang";
        }

        // Mã hóa mật khẩu
        var inputPassword = dl.getMatKhau();
        var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));

        dl.setMatKhau(hash);
        dl.setNgayTao(LocalDate.now());

        try {
            dvl.luuKhachHang(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/khach-hang";
    }

    @GetMapping("/khach-hang/sua")
    public String getEdit(Model model, @RequestParam("id") int id,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            var dl = dvl.xemKhachHang(id);
            model.addAttribute("title_body", "Sửa Khách Hàng");
            model.addAttribute("title_sm", "Cập nhật");
            model.addAttribute("dl", dl);
            model.addAttribute("action", "/admin/khach-hang/sua");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xem thông tin. Mã lỗi: " + e.getMessage());
        }

        return "admin/khachhang/form-bs4-kh.html";
    }

    @PostMapping("/khach-hang/sua")
    public String postEdit(@ModelAttribute("KhachHang") KhachHang dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        var hash = BCrypt.hashpw(dl.getMatKhau(), BCrypt.gensalt(12));

        dl.setMatKhau(hash);
        dl.setNgaySua(LocalDate.now());

        try {
            dvl.luuKhachHang(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã sửa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể sửa. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/khach-hang";
    }

    @GetMapping("/khach-hang/xem")
    public String getShow(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            var dl = dvl.xemKhachHang(id);
            model.addAttribute("title_body", "Xem Khách Hàng");
            model.addAttribute("dl", dl);
            model.addAttribute("action", "/admin/khach-hang/xem");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể xem. Mã lỗi: " + e.getMessage());
        }

        return "admin/khachhang/form-xem-kh-bs4.html";
    }

    @PostMapping("/khach-hang/xoa")
    public String postDelete(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // System.out.println("ID nhận được trong controller là: " + id);
        KhachHang khachhang = dvl.timKhachHangTheoId(id);
        if (khachhang != null && "admin".equals(khachhang.getTenDangNhap())) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa tài khoản này vì đây là tài khoản mặc định.");
            return "redirect:/admin/khach-hang";
        }
        try {

            this.dvl.xoaKhachHang(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã xóa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/khach-hang";
    }

    @GetMapping("/khach-hang/tim-kiem")
    public String getSearch(Model model,
            @RequestParam("criteria") String criteria,
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu nhân viên chưa đăng nhập thì điều hướng đến trang đăng nhập
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Kiểm tra nếu không có từ khóa tìm kiếm
        if (query == null || query.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Chưa nhập từ khóa tìm kiếm.");
            return "redirect:/admin/khach-hang";
        }

        List<KhachHang> list = new ArrayList<>();
        int total = 0;

        switch (criteria) {
            case "Id":
                try {
                    int id = Integer.parseInt(query);
                    KhachHang kh = dvl.timKhachHangTheoId(id);
                    if (kh != null) {
                        list.add(kh);
                        total = 1; // Chỉ có 1 kết quả
                    }
                    model.addAttribute("THONG_BAO_SUCCESS", "Tìm kiếm theo ID thành công.");
                } catch (NumberFormatException e) {
                    model.addAttribute("THONG_BAO_ERROR", "ID phải là số nguyên.");
                }
                break;

            case "tenDayDu":
                list = dvl.timKiemTheoTen(query);
                total = list.size();
                model.addAttribute("THONG_BAO_SUCCESS", "Tìm kiếm theo họ và tên thành công.");
                break;

            case "tenDangNhap":
                list = dvl.timKhachHangTheoTenDN(query);
                total = list.size();
                model.addAttribute("THONG_BAO_SUCCESS", "Tìm kiếm theo tên đăng nhập thành công.");
                break;

            case "email":
                list = dvl.timKiemTheoEmail(query);
                total = list.size();
                model.addAttribute("THONG_BAO_SUCCESS", "Tìm kiếm theo email thành công.");
                break;

            case "dienThoai":
                list = dvl.timKiemTheoDienThoai(query);
                total = list.size();
                model.addAttribute("THONG_BAO_SUCCESS", "Tìm kiếm theo số điện thoại thành công.");
                break;

            default:
                model.addAttribute("THONG_BAO_ERROR", "Tiêu chí tìm kiếm không hợp lệ.");
                break;
        }

        if (total == 0) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không có kết quả nào trùng với từ khóa tìm kiếm hoặc tiêu chí không hợp lệ.");
            return "redirect:/admin/khach-hang";
        }

        // Phân trang danh sách kết quả
        int start = (page - 1) * pageSize;
        int end = Math.min((start + pageSize), list.size());
        Page<KhachHang> khachHangPage = new PageImpl<>(list.subList(start, end), PageRequest.of(page - 1, pageSize),
                total);

        // thêm số thứ tự
        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        model.addAttribute("ds", khachHangPage.getContent());
        model.addAttribute("page", khachHangPage);
        model.addAttribute("dl", new KhachHang());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", khachHangPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("action", "/admin/khach-hang/them");
        model.addAttribute("title_body", "Thêm Khách Hàng");
        model.addAttribute("title_btn_add", "Thêm khách Hàng");
        model.addAttribute("title_duyet", "Khách Hàng");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title", "Quản Lý Khách Hàng");
        model.addAttribute("content", "admin/khachhang/duyet.html");

        return "layouts/layout-admin.html";
    }

}
