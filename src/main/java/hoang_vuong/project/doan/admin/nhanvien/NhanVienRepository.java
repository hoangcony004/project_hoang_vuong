package hoang_vuong.project.doan.admin.nhanvien;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer>
{
    List<NhanVien> findByTenDangNhap(String tenDangNhap);
    Boolean existsByTenDangNhap(String tenDangNhap);
    NhanVien findOneByTenDangNhap(String tenDangNhap);
    NhanVien findOneByEmail(String email);
    List<NhanVien> findByEmail(String email);
    Boolean existsByTenDayDu(String tdd);

    List<NhanVien> findByTenDayDuContainingIgnoreCase(String tenDayDu);

    List<NhanVien> findByEmailContainingIgnoreCase(String email);

    List<NhanVien> findByDienThoaiContainingIgnoreCase(String dienThoai);
}