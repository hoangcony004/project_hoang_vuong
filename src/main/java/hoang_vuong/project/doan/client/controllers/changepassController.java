package hoang_vuong.project.doan.client.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.mail.javamail.JavaMailSender;
import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class changepassController {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private KhachHangService dvl;
     @GetMapping("/apps/quen-mat-khau")
    public String getForgotPassword(Model model) {
        var dl = new KhachHang();

        model.addAttribute("dl", dl);
        model.addAttribute("title", "Quên Mật Khẩu");
        model.addAttribute("content", "client/changepassmail.html");

        return "layouts/layout-admin-login";
    }

    @PostMapping("/apps/quen-mat-khau")
    public String postForgotPassword(@RequestParam("email") String email,
            @RequestParam("g-recaptcha-response") String recaptchaResponse,
            RedirectAttributes redirectAttributes) {
        // In ra giá trị g-recaptcha-response để kiểm tra
        System.out.println("Đã nhận được phản hồi reCAPTCHA: " + recaptchaResponse);

        // Kiểm tra reCAPTCHA
        boolean isCaptchaVerified = verifyRecaptcha(recaptchaResponse);
        if (!isCaptchaVerified) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Vui lòng xác minh reCAPTCHA.");
            return "redirect:/apps/quen-mat-khau";
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
            return "redirect:/apps/quen-mat-khau";
        } else if (status == 2) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Có nhiều hơn một nhân viên với email: " + email);
            return "redirect:/apps/quen-mat-khau";
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
                return "redirect:/apps/quen-mat-khau";
            }
        }

        return "redirect:/apps/doi-mat-khau";
    }

    // // Phương thức tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(8);

    }

    private int updatePassword(String email, String newPassword) {
        List<KhachHang> danhSachNhanVien = dvl.timDanhSachkhachhangTheoEmail(email);

        if (danhSachNhanVien.isEmpty()) {
            // Không tìm thấy nhân viên nào với email này
            return 0;
        } else if (danhSachNhanVien.size() > 1) {
            // Tìm thấy nhiều hơn một nhân viên với email này
            return 2;
        } else {
            // Chỉ có một nhân viên với email này
            KhachHang nhanVien = danhSachNhanVien.get(0);
            var hash = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
            nhanVien.setMatKhau(hash);
            dvl.luuKhachHang(nhanVien);
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

    @GetMapping("/apps/doi-mat-khau")
    public String getChangePassword(@ModelAttribute("dl") KhachHang dl, Model model, HttpServletRequest request) {
        if (Qdl.KhachHangChuaDangNhap(request)) {
            return "redirect:/apps/auth";
        }

        model.addAttribute("title", "Đổi Mật Khẩu");
        model.addAttribute("content", "client/doimatkhau.html");

        return "layouts/layout-client";
    }

    @PostMapping("/apps/doi-mat-khau")
    public String PostChangePassword(
            @RequestParam("matKhau") String matKhau,
            @RequestParam("matKhauMoi") String matKhauMoi,
            @RequestParam("nhapLaiMatKhauMoi") String nhapLaiMatKhauMoi,
            HttpServletRequest request,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra xem người dùng có đăng nhập không
        if (Qdl.KhachHangChuaDangNhap(request)) {
            return "redirect:/apps/auth";
        }

        Integer nhanVienId = (Integer) session.getAttribute("khachhang_Id");

        if (nhanVienId == null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không tìm thấy thông tin nhân viên.");
            return "redirect:/apps/doi-mat-khau";
        }

        // Lấy nhân viên từ cơ sở dữ liệu bằng ID
        KhachHang nhanVien = dvl.timKhachHangTheoId(nhanVienId);

        // Kiểm tra mật khẩu cũ
        String old_password = nhanVien.getMatKhau();
        boolean dung_mat_khau = BCrypt.checkpw(matKhau, old_password);
        if (dung_mat_khau) {
            System.out.println("Mật khẩu cũ đúng");
        } else {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Mật khẩu cũ không đúng.");
            return "redirect:/apps/doi-mat-khau";
        }

        // Kiểm tra mật khẩu mới và mật khẩu nhập lại
        if (!matKhauMoi.equals(nhapLaiMatKhauMoi)) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Mật khẩu mới và mật khẩu nhập lại không khớp.");
            return "redirect:/apps/doi-mat-khau";
        } else {
            String inputPassword = matKhauMoi;
            var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));
            nhanVien.setMatKhau(hash);

            try {
                dvl.luuKhachHang(nhanVien);
                redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đổi mật khẩu thành công.");
                request.getSession().invalidate();
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                        "Không thể đổi mật khẩu. Mã lỗi: " + e.getMessage());
            }

            return "redirect:/apps/auth";
        }

    }
}
