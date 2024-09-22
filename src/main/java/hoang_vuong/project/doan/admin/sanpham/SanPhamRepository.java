package hoang_vuong.project.doan.admin.sanpham;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

<<<<<<< HEAD

=======
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
      Page<SanPham> findByMaNSX(int maNSX, Pageable pageable);

      List<SanPham> findByTrangThai(Boolean trangThai);
>>>>>>> 8d87d435b83f27f926b838331e9ec5853058b106

      List<SanPham> findByMaNSX(int maNSX);

<<<<<<< HEAD
public interface SanPhamRepository extends JpaRepository<SanPham, Integer>
{
   Page<SanPham> findByMaNSX(int maNSX, Pageable pageable);

   List<SanPham> findByTrangThai(Boolean trangThai);

   List<SanPham> findByMaNSX(int maNSX);


   SanPham findOneByMaNSX(int maNSX);
=======
      SanPham findOneByMaNSX(int maNSX);
>>>>>>> 8d87d435b83f27f926b838331e9ec5853058b106

      List<SanPham> findByNoiBat(Boolean noiBat);

      List<SanPham> findByBanChay(Boolean banChay);

      List<SanPham> findByTenSPContaining(String tenSP);

      @Query("SELECT sp FROM SanPham sp WHERE "
                  + "(:minPrice IS NULL OR sp.donGia >= :minPrice) AND "
                  + "(:maxPrice IS NULL OR sp.donGia <= :maxPrice) AND "
                  + "(:maNSX IS NULL OR sp.maNSX = :maNSX) AND "
                  + "(:banChay IS NULL OR sp.banChay = :banChay) AND "
                  + "(:noiBat IS NULL OR sp.noiBat = :noiBat)")
      Page<SanPham> locSanPham(
                  @Param("minPrice") Float minPrice,
                  @Param("maxPrice") Float maxPrice,
                  @Param("maNSX") Integer maNSX,
                  @Param("banChay") Boolean banChay,
                  @Param("noiBat") Boolean noiBat,
                  Pageable pageable);

      @Query("SELECT sp.nhaSanXuat.ten, COUNT(sp) FROM SanPham sp GROUP BY sp.nhaSanXuat.ten")
      List<Object[]> thongKeSanPhamTheoNhaSanXuat();

      @Query("SELECT COUNT(s) FROM SanPham s WHERE s.trangThai = :trangThai")
      long countByTrangThai(@Param("trangThai") boolean trangThai);

      @Query("SELECT s.tenSP AS tenSP, s.donGia AS donGia FROM SanPham s ORDER BY s.donGia DESC")
      List<Object[]> findTop5SanPhamByDonGiaDesc(Pageable pageable);

      @Query("SELECT COUNT(o) FROM SanPham o")
      Long countTotalProducts();
}
