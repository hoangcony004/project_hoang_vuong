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

    // kiểm tra số điện thaoij và email
    public boolean isEmailExists(String email) {
        return kdl.existsByEmail(email);
    }

    public boolean isDienThoaiExists(String dienThoai) {
        return kdl.existsByDienThoai(dienThoai);
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

    public Map<String, Double> getWeeklyRevenue() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        LocalDate startOfLastWeek = startOfWeek.minusWeeks(1);
        LocalDate endOfLastWeek = endOfWeek.minusWeeks(1);

        double revenueThisWeek = kdl.calculateTotalRevenue(startOfWeek, endOfWeek);
        double revenueLastWeek = kdl.calculateTotalRevenue(startOfLastWeek, endOfLastWeek);

        return Map.of(
                "thisWeek", revenueThisWeek,
                "lastWeek", revenueLastWeek);
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
