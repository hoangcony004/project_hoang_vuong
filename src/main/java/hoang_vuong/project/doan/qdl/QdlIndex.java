package hoang_vuong.project.doan.qdl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import jakarta.servlet.http.HttpSession;

@RestController
public class QdlIndex 
{
      @Autowired
    private SanPhamService dvl;
 @GetMapping("/") 
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

}


