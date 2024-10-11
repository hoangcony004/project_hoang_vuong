package hoang_vuong.project.doan.client.favourite;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpSession;

@Controller
public class favouriteController {
    @Autowired
    private HttpServletRequest request;
      @Autowired
    private favouriteService dvl;
  @Autowired
    private SanPhamService dvlsp;

    @GetMapping("/apps/favourite")
    public String getfavourite(Model model, RedirectAttributes redirectAttributes , HttpSession session){
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        if (Qdl.KhachHangChuaDangNhap(request)){
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Đăng nhập để vào mục này.");
            return "redirect:/";
        }
        List<Integer> productIds = dvl.getProductIdsByUserId(khachhang_Id);
        List<SanPham> bien=dvlsp.getProductsByIds(productIds);
        model.addAttribute("dlfavourite", bien);
        model.addAttribute("content", "client/wishlist.html");
        return "layouts/layout-client";
    }
  
    @PostMapping("/add/favourite")
    public ResponseEntity<String> addFavourite(@RequestParam("product-id") int productId, HttpSession session) {
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
    
        if (Qdl.KhachHangChuaDangNhap(request)){
            return ResponseEntity.status(403).body("mày chưa đăng nhập");
        }
        dvl.addFavourite(khachhang_Id, productId);
    
        return ResponseEntity.ok("Favourite added successfully");
    }
     @DeleteMapping("/apps/{userId}/product/{productId}")
    public ResponseEntity<String> removeFavourite(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {
        dvl.removeFavourite(userId, productId);
        return ResponseEntity.ok("Favourite removed successfully");
    }
  
}
