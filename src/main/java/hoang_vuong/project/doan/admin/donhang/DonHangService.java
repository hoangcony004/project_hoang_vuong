package hoang_vuong.project.doan.admin.donhang;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hoang_vuong.project.doan.admin.configs.DateUtils;

@Service
public class DonHangService {
    @Autowired
    private DonHangRepository kdl;

<<<<<<< HEAD
    public List<DoanhThuThang> thongKeDoanhThuTheoThang(int year, int month) {
        // Lấy danh sách doanh thu từ database
        List<DoanhThuThang> doanhThuList = kdl.findDoanhThuTheoThang(year, month);
        
        // Tạo danh sách 31 phần tử, mặc định là 0 cho mỗi ngày trong tháng
        List<DoanhThuThang> doanhThuTheoNgay = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            doanhThuTheoNgay.add(new DoanhThuThang(0.0, i + 1, month, year));  // Thay Float bằng Double
        }
    
=======
    public Map<Integer, Integer> getThongKeDonHang(int year) {
        List<Object[]> results = kdl.countDonHangByMonth(year);
        Map<Integer, Integer> statistics = new HashMap<>();

        // Khởi tạo các tháng với giá trị 0
        for (int month = 1; month <= 12; month++) {
            statistics.put(month, 0);
        }

        // Cập nhật số lượng từ kết quả
        for (Object[] result : results) {
            int month = Integer.parseInt(result[0].toString());
            int count = Integer.parseInt(result[1].toString());
            statistics.put(month, count);
        }

        return statistics;
    }

    public Map<Integer, Long> getOrderCountByYear() {
        List<Object[]> results = kdl.countOrdersByYear();
        Map<Integer, Long> orderCountByYear = new HashMap<>();
        for (Object[] result : results) {
            orderCountByYear.put((Integer) result[0], (Long) result[1]);
        }
        return orderCountByYear;
    }

    // tìm kiếm đơn hàng
    public List<DonHang> searchOrdersByMaDH(String query) {
        return kdl.searchOrdersByMaDH(query);
    }

    // lấy tổng đơn hàng đang có
    public Long getTotalOrders() {
        return kdl.countTotalOrders();
    }

    public List<DoanhThuThang> thongKeDoanhThuTheoThang(int year, int month) {
        // Lấy danh sách doanh thu từ database
        List<DoanhThuThang> doanhThuList = kdl.findDoanhThuTheoThang(year, month);

        // Tạo danh sách 31 phần tử, mặc định là 0 cho mỗi ngày trong tháng
        List<DoanhThuThang> doanhThuTheoNgay = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            doanhThuTheoNgay.add(new DoanhThuThang(0.0, i + 1, month, year)); // Thay Float bằng Double
        }

>>>>>>> 8d87d435b83f27f926b838331e9ec5853058b106
        // Duyệt qua danh sách doanh thu và cập nhật vào danh sách theo ngày
        for (DoanhThuThang dt : doanhThuList) {
            int day = dt.getDay();
            doanhThuTheoNgay.set(day - 1, new DoanhThuThang(dt.getTongTien(), day, month, year));
        }
<<<<<<< HEAD
    
        return doanhThuTheoNgay;  // Trả về danh sách doanh thu theo ngày
=======

