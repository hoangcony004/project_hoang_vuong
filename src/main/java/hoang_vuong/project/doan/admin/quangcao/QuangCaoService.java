package hoang_vuong.project.doan.admin.quangcao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuangCaoService {
    @Autowired
    private QuangCaoRepository kdl;

    public Page<QuangCao> duyetQuangCao(Pageable pageable) {
        return kdl.findAll(pageable);
    }

    public List<QuangCao> dsQuangCao() {
        return kdl.findAll();
    }

    public List<QuangCao> duyet() {
        return kdl.findAll();
    }

    public QuangCao timTheoId(int id) {
        QuangCao dl = null;

        Optional<QuangCao> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public QuangCao xem(int id) {

        QuangCao dl = null;

        Optional<QuangCao> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public void luu(QuangCao dl) {
        this.kdl.save(dl);
    }

    public void them(QuangCao dl) {
        this.kdl.save(dl);
    }

    public void sua(QuangCao dl) {
        this.kdl.save(dl);
    }

    public void xoa(int id) {
        this.kdl.deleteById(id);
    }

}
