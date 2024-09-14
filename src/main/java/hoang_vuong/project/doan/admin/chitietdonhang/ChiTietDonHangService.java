package hoang_vuong.project.doan.admin.chitietdonhang;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ChiTietDonHangService {
    @Autowired
    private ChiTietDonHangRepository kdl;

    public Page<ChiTietDonHang> duyetDonHang(Pageable pageable) {
        return kdl.findAll(pageable);
    }

    public List<ChiTietDonHang> dsChiTietDonHang() {
        return kdl.findAll();
    }

    public List<ChiTietDonHang> duyet() {
        return kdl.findAll();
    }

    public ChiTietDonHang timTheoId(int id) {
        ChiTietDonHang dl = null;

        Optional<ChiTietDonHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public ChiTietDonHang xem(int id) {

        ChiTietDonHang dl = null;

        Optional<ChiTietDonHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public void luu(ChiTietDonHang dl) {
        this.kdl.save(dl);
    }

    public void them(ChiTietDonHang dl) {
        this.kdl.save(dl);
    }

    public void sua(ChiTietDonHang dl) {
        this.kdl.save(dl);
    }

    public void xoa(int id) {
        this.kdl.deleteById(id);
    }

}
