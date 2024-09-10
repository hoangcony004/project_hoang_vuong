package hoang_vuong.project.doan.admin.quangcao;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.text.DecimalFormat;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class QuangCao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String link;
    private String anh;
    private int thuTu;
    private String tuaDe;
    private String tuaDePhu;
    private String moTa;
    private Float giaTien;
    private Boolean trangThai;

    public String getFomatGiaBan() {
        if (giaTien == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(giaTien) + " vnd";
    }

}
