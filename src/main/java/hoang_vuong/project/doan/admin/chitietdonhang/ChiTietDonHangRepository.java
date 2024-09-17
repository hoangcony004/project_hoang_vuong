package hoang_vuong.project.doan.admin.chitietdonhang;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {
    // List<ChiTietDonHang> findByMaDH(String MaDH);
    @Query("SELECT ctdh.sanPham, SUM(ctdh.soLuong) AS totalQuantity " +
            "FROM ChiTietDonHang ctdh " +
            "GROUP BY ctdh.sanPham " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findTop10SanPhamBySoLuongBan(Pageable pageable);

}
