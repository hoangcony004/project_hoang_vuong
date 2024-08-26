package hoang_vuong.project.doan.client.trangchu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/apps")
public class home {
 @Autowired
    private HttpServletRequest request;
      @Autowired
    private SanPhamService dvl;
   
     @GetMapping("/home") 
    public String get(Model model, HttpSession session) {
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        String khachhang_Email = (String) session.getAttribute("khachhang_Email");
        java.util.List<SanPham> list = dvl.dsSanPham();
       model.addAttribute("ds", list);
        model.addAttribute("khachhang_Id", khachhang_Id);
        model.addAttribute("khachhang_Email", khachhang_Email);
        // System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));
        // model.addAttribute("content", "client/index.hml");
     return "client/index.html";
    }
    @GetMapping("/product") 
 public String getXem(Model model, @RequestParam(value = "id") int id) {
        // if (Qdl.KhachHangChuaDangNhap(request))
        //     return "redirect:/apps/dang-ky";

        var dl = dvl.timTheoId(id);

        model.addAttribute("dl", dl);
       // model.addAttribute("content", "admin/nhanvien/xem.html");

        return "client/product-centered.html";
    }
}
