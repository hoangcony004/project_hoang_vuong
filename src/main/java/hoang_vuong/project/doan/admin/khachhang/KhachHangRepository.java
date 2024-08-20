package hoang_vuong.project.doan.admin.khachhang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer>
{
    
    List<KhachHang> findByTenDangNhap(String tenDangNhap);
    Boolean existsByTenDangNhap(String tenDangNhap);
    Boolean existsByEmail(String email);
    KhachHang findOneByTenDangNhap(String tenDangNhap);
    KhachHang findOneByEmail(String email);
    Boolean existsByTenDayDu(String tdd);

    List<KhachHang> findByTenDayDuContainingIgnoreCase(String tenDayDu);

    List<KhachHang> findByEmailContainingIgnoreCase(String email);

    List<KhachHang> findByDienThoaiContainingIgnoreCase(String dienThoai);
}
