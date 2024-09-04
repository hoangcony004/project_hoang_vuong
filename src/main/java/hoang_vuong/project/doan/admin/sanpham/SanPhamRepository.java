package hoang_vuong.project.doan.admin.sanpham;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SanPhamRepository extends JpaRepository<SanPham, Integer>
{
   List<SanPham> findByMaNSX(int maNSX);
   SanPham findOneByMaNSX(int maNSX);
   List<SanPham> findByNoiBat(Boolean noiBat);
   List<SanPham> findByBanChay(Boolean banChay);
}
