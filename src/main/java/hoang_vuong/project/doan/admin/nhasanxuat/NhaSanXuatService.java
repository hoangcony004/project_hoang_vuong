package hoang_vuong.project.doan.admin.nhasanxuat;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NhaSanXuatService 
{
    @Autowired private NhaSanXuatRepository kdl;

    public List<NhaSanXuat> dsNhaSanXuat()
    {
        return kdl.findAll();
    }

    public List<NhaSanXuat>  duyet() 
    {
        return kdl.findAll();
    }

    public NhaSanXuat  timTheoId(int id)
    {
        NhaSanXuat dl = null;

        Optional<NhaSanXuat> optional = kdl.findById(id);

        if
        (optional.isPresent())
        {
            dl = optional.get();
        } else
        {
            
        }

        return dl;

    }

    public NhaSanXuat xem(int id)
    {

        NhaSanXuat dl = null;

        Optional<NhaSanXuat> optional = kdl.findById(id);

        if
        (optional.isPresent())
        {
            dl = optional.get();
        } else
        {
            
        }

        return dl;

    }

    public void luu(NhaSanXuat dl)
    {
        this.kdl.save(dl);
    }

    public void them(NhaSanXuat dl)
    {
        this.kdl.save(dl);
    }

    public void sua(NhaSanXuat dl)
    {
        this.kdl.save(dl);
    }

    public void xoa(int id)
    {
        this.kdl.deleteById(id);
    }

}


