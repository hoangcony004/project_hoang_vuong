package hoang_vuong.project.doan.admin.nhanvien;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import hoang_vuong.project.doan.qdl.Qdl;

@Controller
@RequestMapping("/admin")
public class NhanVienController {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private NhanVienService dvl;

    @Autowired
    private NhanVienService dvlNhanVien;

    @GetMapping("/nhan-vien")
    public String getDuyet(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "tenDayDu") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request) {

        // Kiểm tra nếu nhân viên chưa đăng nhập thì điều hướng đến trang đăng nhập
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Chuyển đổi page từ 1-based thành 0-based
        int pageIndex = page - 1;

        // Thiết lập sắp xếp
        Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(sortDirection, sort));

        // Lấy dữ liệu phân trang và sắp xếp
        Page<NhanVien> nhanVienPage = dvlNhanVien.duyetNhanVien(pageable);
        List<NhanVien> list = nhanVienPage.getContent();

        // Thêm các thuộc tính cần thiết vào model để hiển thị trong view
        model.addAttribute("ds", list);
        model.addAttribute("page", nhanVienPage);
        model.addAttribute("dl", new NhanVien());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", nhanVienPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("sortDirection", direction.equals("asc") ? "asc" : "desc");

        model.addAttribute("action", "/admin/nhan-vien/them");
        model.addAttribute("title_body", "Thêm Nhân Viên");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title", "Quản Lý Nhân Viên");
        model.addAttribute("content", "admin/nhanvien/duyet.html");

        return "layouts/layout-admin.html";
    }

    @GetMapping("/nhan-vien/them")
    public String getThem(Model model) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        var dl = new NhanVien();

        model.addAttribute("dl", dl);
        model.addAttribute("title", "Thêm Nhân Viên");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("action", "/admin/nhan-vien/them");
        model.addAttribute("content", "admin/nhanvien/them.html");

        return "layouts/layout-admin.html";
    }

    @PostMapping("/nhan-vien/them")
    public String postThem(@ModelAttribute("NhanVien") NhanVien dl,
            RedirectAttributes redirectAttributes) {

        // Mã hóa mật khẩu
        var inputPassword = dl.getMatKhau();
        var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));

        dl.setMatKhau(hash);
        dl.setXacNhanMatKhau(inputPassword);
        dl.setNgayTao(LocalDate.now());

        dvl.luuNhanVien(dl);

        redirectAttributes.addFlashAttribute("THONG_BAO", "Đã thêm mới thành công!");

        return "redirect:/admin/nhan-vien";
    }

    @GetMapping("/nhan-vien/sua")
    public String getSuaAjax(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        var dl = dvl.xemNhanVien(id);
        model.addAttribute("title_body", "Sửa Nhân Viên");
        model.addAttribute("title_sm", "Cập nhật");
        model.addAttribute("dl", dl);
        model.addAttribute("action", "/admin/nhan-vien/sua");

        return "admin/nhanvien/form-bs4.html";

    }

    @PostMapping("/nhan-vien/sua")
    public String postSua(@ModelAttribute("NhanVien") NhanVien dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Kiểm tra nếu mật khẩu mới không được nhập
        if (dl.getMatKhau() == null || dl.getMatKhau().isEmpty()) {

            var inputXNMK = dl.getXacNhanMatKhau();
            var hash = BCrypt.hashpw(inputXNMK, BCrypt.gensalt(12));

            dl.setMatKhau(hash);
            dl.setXacNhanMatKhau(inputXNMK);
        } else {
            ;
            var inputPassword = dl.getMatKhau();
            var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));

            dl.setMatKhau(hash);
            dl.setXacNhanMatKhau(inputPassword);
        }

        dl.setNgaySua(LocalDate.now());
        dvl.luuNhanVien(dl);

        redirectAttributes.addFlashAttribute("THONG_BAO", "Đã sửa thành công !");

        return "redirect:/admin/nhan-vien";
    }

    @PostMapping("/nhan-vien/xoa")
    public String postXoa(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        System.out.println("ID nhận được trong controller là: " + id);

        try {
            this.dvl.xoaNhanVien(id);
            redirectAttributes.addFlashAttribute("THONG_BAO", "Đã xóa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa nhân viên. Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nhan-vien";
    }

    @GetMapping("/nhan-vien/xem")
    public String getXemAjax(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        var dl = dvl.xemNhanVien(id);

        model.addAttribute("title_body", "Xem Nhân Viên");
        model.addAttribute("dl", dl);
        model.addAttribute("action", "/admin/nhan-vien/xem");

        return "admin/nhanvien/form-xem-bs4.html";

    }

    @GetMapping("/nhan-vien/tim-kiem")
    public String getTimKiem(Model model,
            @RequestParam("criteria") String criteria,
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "tenDayDu") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu nhân viên chưa đăng nhập thì điều hướng đến trang đăng nhập
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Kiểm tra nếu không có từ khóa tìm kiếm
        if (query == null || query.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ER",
                    "Chưa nhập từ khóa tìm kiếm.");
            return "redirect:/admin/nhan-vien";
        }

        // Thiết lập sắp xếp
        Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortDirection, sort));

        List<NhanVien> list = new ArrayList<>();
        int total = 0;

        switch (criteria) {
            case "Id":
                try {
                    int id = Integer.parseInt(query);
                    NhanVien nv = dvl.timNhanVienTheoId(id);
                    if (nv != null) {
                        list.add(nv);
                        total = 1; // Chỉ có 1 kết quả
                    }
                    model.addAttribute("THONG_BAO", "Tìm kiếm thành công.");
                } catch (NumberFormatException e) {
                    model.addAttribute("THONG_BAO_ER", "ID phải là số nguyên.");
                }
                break;
            case "tenDayDu":
                list = dvl.timKiemTheoTen(query);
                total = list.size();
                model.addAttribute("THONG_BAO", "Tìm kiếm thành công.");
                break;
            case "email":
                list = dvl.timKiemTheoEmail(query);
                total = list.size();
                model.addAttribute("THONG_BAO", "Tìm kiếm thành công.");
                break;
            case "dienThoai":
                list = dvl.timKiemTheoDienThoai(query);
                total = list.size();
                model.addAttribute("THONG_BAO", "Tìm kiếm thành công.");
                break;
            default:
                model.addAttribute("THONG_BAO_ER", "Tiêu chí tìm kiếm không hợp lệ.");
                break;
        }

        if (total == 0) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ER",
                    "Không có kết quả nào trùng với từ khóa tìm kiếm hoặc tiêu chí không hợp lệ.");
            return "redirect:/admin/nhan-vien";
        }

        // Phân trang danh sách kết quả
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        Page<NhanVien> nhanVienPage = new PageImpl<>(list.subList(start, end), pageable, total);

        model.addAttribute("ds", nhanVienPage.getContent());
        model.addAttribute("page", nhanVienPage);
        model.addAttribute("dl", new NhanVien());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", nhanVienPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("sortDirection", direction.equals("asc") ? "asc" : "desc");

        model.addAttribute("action", "/admin/nhan-vien/them");
        model.addAttribute("title_body", "Thêm Nhân Viên");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title", "Quản Lý Nhân Viên");
        model.addAttribute("content", "admin/nhanvien/duyet.html");

        return "layouts/layout-admin.html";
    }

    @GetMapping("/dang-nhap")
    public String getDangNhap(Model model, HttpSession session) {

        System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));
        model.addAttribute("dl", new NhanVien());
        model.addAttribute("content", "admin/nhanvien/dangnhap.html");

        return "layouts/layout-admin-login.html";
    }

    @PostMapping("/dang-nhap")
    public String postDangNhap(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("TenDangNhap") String TenDangNhap,
            @RequestParam("MatKhau") String MatKhau,
            HttpServletRequest request,
            HttpSession session) {

        String old_password = null;

        if (dvl.DaCoTenDangNhap(TenDangNhap)) {
            var old_dl = dvl.timNhanVienTheoTenDangNhap(TenDangNhap);
            old_password = old_dl.getMatKhau();
            boolean dung_mat_khau = BCrypt.checkpw(MatKhau, old_password);

            if (dung_mat_khau) {
                request.getSession().setAttribute("NhanVien_Id", old_dl.getId());
                request.getSession().setAttribute("NhanVien_TenDayDu", old_dl.getTenDayDu());

                var uriBeforeLogin = (String) session.getAttribute("URI_BEFORE_LOGIN");
                if (uriBeforeLogin == null)
                    uriBeforeLogin = "/admin/dashboard";
                return "redirect:" + uriBeforeLogin;
            } else {
                // Sai mật khẩu, giữ lại tên đăng nhập
                // session.setAttribute("TenDangNhap", TenDangNhap);
                redirectAttributes.addFlashAttribute("THONG_BAO", "Sai tên đăng nhập hoặc mật khẩu!");
                return "redirect:/admin/dang-nhap";
            }

        } else {
            // Không tồn tại tên đăng nhập
            redirectAttributes.addFlashAttribute("THONG_BAO", "Sai tên đăng nhập hoặc mật khẩu!");
            return "redirect:/admin/dang-nhap";
        }
    }

    @GetMapping("/dang-xuat")
    public String getDangThoat(Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("THONG_BAO", "Đăng xuất thành công.");
        request.getSession().invalidate();
        return "redirect:/admin/dang-nhap";
    }

}
