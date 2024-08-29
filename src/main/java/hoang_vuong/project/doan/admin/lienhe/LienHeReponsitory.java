package hoang_vuong.project.doan.admin.lienhe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LienHeReponsitory extends JpaRepository<LienHe, Integer>
{
    List<LienHe> findByTen(String ten);
}