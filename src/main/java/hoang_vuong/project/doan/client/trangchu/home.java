package hoang_vuong.project.doan.client.trangchu;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.admin.anhsanpham.AnhSanPham;
import hoang_vuong.project.doan.admin.anhsanpham.AnhSanPhamService;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuat;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuatService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class home {
 @Autowired
    private HttpServletRequest request;
      @Autowired
    private SanPhamService dvl;
    @Autowired
    private NhaSanXuatService nsxdv;
    @Autowired
    private AnhSanPhamService anhdv;
   
public String formatPrice(float price) {
    DecimalFormat formatter = new DecimalFormat("#,###.00");  // Định dạng: phân cách hàng nghìn và 2 chữ số thập phân
    return formatter.format(price);
}
 
    @GetMapping({
      "/apps/",
    "/"
    }
)
    public String get(Model model, HttpSession session) {
      
        // java.util.List<SanPham> list = dvl.dsSanPham();
      List<SanPham> noibat = dvl.dsSanPhamNoiBat();
        List<SanPham> banchay = dvl.dsSanPhamBanChay();  
      
      
      // Format giá cho từng sản phẩm trong danh sách banchay

                  model.addAttribute("ds_noibat", noibat);
                  model.addAttribute("ds_banchay", banchay);    
      //  model.addAttribute("ds", list);
       java.util.List<NhaSanXuat> nsx=  nsxdv.duyet();
       model.addAttribute("dlnsx", nsx);
       model.addAttribute("content", "client/index.html");
       
        // System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));
     return "layouts/layout-client.html";
    }
    @GetMapping("/apps/product") 
 public String getXem(Model model, @RequestParam(value = "id") int id) {
        // if (Qdl.KhachHangChuaDangNhap(request))
        //     return "redirect:/apps/dang-ky";
      var dl = anhdv.dsmasp(id);
    model.addAttribute("ds", dl);
        var dls = dvl.timTheoId(id);

        float price = dls.getDonGia();
        NumberFormat formatTienViet = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
      String giaDinhDang = formatTienViet.format(price);
      model.addAttribute("price", giaDinhDang);

        model.addAttribute("dl", dls);
       java.util.List<NhaSanXuat> nsx=  nsxdv.duyet();
        model.addAttribute("dlnsx", nsx);
       model.addAttribute("content", "client/product-sidebar.html");
        return "layouts/layout-client.html";
        
    }
  
 
    

}
