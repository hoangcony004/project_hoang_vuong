package hoang_vuong.project.doan.admin.donhang;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonHangRepository extends JpaRepository<DonHang, Integer>
{
     List<DonHang> findByEmail(String email);
}
