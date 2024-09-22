package hoang_vuong.project.doan.admin.donhang;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hoang_vuong.project.doan.admin.donhang.DoanhThuThang;

public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

<<<<<<< HEAD
        @Query("SELECT new hoang_vuong.project.doan.admin.donhang.DoanhThuThang(SUM(d.tongTien), DAY(d.ngayTao), MONTH(d.ngayTao), YEAR(d.ngayTao)) "
                        +
                        "FROM DonHang d WHERE YEAR(d.ngayTao) = :year AND MONTH(d.ngayTao) = :month " +
                        "GROUP BY DAY(d.ngayTao), MONTH(d.ngayTao), YEAR(d.ngayTao)")
        List<DoanhThuThang> findDoanhThuTheoThang(@Param("year") int year, @Param("month") int month);

        @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE YEAR(d.ngayTao) = :year")
        Float findTotalRevenueByYear(@Param("year") int year);
        List<DonHang> findByEmail(String email);
        boolean existsByMaDH(String maDH);

        @Query("SELECT EXTRACT(YEAR FROM d.ngayTao) AS year, SUM(d.tongTien) AS totalRevenue " +
                        "FROM DonHang d GROUP BY EXTRACT(YEAR FROM d.ngayTao)")
        List<Object[]> getRevenueByYear();

        @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayTao BETWEEN :startDate AND :endDate")
        long countByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(d.tongTien) FROM DonHang d")
        Float getTongTien();

        // @Query("SELECT EXTRACT(MONTH FROM d.ngayTao) AS month, SUM(d.tongTien) AS
        // total FROM DonHang d GROUP BY EXTRACT(MONTH FROM d.ngayTao) ORDER BY month")
        // List<Object[]> getDoanhThuTheoThang();
        @Query("SELECT EXTRACT(MONTH FROM d.ngayTao) AS month, SUM(d.tongTien) AS total " +
                        "FROM DonHang d " +
                        "WHERE EXTRACT(YEAR FROM d.ngayTao) = :year " +
                        "GROUP BY EXTRACT(MONTH FROM d.ngayTao) " +
                        "ORDER BY month")
        List<Object[]> getDoanhThuTheoThangTheoNam(@Param("year") int year);

        List<DonHang> findAllByNgayTaoBetween(LocalDate startDate, LocalDate endDate);

        @Query("SELECT d FROM DonHang d WHERE d.ngayTao BETWEEN :startDate AND :endDate")
        List<DonHang> findAllByWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT COALESCE(SUM(d.tongTien), 0) FROM DonHang d WHERE d.ngayTao BETWEEN :startDate AND :endDate")
        double calculateTotalRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

=======
        @Query("SELECT MONTH(d.ngayTao) AS month, COUNT(d.id) AS count " +
                        "FROM DonHang d " +
                        "WHERE YEAR(d.ngayTao) = :year " +
                        "GROUP BY MONTH(d.ngayTao) " +
                        "ORDER BY MONTH(d.ngayTao)")
        List<Object[]> countDonHangByMonth(@Param("year") int year);

        @Query("SELECT YEAR(d.ngayTao), COUNT(d) FROM DonHang d GROUP BY YEAR(d.ngayTao)")
        List<Object[]> countOrdersByYear();

        @Query("SELECT new hoang_vuong.project.doan.admin.donhang.DoanhThuThang(SUM(d.tongTien), DAY(d.ngayTao), MONTH(d.ngayTao), YEAR(d.ngayTao)) "
                        +
                        "FROM DonHang d WHERE YEAR(d.ngayTao) = :year AND MONTH(d.ngayTao) = :month " +
                        "GROUP BY DAY(d.ngayTao), MONTH(d.ngayTao), YEAR(d.ngayTao)")
        List<DoanhThuThang> findDoanhThuTheoThang(@Param("year") int year, @Param("month") int month);

        @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE YEAR(d.ngayTao) = :year")
        Float findTotalRevenueByYear(@Param("year") int year);

        List<DonHang> findByEmail(String email);

        boolean existsByMaDH(String maDH);

        @Query("SELECT EXTRACT(YEAR FROM d.ngayTao) AS year, SUM(d.tongTien) AS totalRevenue " +
                        "FROM DonHang d GROUP BY EXTRACT(YEAR FROM d.ngayTao)")
        List<Object[]> getRevenueByYear();

        @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayTao BETWEEN :startDate AND :endDate")
        long countByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(d.tongTien) FROM DonHang d")
        Float getTongTien();

        @Query("SELECT EXTRACT(MONTH FROM d.ngayTao) AS month, SUM(d.tongTien) AS total " +
                        "FROM DonHang d " +
                        "WHERE EXTRACT(YEAR FROM d.ngayTao) = :year " +
                        "GROUP BY EXTRACT(MONTH FROM d.ngayTao) " +
                        "ORDER BY month")
        List<Object[]> getDoanhThuTheoThangTheoNam(@Param("year") int year);

        List<DonHang> findAllByNgayTaoBetween(LocalDate startDate, LocalDate endDate);

        @Query("SELECT d FROM DonHang d WHERE d.ngayTao BETWEEN :startDate AND :endDate")
        List<DonHang> findAllByWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT COALESCE(SUM(d.tongTien), 0) FROM DonHang d WHERE d.ngayTao BETWEEN :startDate AND :endDate")
        double calculateTotalRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT COUNT(o) FROM DonHang o")
        Long countTotalOrders();

        @Query("SELECT o FROM DonHang o WHERE o.maDH LIKE %:query%")
        List<DonHang> searchOrdersByMaDH(@Param("query") String query);
>>>>>>> 8d87d435b83f27f926b838331e9ec5853058b106
}
