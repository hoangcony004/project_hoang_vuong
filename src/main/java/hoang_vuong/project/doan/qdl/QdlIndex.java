package hoang_vuong.project.doan.qdl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuat;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuatService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
@Controller
public class QdlIndex 
{
  @Autowired
    private SanPhamService dvl;
    @Autowired
    private NhaSanXuatService nsxdv;
   
     @GetMapping("/") 
    public String get(Model model, HttpSession session) {
      Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
      String khachhang_Email = (String) session.getAttribute("khachhang_Email");
      Integer cartQuantity = (Integer)session.getAttribute("SoSanPhamTrongGioHang");
if (cartQuantity == null) {
    cartQuantity = 0; // Hoặc giá trị mặc định khác
}
    //  java.util.List<SanPham> list = dvl.dsSanPham();
    List<SanPham> noibat = dvl.dsSanPhamNoiBat();
      List<SanPham> banchay = dvl.dsSanPhamBanChay();        
                model.addAttribute("ds_noibat", noibat);
                model.addAttribute("ds_banchay", banchay);    
    //  model.addAttribute("ds", list);
    
     java.util.List<NhaSanXuat> nsx=  nsxdv.duyet();
     model.addAttribute("dlnsx", nsx);
     model.addAttribute("content", "client/index.html");
      model.addAttribute("khachhang_Id", khachhang_Id);
      model.addAttribute("khachhang_Email", khachhang_Email);
      model.addAttribute("SoSanPhamTrongGioHang", cartQuantity);
      // System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));
   return "layouts/layout-client.html";
    }

}


