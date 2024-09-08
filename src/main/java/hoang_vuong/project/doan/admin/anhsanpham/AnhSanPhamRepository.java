package hoang_vuong.project.doan.admin.anhsanpham;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnhSanPhamRepository extends JpaRepository<AnhSanPham, Integer>
{

    List<AnhSanPham> findByMaSP(int masp);
}