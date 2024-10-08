package hoang_vuong.project.doan.client.danhgiasanpham;

import java.time.LocalDate;

import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DanhGiaSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id", referencedColumnName = "id", nullable = false)
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "san_pham_id", referencedColumnName = "id", nullable = false)
    private SanPham sanPham;

    private int diemDanhGia;

    private String nhanXet;

    private LocalDate ngayDanhGia;


}
