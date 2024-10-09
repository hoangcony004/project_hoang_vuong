package hoang_vuong.project.doan.client.controllers;

import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/apps")
public class DangNhapDangKyController {

    @Autowired
    private KhachHangService dvl;

    @GetMapping("/auth")
    public String getAuth(Model model, HttpSession session) {
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        if (khachhang_Id != null) {
            return "redirect:/apps/auth";
        }
        var dl = new KhachHang();
        model.addAttribute("dl", dl);
        model.addAttribute("title", "Đăng Nhập - Đăng Ký");
        model.addAttribute("content", "client/login.html");
        return "layouts/layout-client";
    }

    @PostMapping("/auth_register")
    public String postAdd(@ModelAttribute("KhachHang") KhachHang dl,
            @RequestParam("emailcom") String emailcom,
            RedirectAttributes redirectAttributes) {
        // Mã hóa mật khẩu
        var inputPassword = dl.getMatKhau();
        var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));
            if(dvl.dacoemail(emailcom)){
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Đăng ký không thành công! Email đã tồn tại!");
                return "redirect:/apps/auth";
                }else{
                    dl.setEmail(emailcom);
                }
        dl.setMatKhau(hash);
        dl.setNgayTao(LocalDate.now());

        dl.setNgaySua(LocalDate.now());
        try {
            dvl.luuKhachHang(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã đăng ký thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Đăng ký không thành công!");
        }

        return "redirect:/apps/auth";
    }

    @PostMapping("/auth_login")
    public String postLogin(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("email") String TenDangNhap,
            @RequestParam("matKhau") String MatKhau,
            HttpServletRequest request,
            HttpSession session) {

        String old_pss = null;

        if (dvl.dacoemail(TenDangNhap) || dvl.dacosdt(TenDangNhap)) {
            KhachHang old_dl = null;
            if (TenDangNhap.contains("@")) {
                // Kiểm tra email
                old_dl = dvl.timEmmail(TenDangNhap);
            } else {
                // Kiểm tra số điện thoại
                old_dl = dvl.timSodienthoai(TenDangNhap);
            }
            old_pss = old_dl.getMatKhau();
            if (BCrypt.checkpw(MatKhau, old_pss)) {
                // Đăng nhập thành công
                request.getSession().setAttribute("khachhang_Id", old_dl.getId());
                request.getSession().setAttribute("khachhang_Email", old_dl.getEmail());

                var uriBeforeLogin = (String) session.getAttribute("URI_BEFORE_LOGIN_USER");
                redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đăng nhập thành công!");
                return uriBeforeLogin != null ? "redirect:" + uriBeforeLogin : "redirect:/";
            } else {
                // Sai mật khẩu
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Sai tên đăng nhập hoặc mật khẩu!");
                return "redirect:/apps/auth";
            }
        } else {
            // Không tồn tại tên đăng nhập
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Sai tên đăng nhập hoặc mật khẩu!");
            return "redirect:/apps/auth";
        }
    }

    @GetMapping("/dang-xuat")
    public String getDangThoat(Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đăng xuất thành công.");
        request.getSession().invalidate();
        return "redirect:/apps";
    }
}
