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
    private String MaDH;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String dienThoai;
    
    private String tenDayDu;
    private String ghiChu;
    private float tongTien;
    private int trangThai;
    private LocalDate ngayTao;

    private String tinhThanh;
    private String quanHuyen;
    private String xaPhuong;
    private String diaChi;

    public String getNgayTaoText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

}
