package hoang_vuong.project.doan.client.favourite;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface favouriteRepository extends JpaRepository<favourite,Integer> {
     @Query("SELECT f.product_id FROM favourite f WHERE f.user_id = :userId")
    List<Integer> findProductIdsByUserId(@Param("userId") Integer userId);
    @Modifying
    @Transactional //   Đánh dấu phương thức này là một hành động thay đổi dữ liệu
    @Query("DELETE FROM favourite f WHERE f.user_id = ?1 AND f.product_id = ?2")
    void deleteByUserIdAndProductId(Integer userId, Integer productId);
}
