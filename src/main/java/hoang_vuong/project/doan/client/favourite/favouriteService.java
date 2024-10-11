package hoang_vuong.project.doan.client.favourite;


import java.time.LocalDate;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class favouriteService {
     @Autowired private favouriteRepository kdl;
      public void luu(favourite dl)
    {
        this.kdl.save(dl);
    }

    public void xoa(int id)
    {
        this.kdl.deleteById(id);
    }
    public void addFavourite(int user, int productId) {
        favourite favourite = new favourite();
        favourite.setUser_id(user);
        favourite.setProduct_id(productId); 
        favourite.setCreatedAt(LocalDate.now()); 
        kdl.save(favourite);
    }
    public List<Integer> getProductIdsByUserId(Integer userId) {
        return kdl.findProductIdsByUserId(userId);
    }
    public void removeFavourite(Integer userId, Integer productId) {
        kdl.deleteByUserIdAndProductId(userId, productId);
    }

}
