package hoang_vuong.project.doan.admin.nhanvien;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(unique = true)
    private String tenDangNhap;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String dienThoai;

    private String tenDayDu;
    private String matKhau;
    private String xacNhanMatKhau;
    private String anhDaiDien;
    private Boolean trangThai;

    private String matKhauMoi;
    private String nhapLaiMatKhauMoi;

    @Column(columnDefinition = "LONGTEXT")
    private String moTa;

    private LocalDate ngayTao;
    private LocalDate ngaySua;

    public String getNgayTaoText() {
        if (ngayTao == null) {
            return "Chưa cập nhật";
        }
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgaySuaText() {
        if (ngaySua == null) {
            return "Chưa cập nhật";
        }
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngaySua);
    }

}
