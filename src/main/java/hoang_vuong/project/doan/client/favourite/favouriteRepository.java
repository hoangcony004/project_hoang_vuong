package hoang_vuong.project.doan.client.favourite;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface favouriteRepository extends JpaRepository<favourite,Integer> {
    List<favourite> findByUser_Id(Integer userId);
}
