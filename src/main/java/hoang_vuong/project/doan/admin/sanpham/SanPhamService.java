package hoang_vuong.project.doan.admin.sanpham;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hoang_vuong.project.doan.admin.nhanvien.NhanVien;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository kdl;

    // public Page<SanPham> locSanPham(Integer minPrice, Integer maxPrice, Integer maNSX, Boolean banChay, Boolean noiBat, int page, int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     return kdl.locSanPham(minPrice, maxPrice, maNSX, banChay, noiBat, pageable);
    // }
    public Page<SanPham> locSanPham(Float minPrice, Float maxPrice, Integer maNSX, Boolean banChay, Boolean noiBat, Pageable pageable) {
        return kdl.locSanPham(minPrice, maxPrice, maNSX, banChay, noiBat, pageable);
    }

    public Page<SanPham> duyetSanPham(Pageable pageable) {
        return kdl.findAll(pageable);
    }

    public List<SanPham> dsSanPham() {
        return kdl.findAll();
    }

    public List<SanPham> duyet() {
        return kdl.findAll();
    }
    public SanPham timMnsx(int id) {
        return kdl.findOneByMaNSX(id);

    }
    public SanPham timTheoId(int id) {
        SanPham dl = null;

        Optional<SanPham> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }
    public List<SanPham> timMaNSX(int maNSX) {
        return kdl.findByMaNSX(maNSX);
    }
    public SanPham xem(int id) {

        SanPham dl = null;

        Optional<SanPham> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public void luu(SanPham dl) {
        this.kdl.save(dl);
    }

    public void them(SanPham dl) {
        this.kdl.save(dl);
    }

    public void sua(SanPham dl) {
        this.kdl.save(dl);
    }

    public void xoa(int id) {
        this.kdl.deleteById(id);
    }
    public List<SanPham> dsSanPhamNoiBat() 
    {
        return kdl.findByNoiBat(true);
    }
    public List<SanPham> dsSanPhamBanChay() 
    {
        return kdl.findByBanChay(true);
    }

}
