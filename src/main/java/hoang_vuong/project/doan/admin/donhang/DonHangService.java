package hoang_vuong.project.doan.admin.donhang;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DonHangService {
    @Autowired
    private DonHangRepository kdl;

    public List<Double> getDoanhThuTheoThang() {
        List<Double> doanhThuTheoThang = new ArrayList<>();
        List<Object[]> results = kdl.getDoanhThuTheoThang();

        // Khởi tạo danh sách với giá trị mặc định 0.0 cho từng tháng
        for (int i = 1; i <= 12; i++) {
            doanhThuTheoThang.add(0.0);
        }

        for (Object[] result : results) {
            int month = ((Number) result[0]).intValue();
            double total = ((Number) result[1]).doubleValue();
            doanhThuTheoThang.set(month - 1, total);
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
