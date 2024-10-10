package hoang_vuong.project.doan.client.controllers;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.admin.donhang.DonHang;
import hoang_vuong.project.doan.admin.donhang.DonHangService;
import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    KhachHangService khSV;
    @Autowired
    DonHangService dhSV;

    @GetMapping("/apps/account")
    public String getAcount(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String khachhang_Email = (String) session.getAttribute("khachhang_Email");
        // Nếu email không tồn tại hoặc rỗng, chuyển hướng về trang /apps
        if (khachhang_Email == null || khachhang_Email.isEmpty()) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Bạn cần đăng nhập để truy cập trang này.");
            return "redirect:/apps";
        }
        // Lấy thông tin khách hàng theo email
        KhachHang formkhachhang = khSV.timEmmail(khachhang_Email);

        List<DonHang> donhang = dhSV.dsDonhangEmail(khachhang_Email);
        System.out.println(donhang + "alosoalo");
        // Thêm thông tin khách hàng vào model
        model.addAttribute("donhang", donhang);
        model.addAttribute("formKH", formkhachhang);

        model.addAttribute("content", "client/dashboard.html");
        return "layouts/layout-client";
    }

    @PostMapping("/apps/account")
    @ResponseBody
    public ResponseEntity<Map<String, String>> postEdit(@ModelAttribute("formKH") KhachHang dl) {
        Map<String, String> response = new HashMap<>();

        dl.setNgaySua(LocalDate.now());

        try {
            khSV.luuKhachHang(dl);
            response.put("message", "Đã sửa thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Không thể sửa. Mã lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/donhang/xoa")
    public ResponseEntity<?> xoaDonHang(@RequestParam("id") int id) {
        // Gọi phương thức trong service để xóa đơn hàng
        boolean isDeleted = dhSV.xoaDonHang(id);

        if (isDeleted) {
            return ResponseEntity.ok().body(Map.of("success", "Đơn hàng đã được xóa thành công!"));
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Có lỗi xảy ra trong quá trình xóa đơn hàng."));
        }
    }

}
