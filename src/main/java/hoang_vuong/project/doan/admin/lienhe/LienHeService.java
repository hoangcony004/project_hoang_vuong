package hoang_vuong.project.doan.admin.lienhe;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LienHeService {
    @Autowired
    private LienHeRepository kdl;

    public Page<LienHe> duyetLienHe(Pageable pageable) {
        return kdl.findAll(pageable);
    }

    public List<LienHe> dsLienHe() {
        return kdl.findAll();
    }

    public List<LienHe> duyet() {
        return kdl.findAll();
    }

    public LienHe timTheoId(int id) {
        LienHe dl = null;

        Optional<LienHe> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public LienHe xem(int id) {

        LienHe dl = null;

        Optional<LienHe> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public void luu(LienHe dl) {
        this.kdl.save(dl);
    }

    public void them(LienHe dl) {
        this.kdl.save(dl);
    }

    public void sua(LienHe dl) {
        this.kdl.save(dl);
    }

    public void xoa(int id) {
        this.kdl.deleteById(id);
    }

}
