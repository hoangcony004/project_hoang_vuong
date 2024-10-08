package hoang_vuong.project.doan.admin.chitietdonhang;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {



        @Query("SELECT c FROM ChiTietDonHang c WHERE c.donHang.id = :id")
        List<ChiTietDonHang> findByDonHangId(@Param("id") int id);

        @Query("SELECT ctdh.sanPham, SUM(ctdh.soLuong) AS totalQuantity " +
                        "FROM ChiTietDonHang ctdh " +
                        "GROUP BY ctdh.sanPham " +
                        "ORDER BY totalQuantity DESC")
        List<Object[]> findTop10SanPhamBySoLuongBan(Pageable pageable);

        @Query("SELECT new hoang_vuong.project.doan.admin.chitietdonhang.ThongKeSanPhamDTO(sp.tenSP, sp.donGia, SUM(ctdh.soLuong), (sp.donGia * SUM(ctdh.soLuong))) "
                        +
                        "FROM ChiTietDonHang ctdh " +
                        "JOIN ctdh.sanPham sp " +
                        "GROUP BY sp.tenSP, sp.donGia " +
                        "ORDER BY SUM(ctdh.soLuong) DESC")
        List<ThongKeSanPhamDTO> thongKeTop10SanPhamBanChay(Pageable pageable);


}
