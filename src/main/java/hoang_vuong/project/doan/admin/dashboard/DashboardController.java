package hoang_vuong.project.doan.admin.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hoang_vuong.project.doan.admin.donhang.DonHang;
import hoang_vuong.project.doan.admin.donhang.DonHangService;
import hoang_vuong.project.doan.admin.donhang.RevenueModel;
import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Controller
@RequestMapping("/admin")
public class DashboardController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private KhachHangService dvl;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DonHangService donHangService;

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

        // đơn hàng
        long donthangnay = donHangService.getCurrentMonthOrderCount();
        long donthangtruoc = donHangService.getPreviousMonthOrderCount();
        double phantramthaydoi;

        if (donthangtruoc != 0) {
            phantramthaydoi = donHangService.calculatePercentageChange(donthangnay, donthangtruoc);
        } else {
            phantramthaydoi = (donthangnay > 0) ? 100.0 : 0.0; // Nếu donthangtruoc là 0 và donthangnay > 0 thì tỷ lệ
                                                               // thay đổi là 100%
        }

        // Định dạng tỷ lệ phần trăm
        DecimalFormat order = new DecimalFormat("#.##");
        String formattedPercentageChangeOrder = order.format(phantramthaydoi);

        model.addAttribute("currentMonthCountOrder", donthangnay);
        model.addAttribute("percentageChangeOrder", formattedPercentageChangeOrder);

        // Lấy tổng tiền từ service
        Float tongTienFloat = donHangService.getTongTien();
        // Tạo đối tượng DecimalFormat để định dạng số
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        // Định dạng tổng tiền thành chuỗi
        String tongTienFormatted = decimalFormat.format(tongTienFloat);
        // Thêm tổng tiền định dạng vào model
        model.addAttribute("tongTien", tongTienFormatted);

        // thống kê từng tháng
        // List<Double> doanhThuTheoThang = donHangService.getDoanhThuTheoThang();
        // model.addAttribute("doanhThuTheoThang", doanhThuTheoThang);
        // System.out.println("doanh thu là :" + doanhThuTheoThang);
        int year = LocalDate.now().getYear(); // Lấy năm hiện tại
        // int year = 2024; // Thay đổi giá trị này để thử nghiệm
        List<Double> doanhThuTheoThang = donHangService.getDoanhThuTheoThang(year);
        model.addAttribute("doanhThuTheoThang", doanhThuTheoThang);
        System.out.println("Doanh thu là: " + doanhThuTheoThang);

        // thống kê tuần
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Map<DayOfWeek, String> weeklyStats = donHangService.getWeeklyStatistics();

        // Tạo danh sách ngày trong tuần với giá trị mặc định 0.0 nếu không có dữ liệu
        List<DayOfWeek> daysOfWeek = List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        List<String> values = daysOfWeek.stream()
                .map(day -> weeklyStats.getOrDefault(day, "0.0"))
                .collect(Collectors.toList());

        model.addAttribute("xValuesTuan", daysOfWeek.stream().map(DayOfWeek::toString).collect(Collectors.toList()));
        model.addAttribute("dataSet", values);

        System.out.println("thống kê tuần" + values);

        model.addAttribute("title", "Dashboard");
        model.addAttribute("content", "admin/dashboard/dashboard.html");

        return "layouts/layout-admin.html";
    }

}
