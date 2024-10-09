package hoang_vuong.project.doan.client.favourite;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hoang_vuong.project.doan.admin.khachhang.KhachHang;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private KhachHang user;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private SanPham product;

    @Column(name = "added_date")
    private LocalDate addedDate;
}
