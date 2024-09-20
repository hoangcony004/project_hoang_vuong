package hoang_vuong.project.doan.admin.donhang;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
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

    private int maKH;
    @ManyToOne
    @JoinColumn(name = "maKH", insertable = false, updatable = false)
    private KhachHang khachHang;
    private String email;
    private String dienThoai;
    private String tenDayDu;
    private String diaChi;
    private String ghiChu;
    private float tongTien;
    private int trangThai;
    private LocalDate ngayTao;
    private String thanhtoan;
    public String getNgayTaoText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }
    public String getFomatTongtien() {
        if (tongTien == 0) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(tongTien) + " vnd";
    }
}
