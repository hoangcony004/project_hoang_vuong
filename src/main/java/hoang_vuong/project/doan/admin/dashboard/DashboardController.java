package hoang_vuong.project.doan.admin.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHangService;
import hoang_vuong.project.doan.admin.chitietdonhang.ThongKeSanPhamDTO;
import hoang_vuong.project.doan.admin.donhang.DonHang;
import hoang_vuong.project.doan.admin.donhang.DonHangService;
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

    @Autowired
    private ChiTietDonHangService chiTietDonHangService;
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
        double maxPercentageChange = 1000.0; // Giới hạn tỷ lệ phần trăm tối đa
        if (donthangtruoc > 0) {
            // Tính tỷ lệ phần trăm thay đổi
            phantramthaydoi = ((double) (donthangnay - donthangtruoc) / donthangtruoc) * 100;
            // Áp dụng giới hạn tối đa cho tỷ lệ phần trăm
            if (phantramthaydoi > maxPercentageChange) {
                phantramthaydoi = maxPercentageChange;
            }
        } else {
            // Khi donthangtruoc là 0, chỉ hiển thị 100% nếu donthangnay > 0
            phantramthaydoi = (donthangnay > 0) ? 100.0 : 0.0;
        }
        // Định dạng tỷ lệ phần trăm với tối đa hai chữ số thập phân
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
        int year = LocalDate.now().getYear(); // Lấy năm hiện tại
        // int year = 2025; // Thay đổi giá trị này để thử nghiệm
        List<String> doanhThuTheoThang = donHangService.getDoanhThuTheoThang(year);
        model.addAttribute("doanhThuTheoThang", doanhThuTheoThang);
        System.out.println("Doanh thu là: " + doanhThuTheoThang);

        // Thống kê tuần
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Doanh thu tuần này
        List<Double> currentWeekRevenue = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            double revenue = donHangService.calculateTotalRevenue(date, date); // Lấy doanh thu cho từng ngày
            currentWeekRevenue.add(revenue);
        }

        // Doanh thu tuần trước
        List<Double> lastWeekRevenue = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.minusWeeks(1).plusDays(i);
            double revenue = donHangService.calculateTotalRevenue(date, date); // Lấy doanh thu cho từng ngày
            lastWeekRevenue.add(revenue);
        }

        // Chuyển đổi giá trị doanh thu thành chuỗi bình thường
        List<String> currentWeekValues = currentWeekRevenue.stream()
                .map(revenue -> String.format("%.2f", revenue)) // Định dạng số
                .collect(Collectors.toList());

        List<String> lastWeekValues = lastWeekRevenue.stream()
                .map(revenue -> String.format("%.2f", revenue)) // Định dạng số
                .collect(Collectors.toList());

        // Chuyển đổi ngày trong tuần sang tiếng Việt
        List<DayOfWeek> daysOfWeek = List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

        List<String> vietnameseDaysOfWeek = daysOfWeek.stream()
                .map(day -> dayOfWeekTranslations.get(day))
                .collect(Collectors.toList());

        // Thêm vào mô hình
        model.addAttribute("xValuesTuan", vietnameseDaysOfWeek);
        model.addAttribute("dataSetCurrentWeek", currentWeekValues);
        model.addAttribute("dataSetLastWeek", lastWeekValues);

        // Định dạng doanh thu
        double revenueThisWeekTotal = currentWeekRevenue.stream().mapToDouble(Double::doubleValue).sum();
        double revenueLastWeekTotal = lastWeekRevenue.stream().mapToDouble(Double::doubleValue).sum();

        // Định dạng doanh thu
        NumberFormat formatter = new DecimalFormat("#,###" + " vn₫");
        String revenueThisWeek = formatter.format(revenueThisWeekTotal);
        String revenueLastWeek = formatter.format(revenueLastWeekTotal);

        // Thêm doanh thu vào mô hình
        model.addAttribute("revenueThisWeek", revenueThisWeek);
        model.addAttribute("revenueLastWeek", revenueLastWeek);

        System.out.println("Doanh thu tuần này: " + revenueThisWeek);
        System.out.println("Doanh thu tuần trước: " + revenueLastWeek);
        System.out.println("xValuesTuan: " + vietnameseDaysOfWeek);
        System.out.println("dataSet tuần này: " + currentWeekValues);
        System.out.println("dataSet tuần trước: " + lastWeekValues);

        // top 5 sản phẩm có đơn giá cao nhất
        List<List<String>> top5SanPham = sanPhamService.getTop5SanPhamByDonGia();
        model.addAttribute("top5SanPham", top5SanPham);

        System.out.println("top 5 sản Phẩm " + top5SanPham);

        // thống kê 10 sản phẩm bán chạy nhất
        // Gọi service để lấy danh sách 10 sản phẩm bán chạy nhất
        List<ThongKeSanPhamDTO> danhSachSanPham = chiTietDonHangService.thongKeTop10SanPham();

        // In thông tin chi tiết ra console
        System.out.println("Top 10 sản phẩm: ");
        for (ThongKeSanPhamDTO dto : danhSachSanPham) {
            System.out.println("Tên Sản Phẩm: " + dto.getTenSP());
            System.out.println("Đơn Giá: " + dto.getFormattedDonGia()); // Sử dụng phương thức định dạng
            System.out.println("Số Lượng Bán: " + dto.getSoLuongBan());
            System.out.println("Tổng Tiền: " + dto.getFormattedTongTien()); // Sử dụng phương thức định dạng
        }

        // Đưa danh sách sản phẩm vào model
        model.addAttribute("danhSachSanPham", danhSachSanPham);
        System.out.println("danh sách sản phẩm" + danhSachSanPham);

        // thống kê nhà sản xuất
        Map<String, Long> thongKe = sanPhamService.thongKeSanPhamTheoNhaSanXuat();
        model.addAttribute("thongKeData", thongKe);
        System.out.println("Data" + thongKe);

        model.addAttribute("title", "Dashboard");
        model.addAttribute("content", "admin/dashboard/dashboard.html");

        return "layouts/layout-admin.html";
    }

    private static final Map<DayOfWeek, String> dayOfWeekTranslations = Map.of(
            DayOfWeek.MONDAY, "Thứ Hai",
            DayOfWeek.TUESDAY, "Thứ Ba",
            DayOfWeek.WEDNESDAY, "Thứ Tư",
            DayOfWeek.THURSDAY, "Thứ Năm",
            DayOfWeek.FRIDAY, "Thứ Sáu",
            DayOfWeek.SATURDAY, "Thứ Bảy",
            DayOfWeek.SUNDAY, "Chủ Nhật");

}
