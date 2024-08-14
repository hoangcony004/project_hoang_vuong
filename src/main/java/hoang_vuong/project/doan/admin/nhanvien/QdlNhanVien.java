package hoang_vuong.project.doan.admin.nhanvien;

import java.time.LocalDate;
import java.util.ArrayList;

// Thư viện chuẩn: Java Standard (JavaSE)
import java.util.List;

// Thư viện web: Java Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// gọi thư viện mã hóa mật khẩu cho dự án
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// Gọi thư viện mã hóa độc lập Spring
import org.mindrot.jbcrypt.BCrypt;
// import org.slf4j.LoggerFactory;

// Thue viện Session
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import hoang_vuong.project.doan.qdl.Qdl;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Controller
public class QdlNhanVien {

    @Autowired
    private HttpServletRequest request;

    // đối tượng chịu trách nhiệm mã hóa
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    // @Autowired
    // private DvlBảngNgoại dvlBangNgoai; // cung cấp các dịch vụ thao tác dữ liệu

    @Controller
    @RequestMapping("/admin")
    public class adminController {
        @Autowired
        private DvlNhanVien dvl;

        // @Autowired
        // private KdlNhanVien DvlNhanVien;

        @Autowired
        private DvlNhanVien dvlNhanVien;

        // private static final Logger logger =
        // LoggerFactory.getLogger(QdlNhanVien.class);

        @GetMapping("/nhan-vien")
        public String getDuyet(Model model,
                @RequestParam(defaultValue = "1") int page,  // Trang bắt đầu từ 1
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
            Page<NhanVien> nhanVienPage = dvlNhanVien.duyệtNhanVien(pageable);
            List<NhanVien> list = nhanVienPage.getContent();
        
            // Thêm các thuộc tính cần thiết vào model để hiển thị trong view
            model.addAttribute("ds", list);
            model.addAttribute("page", nhanVienPage);
            model.addAttribute("dl", new NhanVien());
        
            model.addAttribute("currentPage", page);  // Trang hiện tại bắt đầu từ 1
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
            var dl = new NhanVien();

            model.addAttribute("dl", dl);
            model.addAttribute("title", "Thêm Nhân Viên");
            model.addAttribute("title_sm", "Thêm mới");
            model.addAttribute("action", "/admin/nhan-vien/them");
            model.addAttribute("content", "admin/nhanvien/them.html");

            return "layouts/layout-admin.html";
        }

        @PostMapping("/nhan-vien/them")
        public String postThem(@ModelAttribute("NhanVien") NhanVien dl, RedirectAttributes redirectAttributes) {

            // Cách làm hệ thống: Spring Security
            // mã hóa mật khẩu trước khi lưu
            // var password_encoded = passwordEncoder.encode(dl.getMatKhau());
            // dl.setMatKhau(password_encoded);
            // dl.setMatKhau(passwordEncoder.encode(dl.getMatKhau()));

            // var password = "123abc";
            // Lấy mật khẩu trên HTML Form
            var inputPassword = dl.getMatKhau();
            // Mã hóa
            var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));
            dl.setMatKhau(hash);

            dl.setNgayTao(LocalDate.now());
            dl.setNgaySua(LocalDate.now());

            // rồi mới lưu vào csdl
            dvl.lưuNhanVien(dl);

            // Gửi thông báo thành công từ view Add/Edit sang view List
            redirectAttributes.addFlashAttribute("THONG_BAO", "Đã thêm mới thành công !");

