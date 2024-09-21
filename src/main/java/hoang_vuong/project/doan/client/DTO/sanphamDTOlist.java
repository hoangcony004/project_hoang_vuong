package hoang_vuong.project.doan.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class sanphamDTOlist {
    private int id;
    private String tenSP;
    private String anhDaiDien;
    private float donGia;
}
