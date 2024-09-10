package hoang_vuong.project.doan.admin.quangcao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuangCaoRepository extends JpaRepository<QuangCao, Integer>
{
    List<QuangCao> findByTrangThai(Boolean trangThai);

}