            return "redirect:/admin/nhan-vien";
        }

        // @GetMapping("/nhanvien/sua/{id}")
        @GetMapping("/nhan-vien/sua")
        public String getSua(Model model, @RequestParam("id") int id) {
            // trangSua(Model model, @PathVariable(value = "id") int id) {
            // Lấy ra bản ghi theo id
            NhanVien dl = dvl.xemNhanVien(id);

            // Gửi đối tượng dữ liệu sang bên view
            model.addAttribute("dl", dl);
            // model.addAttribute("dsBangNgoai", this.dvlBangNgoai.dsBangNgoai());

            // Hiển thị giao diện view html
            // Nội dung riêng của trang...
            model.addAttribute("content", "admin/nhanvien/sua.html"); // sua.html

            return "layouts/layout-admin.html";
        }

        @GetMapping("/nhan-vien/sua-ajax")
        public String getSuaAjax(Model model, @RequestParam("id") int id) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/admin/dang-nhap";

            // NhanVien dl = dvl.xemNhanVien(id);
            var dl = dvl.xemNhanVien(id);
            model.addAttribute("title_body", "Sửa Nhân Viên");
            model.addAttribute("title_sm", "Cập nhật");
            // Gửi đối tượng dữ liệu sang bên view
            model.addAttribute("dl", dl);
            model.addAttribute("action", "/admin/nhan-vien/sua");

            return "admin/nhanvien/form-bs4.html";

        }

        @PostMapping("/nhan-vien/sua")
        public String postSua(@ModelAttribute("NhanVien") NhanVien dl, RedirectAttributes redirectAttributes) {

            dl.setMatKhau(BCrypt.hashpw(dl.getMatKhau(), BCrypt.gensalt(12)));

            dvl.lưuNhanVien(dl);

            // Gửi thông báo thành công từ view Add/Edit sang view List
            redirectAttributes.addFlashAttribute("THONG_BAO", "Đã sửa thành công !");

            return "redirect:/admin/nhan-vien";
        }

        @GetMapping("/nhan-vien/xoa")
        public String getXoa(Model model, @RequestParam(value = "id") int id) {
            // Lấy ra bản ghi theo id
            NhanVien dl = dvl.tìmNhanVienTheoId(id);

            // Gửi đối tượng dữ liệu sang bên view
            model.addAttribute("dl", dl);

            // Hiển thị view giao diện
            // Nội dung riêng của trang...
            model.addAttribute("content", "admin/nhanvien/xoa.html"); // xoa.html

            return "layouts/layout-admin.html";
        }

        @PostMapping("/nhan-vien/xoa")
        public String postXoa(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
            System.out.println("ID nhận được trong controller: " + id);

            // Xoá dữ liệu
            try {
                this.dvl.xóaNhanVien(id);
                redirectAttributes.addFlashAttribute("THONG_BAO", "Đã xóa thành công !");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                        "Không thể xóa nhân viên. Lỗi: " + e.getMessage());
            }

            // Điều hướng sang trang giao diện
            return "redirect:/admin/nhan-vien";
        }

        @GetMapping("/nhan-vien/xem")
        public String getXem(Model model, @RequestParam(value = "id") int id) {
            // Lấy ra bản ghi theo id
            NhanVien dl = dvl.xemNhanVien(id);
            // Gửi đối tượng dữ liệu sang bên view
            model.addAttribute("dl", dl);

            // Nội dung riêng của trang...
            model.addAttribute("content", "admin/nhanvien/xem.html");

            return "layouts/layout-admin.html";
        }

        @GetMapping("/nhan-vien/xem-ajax")
        public String getXemAjax(Model model, @RequestParam("id") int id) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/admin/dang-nhap";

            // NhanVien dl = dvl.xemNhanVien(id);
            var dl = dvl.xemNhanVien(id);
            model.addAttribute("title_body", "Xem Nhân Viên");
            // Gửi đối tượng dữ liệu sang bên view
            model.addAttribute("dl", dl);
            model.addAttribute("action", "/admin/nhan-vien/xem");

            return "admin/nhanvien/form-xem-bs4.html";

        }

        @GetMapping("/nhan-vien/tim-kiem")
        public String getTimKiem(Model model, @RequestParam("criteria") String criteria,
                @RequestParam("query") String query) {
            List<NhanVien> results = new ArrayList<>();

            if (query == null || query.trim().isEmpty()) {
                model.addAttribute("THONG_BAO", "Vui lòng nhập thông tin tìm kiếm.");
                model.addAttribute("ds", results);
                model.addAttribute("dl", new NhanVien());
                model.addAttribute("action", "/admin/nhan-vien/them");
                model.addAttribute("title_body", "Thêm Nhân Viên");
                model.addAttribute("title_sm", "Thêm mới");
                model.addAttribute("title", "Quản Lý Nhân Viên");
                model.addAttribute("content", "admin/nhanvien/duyet.html");
                return "layouts/layout-admin.html";
            }

            switch (criteria) {
                case "Id":
                    try {
                        int id = Integer.parseInt(query);
                        NhanVien nv = dvl.tìmNhanVienTheoId(id);
                        if (nv != null) {
                            results.add(nv);
                        } else {
                            model.addAttribute("THONG_BAO", "Không tìm thấy nhân viên với ID này.");
                        }
                    } catch (NumberFormatException e) {
                        model.addAttribute("THONG_BAO", "ID phải là số nguyên.");
                    }
                    break;
                case "tenDayDu":
                    results = dvl.timKiemTheoTen(query);
                    break;
                case "email":
                    results = dvl.timKiemTheoEmail(query);
                    break;
                case "dienThoai":
                    results = dvl.timKiemTheoDienThoai(query);
                    break;
                default:
                    model.addAttribute("THONG_BAO", "Tiêu chí tìm kiếm không hợp lệ.");
                    break;
            }

            model.addAttribute("ds", results);
            model.addAttribute("dl", new NhanVien());
            model.addAttribute("action", "/admin/nhan-vien/them");
            model.addAttribute("title_body", "Thêm Nhân Viên");
            model.addAttribute("title_sm", "Thêm mới");
            model.addAttribute("title", "Quản Lý Nhân Viên");
            model.addAttribute("content", "admin/nhanvien/duyet.html");

            return "layouts/layout-admin.html";
        }

        // Register User Account (Đăng ký tài khoản, có mật khẩu)
        // Sign In, Sign Out, Sign Up (user)
        // Log In , Log Out, Register (customer)

        @GetMapping("/dang-nhap")
        public String getDangNhap(Model model, HttpSession session) {

            System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));
            model.addAttribute("dl", new NhanVien());
            // Lấy ra bản ghi theo id
            model.addAttribute("content", "admin/nhanvien/dangnhap.html");

            // ...được đặt vào bố cục chung của toàn website
            // return "layout.html";
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

            if (dvl.tồnTạiTênĐăngNhập(TenDangNhap)) {
                var old_dl = dvl.tìmNhanVienTheoTenDangNhap(TenDangNhap);
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
                    redirectAttributes.addFlashAttribute("TenDangNhap", TenDangNhap);
                    redirectAttributes.addFlashAttribute("THONG_BAO", "Sai mật khẩu, vui lòng nhập lại!");
                    return "redirect:/admin/dang-nhap";
                }
            } else {
                // Không tồn tại tên đăng nhập
                redirectAttributes.addFlashAttribute("THONG_BAO", "Sai tên đăng nhập hoặc mật khẩu!");
                return "redirect:/admin/dang-nhap";
            }
        }

        @GetMapping("/dang-xuat")
        public String getDangThoat(HttpServletRequest request) {

            request.getSession().invalidate();
            return "redirect:/admin/dang-nhap";
        }

        @GetMapping("/dashboard")
        public String getDashboard(Model model) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/admin/dang-nhap";

            model.addAttribute("title", "Dashboard");
            model.addAttribute("content", "admin/dashboard/dashboard.html");

            // ...được đặt vào bố cục chung của toàn website
            // return "layout.html";
            return "layouts/layout-admin.html";
        }

    }

}
// end class
