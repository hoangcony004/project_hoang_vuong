package hoang_vuong.project.doan.admin.chitietdonhang;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import hoang_vuong.project.doan.admin.donhang.DonHang;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChiTietDonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // @Column(unique = true)
    // private String MaDH;
    // Liên kết với bảng SanPham
    private Integer maSP;
    @ManyToOne
    @JoinColumn(name = "maSP", insertable = false, updatable = false)
    private SanPham sanPham;

    // Liên kết với bảng DonHang
    @ManyToOne
    @JoinColumn(name = "don_hang_id", nullable = false)
    private DonHang donHang;

    private int soLuong;
    private Float donGia;
    private Float tongTien;
    private String ten;
    private String model;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
    public Integer getDonHangId() {
        return donHang != null ? donHang.getId() : null; // Assuming DonHang has getId()
    }
    
    // Setter tùy chỉnh cho don_hang_id
    public void setDonHangId(Integer donHangId) {
        if (donHang == null) {
            donHang = new DonHang();
        }
        donHang.setId(donHangId);
    }
    public String getNgayTaoText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgaySuaText() {
        if (ngaySua == null) {
            return "Chưa cập nhật";
        }
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngaySua);
    }

}
