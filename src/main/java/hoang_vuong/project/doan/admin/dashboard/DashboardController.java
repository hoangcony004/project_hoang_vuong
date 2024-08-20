package hoang_vuong.project.doan.admin.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class DashboardController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Lấy danh sách tất cả khách hàng
        List<KhachHang> allKhachHang = khachHangService.dsKhachHang();

        long currentMonthCount = khachHangService.getNumberOfCustomersThisMonth();
        long lastMonthCount = khachHangService.getNumberOfCustomersLastMonth();
        double percentageChange = khachHangService.calculateChangePercentage(currentMonthCount, lastMonthCount);
        model.addAttribute("totalCustomers", currentMonthCount);
        model.addAttribute("percentageChange", percentageChange);
        model.addAttribute("isIncrease", percentageChange >= 0);
        // Truyền số lượng khách hàng vào mô hình
        model.addAttribute("totalCustomers", allKhachHang.size());

        // List<SanPham> danhSachSanPham = sanPhamService.dsSanPham();
        // model.addAttribute("dsSanPham", danhSachSanPham);

        model.addAttribute("title", "Dashboard");
        model.addAttribute("content", "admin/dashboard/dashboard.html");

        return "layouts/layout-admin.html";
    }
}
