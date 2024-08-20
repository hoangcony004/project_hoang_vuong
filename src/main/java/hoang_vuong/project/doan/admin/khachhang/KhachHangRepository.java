package hoang_vuong.project.doan.admin.khachhang;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer>
{

    long countByNgayTaoBetween(LocalDate startDate, LocalDate endDate);
    List<KhachHang> findByTenDangNhap(String tenDangNhap);
    Boolean existsByTenDangNhap(String tenDangNhap);
    KhachHang findOneByTenDangNhap(String tenDangNhap);
    Boolean existsByTenDayDu(String tdd);

    List<KhachHang> findByTenDayDuContainingIgnoreCase(String tenDayDu);

    List<KhachHang> findByEmailContainingIgnoreCase(String email);

    List<KhachHang> findByDienThoaiContainingIgnoreCase(String dienThoai);
}
