package hoang_vuong.project.doan.admin.sanpham;

import java.util.List;
import java.util.Optional;

// import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository kdl;

    public Map<String, Long> thongKeSanPhamTheoTrangThai() {
        Map<String, Long> thongKe = new HashMap<>();
        long soLuongTrue = kdl.countByTrangThai(true);
        long soLuongFalse = kdl.countByTrangThai(false);
        thongKe.put("Đang Bán", soLuongTrue);
        thongKe.put("Ngừng Bán", soLuongFalse);
        return thongKe;
    }

    public Map<String, Long> thongKeSanPhamTheoNhaSanXuat() {
        List<Object[]> results = kdl.thongKeSanPhamTheoNhaSanXuat();
        Map<String, Long> thongKe = new HashMap<>();

        for (Object[] result : results) {
            String nhaSanXuat = (String) result[0];
            Long soLuong = (Long) result[1];
            thongKe.put(nhaSanXuat, soLuong);
        }

        return thongKe;
    }

    public Page<SanPham> locSanPham(Float minPrice, Float maxPrice, Integer maNSX, Boolean banChay, Boolean noiBat,
            Pageable pageable) {
        return kdl.locSanPham(minPrice, maxPrice, maNSX, banChay, noiBat, pageable);
    }

    public Page<SanPham> duyetSanPham(Pageable pageable) {
        return kdl.findAll(pageable);
    }

    public List<SanPham> timSanPhamTheoTen(String tenSanPham) {
        return kdl.findByTenSPContaining(tenSanPham);
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

    public List<SanPham> dsSanPhamNoiBat() {
        return kdl.findByNoiBat(true);
    }

    public List<SanPham> dsSanPhamBanChay() {
        return kdl.findByBanChay(true);
    }

}
