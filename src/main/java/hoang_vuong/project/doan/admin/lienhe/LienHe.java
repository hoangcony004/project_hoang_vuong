package hoang_vuong.project.doan.admin.lienhe;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
public class LienHe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String ten;
    @Column(unique = true)
    private String email;
    private String tieuDe;
    private String tinNhan;
    @Column(unique = true)
    private String dienThoai;
    private String diaChi;
    private String webSite;
    
    private LocalDate ngayTao;
    private LocalDate ngaySua;



    public String getNgayTaoText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgaySuaText() {
        if (ngaySua == null) {
            return "Chưa cập nhật";
        }
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngaySua);
    }

}
