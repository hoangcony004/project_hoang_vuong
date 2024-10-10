package hoang_vuong.project.doan.client.danhgiasanpham;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DanhGiaSanPhamRepository extends JpaRepository<DanhGiaSanPham, Integer>
{
    List<DanhGiaSanPham> findBySanPhamId(int san_pham_id);
}
