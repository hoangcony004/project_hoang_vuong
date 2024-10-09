package hoang_vuong.project.doan.client.favourite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import hoang_vuong.project.doan.client.danhgiasanpham.DanhGiaSanPhamRepository;

@Service
public class favouriteService {
     @Autowired private favouriteRepository kdl;
      public void luu(favourite dl)
    {
        this.kdl.save(dl);
    }
    public List<favourite> getFavouritesByUserId(Integer userId) {
        return kdl.findByUser_Id(userId);
    }
    public void xoa(int id)
    {
        this.kdl.deleteById(id);
    }
}
