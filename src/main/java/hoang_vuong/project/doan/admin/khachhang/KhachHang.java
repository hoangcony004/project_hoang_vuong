package hoang_vuong.project.doan.admin.khachhang;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private String tenDayDu;

    @Column(unique = true)
    private String tenDangNhap;
    private String matKhau;
    @Column(unique = true)
    private String dienThoai;
    @Column(unique = true)
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
