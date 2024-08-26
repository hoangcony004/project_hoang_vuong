package hoang_vuong.project.doan.admin.sanpham;

import org.springframework.format.annotation.DateTimeFormat;

import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String model;
    private String tenSP;
    private String anhDaiDien;
    private Float donGia;
    private Boolean trangThai;
    private String moTa;
    private int thuTu;
    private Boolean banChay;
    private Boolean noiBat;
    private String tag;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
    @ManyToOne
    @JoinColumn(name = "maNSX")
    private NhaSanXuat nhaSanXuat;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayHetHan;

    public String getNgayTaoText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgaySuaText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngaySua);
    }

    public String getNgayHetHanText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayHetHan);
    }

}
