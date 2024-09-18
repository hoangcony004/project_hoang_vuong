package hoang_vuong.project.doan.admin.donhang;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.text.DecimalFormat;

import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer maKH;
    @ManyToOne
    @JoinColumn(name = "maKH", insertable = false, updatable = false)
    private KhachHang khachHang;

    @Column(unique = true)
    private String maDH;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String dienThoai;

    private String tenDayDu;
    private String ghiChu;

    private int trangThai;
    private Float tongTien;
    private LocalDate ngayTao;

    private String tinhThanh;
    private String quanHuyen;
    private String xaPhuong;
    private String diaChi;

    public String getNgayTaoText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getFomatTongTien() {
        if (tongTien == null) {
            return "Đang để trống!";
        }
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(tongTien) + " vn₫";
    }

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietDonHang> chiTietDonHangList; // Danh sách chi tiết đơn hàng

    // Getter và Setter
    public List<ChiTietDonHang> getChiTietDonHangList() {
        return chiTietDonHangList;
    }

    public void setChiTietDonHangList(List<ChiTietDonHang> chiTietDonHangList) {
        this.chiTietDonHangList = chiTietDonHangList;
    }

}
