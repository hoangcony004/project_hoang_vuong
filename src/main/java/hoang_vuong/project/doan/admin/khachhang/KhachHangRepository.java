package hoang_vuong.project.doan.admin.khachhang;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hoang_vuong.project.doan.admin.nhanvien.NhanVien;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    long countByNgayTaoBetween(LocalDate startDate, LocalDate endDate);
 List<KhachHang> findByEmail(String email);
    List<KhachHang> findByTenDangNhap(String tenDangNhap);

    Boolean existsByTenDangNhap(String tenDangNhap);

    Boolean existsByEmail(String email);

    KhachHang findOneByTenDangNhap(String tenDangNhap);

    KhachHang findOneByEmail(String email);

    KhachHang findOneByDienThoai(String dienThoai);

    Boolean existsByTenDayDu(String tdd);

    Boolean existsByDienThoai(String dienThoai);

    List<KhachHang> findByTenDayDuContainingIgnoreCase(String tenDayDu);

    List<KhachHang> findByEmailContainingIgnoreCase(String email);

    List<KhachHang> findByDienThoaiContainingIgnoreCase(String dienThoai);

    @Query("SELECT COUNT(o) FROM KhachHang o")
    Long countTotalUsers();
}
