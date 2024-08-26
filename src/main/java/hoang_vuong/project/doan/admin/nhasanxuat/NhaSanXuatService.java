package hoang_vuong.project.doan.admin.nhasanxuat;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NhaSanXuatService {

    @Autowired
    private NhaSanXuatRepository kdl;

    public Page<NhaSanXuat> duyetNhaSanXuat(Pageable pageable) {
        return kdl.findAll(pageable);
    }

    public List<NhaSanXuat> dsNhaSanXuat() {
        return kdl.findAll();
    }

    public List<NhaSanXuat> duyet() {
        return kdl.findAll();
    }

    public NhaSanXuat timTheoId(int id) {
        Optional<NhaSanXuat> optional = kdl.findById(id);
        return optional.orElse(null); // Trả về null nếu không tìm thấy
    }

    public NhaSanXuat xem(int id) {
        Optional<NhaSanXuat> optional = kdl.findById(id);
        return optional.orElse(null); // Trả về null nếu không tìm thấy
    }

    public void luu(NhaSanXuat dl) {
        kdl.save(dl);
    }

    public void them(NhaSanXuat dl) {
        kdl.save(dl);
    }

    public void sua(NhaSanXuat dl) {
        kdl.save(dl);
    }

    public void xoa(int id) {
        kdl.deleteById(id);
    }
}
