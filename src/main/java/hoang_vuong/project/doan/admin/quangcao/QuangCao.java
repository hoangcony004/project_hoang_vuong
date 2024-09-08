package hoang_vuong.project.doan.admin.quangcao;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
    private String giaTien;
    private Boolean trangThai;

}
