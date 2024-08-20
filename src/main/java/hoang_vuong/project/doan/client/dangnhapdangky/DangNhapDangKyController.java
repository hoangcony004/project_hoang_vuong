package hoang_vuong.project.doan.client.dangnhapdangky;

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
    @GetMapping("/dang-nhap") 
    public String get(Model model, HttpSession session) {
        var dl = new KhachHang();
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        String khachhang_Email = (String) session.getAttribute("khachhang_Email");
        model.addAttribute("khachhang_Id", khachhang_Id);
        model.addAttribute("khachhang_Email", khachhang_Email);
        System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));
        model.addAttribute("dl", dl);
        model.addAttribute("content", "client/index.hml");
     return "client/index";
    }
        @GetMapping("/dang-ky") 
    public String getDuyet(Model model, HttpSession session) {
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        if(khachhang_Id !=null){
            return "redirect:/apps/dang-nhap";
        }
        var dl = new KhachHang();
        model.addAttribute("dl", dl);
        model.addAttribute("title_add", "Thêm Khách Hàng");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("action", "/apps/dang-ky");
     return "client/login";
    }
  @PostMapping("/dang-ky")
    public String postThem(@ModelAttribute("KhachHang") KhachHang dl,
            RedirectAttributes redirectAttributes) {
        // Mã hóa mật khẩu
        var inputPassword = dl.getMatKhau();
        var hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));

        dl.setMatKhau(hash);
        dl.setNgayTao(LocalDate.now());

        dvl.luuKhachHang(dl);

        redirectAttributes.addFlashAttribute("THONG_BAO", "Đã thêm mới thành công!");

        return "redirect:/apps/dang-ky";
    }

    
    @PostMapping("/dang-nhap")
    public String postDangNhap(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("email") String TenDangNhap,
            @RequestParam("matKhau") String MatKhau,
            HttpServletRequest request,
            HttpSession session) {

                String old_pss = null;
        if (dvl.dacoemail(TenDangNhap)) {
            var old_dl = dvl.timEmmail(TenDangNhap);
            old_pss= old_dl.getMatKhau();
            if (BCrypt.checkpw(MatKhau,old_pss )) {
                // Đăng nhập thành công
                request.getSession().setAttribute("khachhang_Id", old_dl.getId());
                request.getSession().setAttribute("khachhang_Email", old_dl.getEmail());
        
                var uriBeforeLogin = (String) session.getAttribute("URI_BEFORE_LOGIN");
                return uriBeforeLogin != null ? "redirect:" + uriBeforeLogin : "redirect:/apps/dang-nhap";
            } else {
                // Sai mật khẩu
                redirectAttributes.addFlashAttribute("THONG_BAO", "Sai tên đăng nhập hoặc mật khẩu!");
                return "redirect:/apps/dang-ky";
            }
        } else {
            // Không tồn tại tên đăng nhập
            redirectAttributes.addFlashAttribute("THONG_BAO", "Sai tên đăng nhập hoặc mật khẩu!");
            return "redirect:/apps/dang-ky";
        }
    }
    @GetMapping("/dang-xuat")
    public String getDangThoat(Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("THONG_BAO", "Đăng xuất thành công.");
        request.getSession().invalidate();
        return "redirect:/apps/dang-nhap";
    }
}
