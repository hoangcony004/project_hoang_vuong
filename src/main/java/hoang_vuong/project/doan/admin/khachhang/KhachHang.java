package hoang_vuong.project.doan.admin.khachhang;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private String tenDayDu;
    private String tenDangNhap;
    private String matKhau;
    private String dienThoai;
    private String email;
    private String anhDaiDien;
    private Boolean trangThai;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayTao;
    private LocalDate ngayHetHan;
    private String diaChi;
    private int thuTu;

    public String getNgayTaoText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgayHetHanText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayHetHan);
    }

}
