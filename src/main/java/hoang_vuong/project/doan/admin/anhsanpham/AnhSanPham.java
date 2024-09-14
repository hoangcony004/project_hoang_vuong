package hoang_vuong.project.doan.admin.anhsanpham;

import hoang_vuong.project.doan.admin.sanpham.SanPham;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
public class AnhSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String hinhAnh;

    private Integer maSP;
    @ManyToOne
    @JoinColumn(name = "maSP", insertable = false, updatable = false)
    private SanPham sanPham;

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