        return doanhThuTheoNgay; // Trả về danh sách doanh thu theo ngày
>>>>>>> 8d87d435b83f27f926b838331e9ec5853058b106
    }

    public String getTotalRevenueByYear(int year) {
        // Lấy tổng doanh thu từ repository
        Float totalRevenue = kdl.findTotalRevenueByYear(year);

        // Nếu tổng doanh thu không null, định dạng số thành chuỗi
        if (totalRevenue != null) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            return decimalFormat.format(totalRevenue);
        } else {
            return "0"; // Hoặc giá trị mặc định khác nếu không có doanh thu
        }
    }

    public Map<Integer, String> getRevenueByYear() {
        List<Object[]> results = kdl.getRevenueByYear();
        Map<Integer, String> revenueMap = new HashMap<>();
        DecimalFormat formatter = new DecimalFormat("####"); // Định dạng số

        for (Object[] result : results) {
            int year = ((Number) result[0]).intValue();
            float totalRevenue = ((Number) result[1]).floatValue();
            String formattedRevenue = formatter.format(totalRevenue);
            revenueMap.put(year, formattedRevenue);
        }
        return revenueMap;
    }

    // Hàm tạo mã đơn hàng không trùng
    public String generateOrderCode() {
        String orderCode;
        do {
            // Tạo mã đơn hàng với "DH" + 6 số ngẫu nhiên
            orderCode = "DH" + String.format("%06d", new Random().nextInt(999999));
        } while (kdl.existsByMaDH(orderCode)); // Kiểm tra xem mã đã tồn tại chưa

        return orderCode;
    }

    // thống kê tuần
    public double calculateTotalRevenue(LocalDate startDate, LocalDate endDate) {
        return kdl.calculateTotalRevenue(startDate, endDate);
    }

    public Map<String, List<String>> getWeeklyRevenue() {
        LocalDate now = LocalDate.now();

        // Tuần hiện tại
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Tuần trước
        LocalDate startOfLastWeek = startOfWeek.minusWeeks(1);
        LocalDate endOfLastWeek = endOfWeek.minusWeeks(1);

        // Lấy doanh thu từng tuần
        double revenueThisWeek = kdl.calculateTotalRevenue(startOfWeek, endOfWeek);
        double revenueLastWeek = kdl.calculateTotalRevenue(startOfLastWeek, endOfLastWeek);

        // Định dạng số
        DecimalFormat df = new DecimalFormat("#.##");

        List<String> dataSetThisWeek = new ArrayList<>();
        List<String> dataSetLastWeek = new ArrayList<>();

        // Giả sử bạn có danh sách doanh thu cho từng ngày trong tuần
        for (int i = 0; i < 7; i++) {
            dataSetThisWeek.add(df.format(revenueThisWeek)); // Lấy doanh thu từng ngày
            dataSetLastWeek.add(df.format(0.0)); // Giả sử không có doanh thu tuần trước
        }

        return Map.of(
                "thisWeek", dataSetThisWeek,
                "lastWeek", dataSetLastWeek);
    }

    public List<DonHang> findAllByWeek(LocalDate startDate, LocalDate endDate) {
        return kdl.findAllByNgayTaoBetween(startDate, endDate);
    }

    public Map<DayOfWeek, String> getWeeklyStatistics() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<DonHang> weeklyDonHang = findAllByWeek(startOfWeek, endOfWeek);

        // Tổng hợp dữ liệu cho mỗi ngày trong tuần
        return weeklyDonHang.stream()
                .collect(Collectors.groupingBy(
                        dh -> dh.getNgayTao().getDayOfWeek(),
                        Collectors.summingDouble(dh -> dh.getTongTien() != null ? dh.getTongTien() : 0)))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> String.format("%.0f", e.getValue())));
    }

    public List<String> getDoanhThuTheoThang(int year) {
        // Khởi tạo danh sách với giá trị mặc định là "0.0" cho 12 tháng
        List<String> doanhThuTheoThang = new ArrayList<>(Collections.nCopies(12, "0.0"));

        // Lấy dữ liệu từ repository
        List<Object[]> result = kdl.getDoanhThuTheoThangTheoNam(year);

        // Cập nhật giá trị doanh thu vào danh sách
        for (Object[] row : result) {
            int month = ((Number) row[0]).intValue() - 1; // Tháng bắt đầu từ 0
            double total = ((Number) row[1]).doubleValue();
            doanhThuTheoThang.set(month, String.format("%.2f", total)); // Chuyển đổi giá trị thành chuỗi với định dạng
                                                                        // 2 chữ số thập phân
        }

        return doanhThuTheoThang;
    }

    public Float getTongTien() {
        return kdl.getTongTien();
    }

    public long getCurrentMonthOrderCount() {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();
        return kdl.countByDateRange(startDate, endDate);
    }

    public long getPreviousMonthOrderCount() {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        LocalDate startDate = previousMonth.atDay(1);
        LocalDate endDate = previousMonth.atEndOfMonth();
        return kdl.countByDateRange(startDate, endDate);
    }

    public double calculatePercentageChange(long currentMonthCount, long previousMonthCount) {
        if (previousMonthCount == 0) {
            return currentMonthCount > 0 ? 100.0 : 0.0;
        }
        return ((double) (currentMonthCount - previousMonthCount) / previousMonthCount) * 100;
    }

    public Page<DonHang> duyetDonHang(Pageable pageable) {
        return kdl.findAll(pageable);
    }

    public List<DonHang> dsDonHang() {
        return kdl.findAll();
    }

    public List<DonHang> dsDonhangEmail(String email) {
        return kdl.findByEmail(email);
    }

    public List<DonHang> duyet() {
        return kdl.findAll();
    }

    public DonHang timTheoId(int id) {
        DonHang dl = null;

        Optional<DonHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public DonHang xem(int id) {

        DonHang dl = null;

        Optional<DonHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public DonHang luuDonHang(DonHang dl) {
        return this.kdl.save(dl);
    }

    public void luu(DonHang dl) {
        this.kdl.save(dl);
    }

    public void them(DonHang dl) {
        this.kdl.save(dl);
    }

    public void sua(DonHang dl) {
        this.kdl.save(dl);
    }

    public void xoa(int id) {
        this.kdl.deleteById(id);
    }

}
