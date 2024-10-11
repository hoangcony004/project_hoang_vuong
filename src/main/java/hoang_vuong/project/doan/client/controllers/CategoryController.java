package hoang_vuong.project.doan.client.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CategoryController {

    @Autowired
    private SanPhamService dvl;

    @PostMapping("/apps/categories")
    public String postFilter(
            @RequestParam(value = "minPrice", required = false) Float minPrice,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice,
            @RequestParam(value = "category", required = false) Integer maNSX,
            @RequestParam(defaultValue = "1") int page,  // Default to first page
            @RequestParam(defaultValue = "9") int pageSize,  // Default page size
            RedirectAttributes redirectAttributes,
            Model model) {
    
        try {
        
            // Create pagination information
            int pageIndex = page - 1;  // PageRequest is 0-based, so subtract 1

    
            Pageable pageable = PageRequest.of(pageIndex, pageSize);
            // Filter products using the service
            Page<SanPham> sanPhams = dvl.locSanPhamKhongFilterBanChayNoiBat(minPrice, maxPrice, maNSX, pageable);
    
            // Check if no products were found
            if (sanPhams.isEmpty()) {
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không có kết quả nào khớp với nội dung lọc.");
                return "redirect:/apps/categories";
            } else {
                // Pass filtered data to model
                model.addAttribute("dlns", sanPhams.getContent());
                model.addAttribute("dl", new SanPham());
                model.addAttribute("content", "client/category.html");
                model.addAttribute("THONG_BAO_SUCCESS", "Lọc thành công!");
    
                // Add pagination information to the model
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", sanPhams.getTotalPages());
                model.addAttribute("pageSize", pageSize);
            }
    
        } catch (Exception e) {
            // Handle error
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể lọc. Mã lỗi: " + e.getMessage());
            return "redirect:/apps/categories";
        }
    
        return "layouts/layout-client";
    }
    

    @GetMapping("/apps/categories")
    public String getDuyetSanPham(Model model,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "9") int pageSize,
        @RequestParam(defaultValue = "tenSP") String sort, // Sắp xếp theo tên sản phẩm
        @RequestParam(defaultValue = "asc") String direction, // Hướng sắp xếp
        @RequestParam(value = "id", required = false) Integer id, // ID tùy chọn
        HttpServletRequest request) {
      // Chuyển đổi page từ 1-based thành 0-based
      int pageIndex = page - 1;
  
      // Thiết lập sắp xếp
      Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
      Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(sortDirection, sort));
  
      Page<SanPham> sanPhamPage;
      List<SanPham> list;
  
      // Nếu `id` không null, tìm sản phẩm theo `id`
      if (id != null && id != 0) {
        // Tìm sản phẩm theo `id` và trả về kết quả phân trang
        sanPhamPage = dvl.duyetSanPhamTheoId(id, pageable);
      } else {
        // Nếu không có `id`, trả về tất cả sản phẩm với phân trang và sắp xếp
        sanPhamPage = dvl.duyetSanPham(pageable);
      }
      list = sanPhamPage.getContent();
  
      // Thêm các thuộc tính cần thiết vào model để hiển thị trong view
      model.addAttribute("dlns", list); // Danh sách sản phẩm
      model.addAttribute("page", sanPhamPage); // Thông tin phân trang
      model.addAttribute("dl", new SanPham()); // Đối tượng sản phẩm để thêm mới
  
      // Thêm các thuộc tính cho phân trang và sắp xếp
      model.addAttribute("currentPage", page);
      model.addAttribute("totalPages", sanPhamPage.getTotalPages());
      model.addAttribute("pageSize", pageSize);
      model.addAttribute("id", id != null ? id : 0);
  
      model.addAttribute("sort", sort); // Cột sắp xếp
      model.addAttribute("direction", direction); // Hướng sắp xếp (asc/desc)
      model.addAttribute("sortDirection", direction.equals("asc") ? "asc" : "desc");
      int startIndex = (page - 1) * pageSize;
      model.addAttribute("startIndex", startIndex);
  
      model.addAttribute("content", "client/category.html");
      return "layouts/layout-client";
    }
     
}