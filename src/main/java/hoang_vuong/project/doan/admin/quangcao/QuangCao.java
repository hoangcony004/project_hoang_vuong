package hoang_vuong.project.doan.admin.quangcao;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.text.DecimalFormat;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
public class QuangCao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String link;
    private String anh;
    private String tuaDe;
    private String tuaDePhu;
    private String moTa;
    private Float giaTien;
    private Boolean trangThai;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
    private LocalDate ngayHetHan;

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

    public String getNgayHetHanText() {
        if (ngayHetHan == null) {
            return "Chưa cập nhật";
        }
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayHetHan);
    }

    public String getFomatGiaBan() {
        if (giaTien == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(giaTien) + " vnd";
    }

}
