package hoang_vuong.project.doan.admin.donhang;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DonHangService {
    @Autowired
    private DonHangRepository kdl;

    public Page<DonHang> duyetDonHang(Pageable pageable) {
        return kdl.findAll(pageable);
    }

    public List<DonHang> dsDonHang() {
        return kdl.findAll();
    }
    public List<DonHang> dsDonhangEmail(String email) {
        return kdl.findByEmail(email);
    }

    public List<DonHang> duyet() {
        return kdl.findAll();
    }

    public DonHang timTheoId(int id) {
        DonHang dl = null;

        Optional<DonHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public DonHang xem(int id) {

        DonHang dl = null;

        Optional<DonHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }
    public DonHang luuDonHang(DonHang dl) {
       return this.kdl.save(dl);
    }
    public void luu(DonHang dl) {
        this.kdl.save(dl);
    }

    public void them(DonHang dl) {
        this.kdl.save(dl);
    }

    public void sua(DonHang dl) {
        this.kdl.save(dl);
    }

    public void xoa(int id) {
        this.kdl.deleteById(id);
    }

}
