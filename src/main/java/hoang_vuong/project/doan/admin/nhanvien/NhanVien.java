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
public class NhanVien
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int Id;

    private String    tenDangNhap;
    private String    email;
    private String    tenDayDu;
    private String    matKhau;
    private String    xacNhanMatKhau;
    private String    anhDaiDien; 
    private Boolean   trangThai;
    private String    dienThoai;
    
    @Column(columnDefinition="LONGTEXT")
    private String    moTa;
    private int       thuTu; 

    private LocalDate ngayTao; 
    private LocalDate ngaySua; 
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayHetHan; 

    public String getNgayTaoText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgayHetHanText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayHetHan);
    }

    public Boolean KhongHopLe() {
        var khl = false;

        return khl;
    }


}
