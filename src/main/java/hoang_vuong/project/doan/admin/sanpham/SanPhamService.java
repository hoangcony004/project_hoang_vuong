package hoang_vuong.project.doan.admin.sanpham;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;

import hoang_vuong.project.doan.admin.nhanvien.NhanVien;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository kdl;

    public Long getTotalProducts() {
        return kdl.countTotalProducts();
    }

    public List<List<String>> getTop5SanPhamByDonGia() {
        // Tạo Pageable để lấy 5 bản ghi
        Pageable pageable = PageRequest.of(0, 5); // Trang đầu tiên, 5 sản phẩm

        // Gọi phương thức repository với Pageable
        List<Object[]> results = kdl.findTop5SanPhamByDonGiaDesc(pageable);

        List<List<String>> sanPhamList = new ArrayList<>();
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        for (Object[] result : results) {
            List<String> sanPhamData = new ArrayList<>();
            sanPhamData.add((String) result[0]); // tên sản phẩm (tenSP)
            sanPhamData.add(currencyFormat.format((Float) result[1])); // định dạng đơn giá (donGia)
            sanPhamList.add(sanPhamData);
        }
        return sanPhamList;
    }

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

 
    public Page<SanPham> duyetSanPhamTheoId(int id, Pageable pageable) {
        return kdl.findByMaNSX(id, pageable);
    }

    public List<SanPham> duyet() {
        return kdl.findAll();
    }

    public SanPham timMnsx(int id) {
        return kdl.findOneByMaNSX(id);

    }

    public List<SanPham> trangthai() {
        return kdl.findByTrangThai(true);
    }
    public List<SanPham> timMaNhnuat(int maNSX) {
        return kdl.findByMaNSX(maNSX);
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
        return getSanPhamByCondition(kdl::findByNoiBat);
    }
    
    public List<SanPham> dsSanPhamBanChay() {
        return getSanPhamByCondition(kdl::findByBanChay);
    }
    public List<SanPham> dsSanPham() {
        return  getSanPhamByConditionall(this::duyet);
    }
    
    private List<SanPham> getSanPhamByCondition(Function<Boolean, List<SanPham>> fetchFunction) {
        List<SanPham> sanPhams = trangthai();  // Lọc theo trạng thái
        return sanPhams.stream()               // Lọc theo điều kiện nổi bật hoặc bán chạy
                .filter(sp -> fetchFunction.apply(true).contains(sp))
                .limit(16)       
                .collect(Collectors.toList());
    }
    private List<SanPham> getSanPhamByConditionall(Supplier<List<SanPham>> fetchFunction) {
        List<SanPham> sanPhams = trangthai();  // Lọc theo trạng thái
        return sanPhams.stream()               // Lọc theo điều kiện (nổi bật, bán chạy, v.v.)
                .filter(sp -> fetchFunction.get().contains(sp)) // Sử dụng get() của Supplier
                .limit(16)       
                .collect(Collectors.toList());
    }
}
