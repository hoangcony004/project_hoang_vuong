package hoang_vuong.project.doan.qdl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuat;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuatService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import jakarta.servlet.http.HttpSession;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class QdlIndex {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TimKiemService dvls;
    @Autowired
    private SanPhamService dvl;
    @Autowired
    private NhaSanXuatService nsxdv;
 
     @GetMapping("/") 
    public String get(Model model, HttpSession session) {
    //  java.util.List<SanPham> list = dvl.dsSanPham();
    List<SanPham> noibat = dvl.dsSanPhamNoiBat();
      List<SanPham> banchay = dvl.dsSanPhamBanChay();        
                model.addAttribute("ds_noibat", noibat);
                model.addAttribute("ds_banchay", banchay);    
    //  model.addAttribute("ds", list);   
     java.util.List<NhaSanXuat> nsx=  nsxdv.duyet();
     model.addAttribute("dlnsx", nsx);
     model.addAttribute("content", "client/index.html");
    
    
      // System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));
   return "layouts/layout-client.html";
    }

    @GetMapping("/admin/tim-kiem-in-app")
    public String getTimKiemApp(@RequestParam("search") String search, Model model, HttpServletRequest request) {
        if (Qdl.NhanVienChuaDangNhap(request)) {
            return "redirect:/admin/dang-nhap";
        }

        if (search != null && !search.isEmpty()) {
            List<TimKiem> results = dvls.timKiemChucNang(search);
            model.addAttribute("results", results);
            model.addAttribute("searchQuery", search);
        }

        model.addAttribute("title", "Quản Lý Tìm Kiếm");
        model.addAttribute("content", "admin/timkiem/tim-kiem-result.html");

        return "layouts/layout-admin.html";
    }

}
