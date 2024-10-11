package hoang_vuong.project.doan.client.favourite;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.math3.stat.descriptive.summary.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favourites")
public class favourite {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer user_id;
    @Column(name = "product_id", nullable = false)
    private Integer product_id;
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

}
