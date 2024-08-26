package hoang_vuong.project.doan.admin.sanpham;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SanPhamService 
{
    @Autowired private SanPhamRepository kdl;
    public Page<SanPham> duyetSanPham(Pageable pageable) {
        return kdl.findAll(pageable);
    }
    public List<SanPham> dsSanPham() 
    {
        return kdl.findAll();
    }

    public List<SanPham>  duyet() 
    {
        return kdl.findAll();
    }

    public SanPham  timTheoId(int id)
    {
        SanPham dl = null;

        Optional<SanPham> optional = kdl.findById(id);

        if
        (optional.isPresent())
        {
            dl = optional.get();
        } else
        {

        }

        return dl;

    }

    public SanPham xem(int id)
    {

        SanPham dl = null;

        Optional<SanPham> optional = kdl.findById(id);

        if
        (optional.isPresent())
        {
            dl = optional.get();
        } else
        {

        }

        return dl;

    }

    public void luu(SanPham dl)
    {
        this.kdl.save(dl);
    }

    public void them(SanPham dl)
    {
        this.kdl.save(dl);
    }

    public void sua(SanPham dl)
    {
        this.kdl.save(dl);
    }

    public void xoa(int id)
    {
        this.kdl.deleteById(id);
    }


}


