package hoang_vuong.project.doan.admin.dashboard;

import org.apache.catalina.connector.Request;
// import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;

import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ThongKeController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SanPhamService dvl;

    @GetMapping("/thong-ke-san-pham")
    public String getThongKeSanPham(Model model, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            Map<String, Long> thongKe = dvl.thongKeSanPhamTheoNhaSanXuat();
            Map<String, Long> thongKeTrangThai = dvl.thongKeSanPhamTheoTrangThai();
            model.addAttribute("thongKeData", thongKe);
            model.addAttribute("thongKeDataTrangThai", thongKeTrangThai);
            // Ví dụ nếu thongKeTrangThai là boolean
            // Kiểm tra dữ liệu trước khi thêm vào model
            if (thongKe == null || thongKeTrangThai == null) {
                System.err.println("Dữ liệu thống kê là null.");
            } else {
                System.out.println("Trang thai la: " + thongKeTrangThai);
            }

            model.addAttribute("content", "admin/dashboard/thongkesanpham.html");
            model.addAttribute("title_duyet", "Thống Kê Sản Phẩm");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thống kê sản phẩm. Mã lỗi: " + e.getMessage());
        }

        return "layouts/layout-admin.html";
    }

}
