package hoang_vuong.project.doan.admin.sanpham;


import hoang_vuong.project.doan.admin.anhsanpham.AnhSanPham;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Setter
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String model;
    
    private String anhDaiDien;
    private String tenSP;
    private Float donGia;
    private Boolean trangThai;

    private Integer maNSX;
    @ManyToOne
    @JoinColumn(name = "maNSX", insertable = false, updatable = false)
    private NhaSanXuat nhaSanXuat;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL)
    private List<AnhSanPham> anhSanPham;

    @Column(columnDefinition = "LONGTEXT")
    private String moTa;
    private Boolean banChay;
    private Boolean noiBat;
    private String tag;
    private int soLuong;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
private LocalDate ngayHetHan;

    public String getNgayTaoText() {
        if (ngayTao == null) {
            return "Chưa cập nhật";
        }
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getFomatGiaBan() {
        if (donGia == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(donGia) + " vn₫";
    }

    public String getNgaySuaText() {
        if (ngaySua == null) {
            return "Chưa cập nhật";
        }
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngaySua);
    }
    public String getNgayHetHan() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy, MM, dd");
        // Định dạng chuỗi theo yêu cầu: yyyy, MM, dd
        if (ngayHetHan == null) {
        return ngayTao.format(formatter);
        }
        return ngayHetHan.format(formatter);
    }

}
