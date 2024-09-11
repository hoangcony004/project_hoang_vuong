package hoang_vuong.project.doan.admin.sanpham;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SanPhamRepository extends JpaRepository<SanPham, Integer>
{
   List<SanPham> findByMaNSX(int maNSX);
   SanPham findOneByMaNSX(int maNSX);
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


}
