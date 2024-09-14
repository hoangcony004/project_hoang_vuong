package hoang_vuong.project.doan.admin.chitietdonhang;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {
    // List<ChiTietDonHang> findByMaDH(String MaDH);
}
