package hoang_vuong.project.doan.client.danhgiasanpham;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DanhGiaSanPhamService 
{
    @Autowired 
    private DanhGiaSanPhamRepository kdl;

    public List<DanhGiaSanPham> getDanhGiaBySanPham(int idSanPham) {
        return kdl.findBySanPhamId(idSanPham);
    }

    public List<DanhGiaSanPham> dsDanhGiaSanPham()
    {
        return kdl.findAll();
    }

    public List<DanhGiaSanPham>  duyet() 
    {
        return kdl.findAll();
    }

    public DanhGiaSanPham  timTheoId(int id)
    {
        DanhGiaSanPham dl = null;

        Optional<DanhGiaSanPham> optional = kdl.findById(id);

        if
        (optional.isPresent()) 
        {
            dl = optional.get();
        } else
        {
            
        }

        return dl;

    }

    public DanhGiaSanPham xem(int id) 
    {

        DanhGiaSanPham dl = null;

        Optional<DanhGiaSanPham> optional = kdl.findById(id);

        if
        (optional.isPresent())
        {
            dl = optional.get();
        } else
        {

        }

        return dl;

    }

    public void luu(DanhGiaSanPham dl)
    {
        this.kdl.save(dl);
    }

    public void them(DanhGiaSanPham dl)
    {
        this.kdl.save(dl);
    }

    public void sua(DanhGiaSanPham dl)
    {
        this.kdl.save(dl);
    }

    public void xoa(int id)
    {
        this.kdl.deleteById(id);
    }

}


