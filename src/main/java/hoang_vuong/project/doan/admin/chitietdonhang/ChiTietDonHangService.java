package hoang_vuong.project.doan.admin.chitietdonhang;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hoang_vuong.project.doan.admin.sanpham.SanPham;

@Service
public class ChiTietDonHangService {

    @Autowired
    private ChiTietDonHangRepository kdl;


    public List<ChiTietDonHang> getChiTietDonHangByDonHangId(int id) {
        return kdl.findByDonHangId(id);
    }
    public List<ThongKeSanPhamDTO> thongKeTop10SanPham() {
        Pageable top10 = PageRequest.of(0, 10);
        return kdl.thongKeTop10SanPhamBanChay(top10);
    }

    public List<SanPhamBanChayDTO> layTop10SanPhamBanChay() {
        Pageable topTen = PageRequest.of(0, 10); // Lấy 10 sản phẩm bán chạy nhất
        List<Object[]> result = kdl.findTop10SanPhamBySoLuongBan(topTen);

        List<SanPhamBanChayDTO> sanPhamBanChayList = new ArrayList<>();
        for (Object[] row : result) {
            SanPham sanPham = (SanPham) row[0];
            Long soLuongDaBan = (Long) row[1];
            sanPhamBanChayList.add(new SanPhamBanChayDTO(sanPham, soLuongDaBan));
        }
        return sanPhamBanChayList;
    }

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
