package hoang_vuong.project.doan.admin.anhsanpham;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class AnhSanPhamService {
    @Autowired
    private AnhSanPhamRepository kdl;

    public Page<AnhSanPham> duyetAnhSanPham(Pageable pageable) {
        return kdl.findAll(pageable);
    }

    public List<AnhSanPham> dsAnhSanPham() {
        return kdl.findAll();
    }

    public List<AnhSanPham> duyet() {
        return kdl.findAll();
    }

    public AnhSanPham timTheoId(int id) {
        AnhSanPham dl = null;

        Optional<AnhSanPham> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public AnhSanPham xem(int id) {

        AnhSanPham dl = null;

        Optional<AnhSanPham> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public void luu(AnhSanPham dl) {
        this.kdl.save(dl);
    }

    public void them(AnhSanPham dl) {
        this.kdl.save(dl);
    }

    public void sua(AnhSanPham dl) {
        this.kdl.save(dl);
    }

    public void xoa(int id) {
        this.kdl.deleteById(id);
    }

}
