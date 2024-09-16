package hoang_vuong.project.doan.admin.donhang;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayTao BETWEEN :startDate AND :endDate")
    long countByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(d.tongTien) FROM DonHang d")
    Float getTongTien();

    @Query("SELECT EXTRACT(MONTH FROM d.ngayTao) AS month, SUM(d.tongTien) AS total FROM DonHang d GROUP BY EXTRACT(MONTH FROM d.ngayTao) ORDER BY month")
    List<Object[]> getDoanhThuTheoThang();
}
