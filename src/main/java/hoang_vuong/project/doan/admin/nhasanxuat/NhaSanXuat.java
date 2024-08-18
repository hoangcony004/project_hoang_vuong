package hoang_vuong.project.doan.admin.nhasanxuat;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Getter
@Setter
public class NhaSanXuat
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String    ten;
    private String    anh;
    private String    link;
    private int       thuTu; 
    private int       noiBat; 
}



