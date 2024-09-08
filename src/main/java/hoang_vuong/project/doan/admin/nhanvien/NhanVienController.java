package hoang_vuong.project.doan.admin.nhanvien;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.Map;
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
    private JavaMailSender mailSender;

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
        Page<NhanVien> nhanVienPage = dvl.duyetNhanVien(pageable);
        List<NhanVien> list = nhanVienPage.getContent();

        // Thêm các thuộc tính cần thiết vào model để hiển thị trong view
        model.addAttribute("ds", list);
        model.addAttribute("page", nhanVienPage);
        model.addAttribute("dl", new NhanVien());

        // thuộc tính phân trang
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", nhanVienPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);

        // thuộc tính sắp xếp
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("sortDirection", direction.equals("asc") ? "asc" : "desc");

        // thêm số thứ tự
        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        model.addAttribute("action", "/admin/nhan-vien/them");
        model.addAttribute("title_body", "Thêm Nhân Viên");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title_loc", "Lọc Nhân Viên");
        model.addAttribute("action_loc", "/admin/nhan-vien/loc");
        model.addAttribute("title", "Quản Lý Nhân Viên");
        model.addAttribute("content", "admin/nhanvien/duyet.html");

        return "layouts/layout-admin.html";
    }

    @PostMapping("/nhan-vien/them")
    public String postAdd(@ModelAttribute("NhanVien") NhanVien dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Kiểm tra trùng lặp tên đăng nhập, email, và số điện thoại
        if (dvl.timNhanVienTheoTenDangNhap(dl.getTenDangNhap()) != null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Tên đăng nhập đã tồn tại!");
            return "redirect:/admin/nhan-vien";
        }

        if (dvl.timNhanVienTheoEmail(dl.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Email đã tồn tại!");
            return "redirect:/admin/nhan-vien";
        }

        if (dvl.timNhanVienTheoDienThoai(dl.getDienThoai()) != null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Số điện thoại đã tồn tại!");
            return "redirect:/admin/nhan-vien";
        }

        // Mã hóa mật khẩu
        var inputPassword = dl.getMatKhau();
        var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));

        dl.setMatKhau(hash);
        dl.setXacNhanMatKhau(inputPassword);
        dl.setNgayTao(LocalDate.now());

        try {
            dvl.luuNhanVien(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nhan-vien";
    }

    @GetMapping("/nhan-vien/sua")
    public String getEdit(Model model, @RequestParam("id") int id) {
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
    public String postEdit(@ModelAttribute("NhanVien") NhanVien dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Kiểm tra trùng lặp tên đăng nhập, email, và số điện thoại
        if (dvl.timNhanVienTheoTenDangNhap(dl.getTenDangNhap()) != null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Tên đăng nhập đã tồn tại!");
            return "redirect:/admin/nhan-vien";
        }

        if (dvl.timNhanVienTheoEmail(dl.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Email đã tồn tại!");
            return "redirect:/admin/nhan-vien";
        }

        if (dvl.timNhanVienTheoDienThoai(dl.getDienThoai()) != null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Số điện thoại đã tồn tại!");
            return "redirect:/admin/nhan-vien";
        }

        // Kiểm tra nếu mật khẩu mới không được nhập
        if (dl.getMatKhau() == null || dl.getMatKhau().isEmpty()) {

            var inputXNMK = dl.getXacNhanMatKhau();
            var hash = BCrypt.hashpw(inputXNMK, BCrypt.gensalt(12));

            dl.setMatKhau(hash);
            dl.setXacNhanMatKhau(inputXNMK);
        } else {

            var inputPassword = dl.getMatKhau();
            var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));

            dl.setMatKhau(hash);
            dl.setXacNhanMatKhau(inputPassword);
        }

        dl.setNgaySua(LocalDate.now());
        try {
            dvl.luuNhanVien(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã sửa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể sửa. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nhan-vien";
    }

    @PostMapping("/nhan-vien/xoa")
    public String postDelete(@RequestParam("id") int id,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        // Kiểm tra nếu chưa đăng nhập, chuyển hướng tới trang đăng nhập
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Lấy ID nhân viên hiện đang đăng nhập từ session
        Integer idNhanVienDangNhap = (Integer) request.getSession().getAttribute("NhanVien_Id");

        // Kiểm tra nếu ID cần xóa trùng với ID của nhân viên đang đăng nhập
        if (idNhanVienDangNhap != null && id == idNhanVienDangNhap) {
            System.out.println("ID nhận được trong controller là: " + id);
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa! - Do tài khoản bạn xóa đang đăng nhập.");
            return "redirect:/admin/nhan-vien";
        }

        // Kiểm tra tài khoản không được phép xóa
        NhanVien nhanVien = dvl.timNhanVienTheoId(id);
        if (nhanVien != null && "admin".equals(nhanVien.getTenDangNhap())) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa tài khoản này vì đây là tài khoản mặc định.");
            return "redirect:/admin/nhan-vien";
        }

        try {
            this.dvl.xoaNhanVien(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã xóa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/nhan-vien";
    }

    @GetMapping("/nhan-vien/xem")
    public String getShow(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            var dl = dvl.xemNhanVien(id);
            model.addAttribute("title_body", "Xem Nhân Viên");
            model.addAttribute("dl", dl);
            model.addAttribute("action", "/admin/nhan-vien/xem");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể xem. Mã lỗi: " + e.getMessage());
        }

        return "admin/nhanvien/form-xem-bs4.html";
    }

    @GetMapping("/nhan-vien/tim-kiem")
    public String getSearch(Model model,
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
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
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
                list = dvl.timKiemTheoTenDangNhap(query);
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
            return "redirect:/admin/nhan-vien";
        }

        // Phân trang danh sách kết quả
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        Page<NhanVien> nhanVienPage = new PageImpl<>(list.subList(start, end), pageable, total);

        // thêm số thứ tự
        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

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
    public String getLogin(Model model, HttpSession session) {

        System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));

        model.addAttribute("title", "Đăng Nhập");
        model.addAttribute("dl", new NhanVien());
        model.addAttribute("content", "admin/nhanvien/dangnhap.html");

        return "layouts/layout-admin-login.html";
    }

    @PostMapping("/dang-nhap")
    public String postLogin(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("TenDangNhap") String TenDangNhap,
            @RequestParam("MatKhau") String MatKhau,
            HttpServletRequest request,
            HttpSession session) {

        // Tìm nhân viên theo tên đăng nhập, email hoặc số điện thoại
        NhanVien old_dl = null;
        if (TenDangNhap.contains("@")) {
            // Kiểm tra email
            old_dl = dvl.timNhanVienTheoEmail(TenDangNhap);
        } else if (TenDangNhap.matches("\\d+")) {
            // Kiểm tra số điện thoại
            old_dl = dvl.timNhanVienTheoDienThoai(TenDangNhap);
        } else {
            // Kiểm tra tên đăng nhập
            old_dl = dvl.timNhanVienTheoTenDangNhap(TenDangNhap);
        }

        if (old_dl != null) {
            // Kiểm tra mật khẩu
            String old_password = old_dl.getMatKhau();
            boolean dung_mat_khau = BCrypt.checkpw(MatKhau, old_password);

            if (dung_mat_khau) {
                request.getSession().setAttribute("NhanVien_Id", old_dl.getId());
                request.getSession().setAttribute("NhanVien_TenDayDu", old_dl.getTenDayDu());
                request.getSession().setAttribute("NhanVien_AnhDaiDien", old_dl.getAnhDaiDien());

                var uriBeforeLogin = (String) session.getAttribute("URI_BEFORE_LOGIN");
                if (uriBeforeLogin == null)
                    uriBeforeLogin = "/admin/dashboard";
                return "redirect:" + uriBeforeLogin;
            } else {
                // Sai mật khẩu, giữ lại thông tin đăng nhập
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Sai tên đăng nhập hoặc mật khẩu!");
                return "redirect:/admin/dang-nhap";
            }
        } else {
            // Không tìm thấy thông tin đăng nhập
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Sai tên đăng nhập hoặc mật khẩu!");
            return "redirect:/admin/dang-nhap";
        }
    }

    @GetMapping("/dang-xuat")
    public String getLogout(Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đăng xuất thành công.");
        request.getSession().invalidate();
        return "redirect:/admin/dang-nhap";
    }

    @GetMapping("/quen-mat-khau")
    public String getForgotPassword(Model model) {
        var dl = new NhanVien();

        model.addAttribute("dl", dl);
        model.addAttribute("title", "Quên Mật Khẩu");
        model.addAttribute("content", "admin/nhanvien/quenmatkhau.html");

        return "layouts/layout-admin-login.html";
    }

    @PostMapping("/quen-mat-khau")
    public String postForgotPassword(@RequestParam("email") String email,
            @RequestParam("g-recaptcha-response") String recaptchaResponse,
            RedirectAttributes redirectAttributes) {
        // In ra giá trị g-recaptcha-response để kiểm tra
        System.out.println("Đã nhận được phản hồi reCAPTCHA: " + recaptchaResponse);

        // Kiểm tra reCAPTCHA
        boolean isCaptchaVerified = verifyRecaptcha(recaptchaResponse);
        if (!isCaptchaVerified) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Vui lòng xác minh reCAPTCHA.");
            return "redirect:/admin/quen-mat-khau";
        } else {
            System.out.println("Recaptcha hợp lệ");
        }

        // Tạo mật khẩu mới
        String newPassword = generateRandomPassword();
        // Cập nhật mật khẩu mới trong cơ sở dữ liệu và lấy trạng thái
        int status = updatePassword(email, newPassword);

        // Kiểm tra trạng thái và thực hiện hành động tương ứng
        if (status == 0) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không tìm thấy nhân viên với email: " + email);
            return "redirect:/admin/quen-mat-khau";
        } else if (status == 2) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Có nhiều hơn một nhân viên với email: " + email);
            return "redirect:/admin/quen-mat-khau";
        } else {
            // Gửi mật khẩu mới qua email
            try {
                String subject = "Cấp Mật khẩu Mới";
                String body = "Mật khẩu mới của bạn là: " + newPassword;
                sendEmail(email, subject, body);
                redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS",
                        "Mật khẩu mới đã được gửi đến email của bạn.");
            } catch (MessagingException e) {
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                        "Có lỗi xảy ra khi gửi email. Vui lòng thử lại.");
                return "redirect:/admin/quen-mat-khau";
            }
        }

        return "redirect:/admin/dang-nhap";
    }

    // // Phương thức tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(8);

    }

    private int updatePassword(String email, String newPassword) {
        List<NhanVien> danhSachNhanVien = dvl.timDanhSachNhanVienTheoEmail(email);

        if (danhSachNhanVien.isEmpty()) {
            // Không tìm thấy nhân viên nào với email này
            return 0;
        } else if (danhSachNhanVien.size() > 1) {
            // Tìm thấy nhiều hơn một nhân viên với email này
            return 2;
        } else {
            // Chỉ có một nhân viên với email này
            NhanVien nhanVien = danhSachNhanVien.get(0);
            var hash = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
            nhanVien.setMatKhau(hash);
            nhanVien.setXacNhanMatKhau(newPassword);
            dvl.luuNhanVien(nhanVien);
            return 1;
        }
    }

    // Phương thức gửi email
    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

    private boolean verifyRecaptcha(String recaptchaResponse) {
        String secretKey = "6LdxSzkqAAAAALIXGDpmzpqa9Y4iAKY4_qIvGg7d";
        String url = "https://www.google.com/recaptcha/api/siteverify";

        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("secret", secretKey);
            requestBody.add("response", recaptchaResponse);

            // Gửi yêu cầu POST tới API của Google
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
            Map<String, Object> responseBody = response.getBody();

            // In ra toàn bộ phản hồi để kiểm tra chi tiết
            System.out.println("reCAPTCHA response: " + responseBody);

            // Kiểm tra giá trị success trong phản hồi
            boolean success = responseBody != null && Boolean.TRUE.equals(responseBody.get("success"));
            if (!success) {
                System.out.println("reCAPTCHA verification failed: " + responseBody);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/doi-mat-khau")
    public String getChangePassword(@ModelAttribute("dl") NhanVien dl, Model model, HttpServletRequest request) {
        if (Qdl.NhanVienChuaDangNhap(request)) {
            return "redirect:/admin/dang-nhap";
        }

        model.addAttribute("title", "Đổi Mật Khẩu");
        model.addAttribute("content", "admin/nhanvien/doimatkhau.html");

        return "layouts/layout-admin-login.html";
    }

    @PostMapping("/doi-mat-khau")
    public String PostChangePassword(
            @RequestParam("matKhau") String matKhau,
            @RequestParam("matKhauMoi") String matKhauMoi,
            @RequestParam("nhapLaiMatKhauMoi") String nhapLaiMatKhauMoi,
            @ModelAttribute("dl") NhanVien dl,
            HttpServletRequest request,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra xem người dùng có đăng nhập không
        if (Qdl.NhanVienChuaDangNhap(request)) {
            return "redirect:/admin/dang-nhap";
        }

        Integer nhanVienId = (Integer) session.getAttribute("NhanVien_Id");

        if (nhanVienId == null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không tìm thấy thông tin nhân viên.");
            return "redirect:/admin/doi-mat-khau";
        }

        // Lấy nhân viên từ cơ sở dữ liệu bằng ID
        NhanVien nhanVien = dvl.timNhanVienTheoId(nhanVienId);

        // Kiểm tra mật khẩu cũ
        String old_password = nhanVien.getMatKhau();
        boolean dung_mat_khau = BCrypt.checkpw(matKhau, old_password);
        if (dung_mat_khau) {
            System.out.println("Mật khẩu cũ đúng");
        } else {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Mật khẩu cũ không đúng.");
            return "redirect:/admin/doi-mat-khau";
        }

        // Kiểm tra mật khẩu mới và mật khẩu nhập lại
        if (!matKhauMoi.equals(nhapLaiMatKhauMoi)) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Mật khẩu mới và mật khẩu nhập lại không khớp.");
            return "redirect:/admin/doi-mat-khau";
        } else {
            var inputPassword = dl.getMatKhauMoi();
            var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));
            nhanVien.setMatKhau(hash);

            nhanVien.setXacNhanMatKhau(matKhauMoi);

            try {
                dvl.luuNhanVien(nhanVien);
                redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đổi mật khẩu thành công.");
                request.getSession().invalidate();
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                        "Không thể đổi mật khẩu. Mã lỗi: " + e.getMessage());
            }

            return "redirect:/admin/dang-nhap";
        }

    }

}
