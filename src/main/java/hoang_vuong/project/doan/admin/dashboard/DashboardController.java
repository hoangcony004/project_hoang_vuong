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
import java.text.DecimalFormat;

@Controller
@RequestMapping("/admin")
public class DashboardController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private KhachHangService dvl;

    @Autowired
    private SanPhamService sanPhamService;

    // v1
    // @GetMapping("/dashboard")
    // public String getDashboard(Model model) {
    // if (Qdl.NhanVienChuaDangNhap(request))
    // return "redirect:/admin/dang-nhap";

    // // Lấy danh sách tất cả khách hàng
    // List<KhachHang> allKhachHang = dvl.dsKhachHang();

    // long currentMonthCount = dvl.getNumberOfCustomersThisMonth();
    // long lastMonthCount = dvl.getNumberOfCustomersLastMonth();
    // double percentageChange = dvl.calculateChangePercentage(currentMonthCount,
    // lastMonthCount);
    // model.addAttribute("totalCustomers", currentMonthCount);
    // model.addAttribute("percentageChange", percentageChange);
    // model.addAttribute("isIncrease", percentageChange >= 0);
    // // Truyền số lượng khách hàng vào mô hình
    // model.addAttribute("totalCustomers", allKhachHang.size());

    // model.addAttribute("title", "Dashboard");
    // model.addAttribute("content", "admin/dashboard/dashboard.html");

    // return "layouts/layout-admin.html";
    // }

    // v2
    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Lấy danh sách tất cả khách hàng
        List<KhachHang> allKhachHang = dvl.dsKhachHang();

        long currentMonthCount = dvl.getNumberOfCustomersThisMonth();
        long lastMonthCount = dvl.getNumberOfCustomersLastMonth();
        double percentageChange = dvl.calculateChangePercentage(currentMonthCount, lastMonthCount);

        // Định dạng percentageChange chỉ lấy 1 số sau dấu phẩy
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedPercentageChange = df.format(percentageChange);

        model.addAttribute("totalCustomers", currentMonthCount);
        model.addAttribute("percentageChange", formattedPercentageChange);
        model.addAttribute("isIncrease", percentageChange >= 0);
        // Truyền số lượng khách hàng vào mô hình
        model.addAttribute("totalCustomers", allKhachHang.size());

        model.addAttribute("title", "Dashboard");
        model.addAttribute("content", "admin/dashboard/dashboard.html");

        return "layouts/layout-admin.html";
    }

}
