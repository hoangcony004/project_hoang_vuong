package hoang_vuong.project.doan.qdl;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimKiemService {

    public List<TimKiem> timKiemChucNang(String keyword) {
        List<TimKiem> chucNangs = new ArrayList<>();

        // Các chức năng cũ
        if (keyword.toLowerCase().contains("cài đặt")) {
            chucNangs.add(new TimKiem("Cài Đặt", "/admin/settings"));
        }
        if (keyword.toLowerCase().contains("dashboard")) {
            chucNangs.add(new TimKiem("Dashboard", "/admin/dashboard"));
        }
        if (keyword.toLowerCase().contains("nhân viên")) {
            chucNangs.add(new TimKiem("Nhân Viên", "/admin/nhan-vien"));
        }
        if (keyword.toLowerCase().contains("đơn hàng")) {
            chucNangs.add(new TimKiem("Đơn Hàng", "/admin/don-hang"));
        }

        // Các chức năng mới
        if (keyword.toLowerCase().contains("khách hàng")) {
            chucNangs.add(new TimKiem("Khách Hàng", "/admin/khach-hang"));
        }
        if (keyword.toLowerCase().contains("liên hệ")) {
            chucNangs.add(new TimKiem("Liên Hệ", "/admin/lien-he"));
        }
        if (keyword.toLowerCase().contains("loại")) {
            chucNangs.add(new TimKiem("Loại", "/admin/loai"));
        }
        if (keyword.toLowerCase().contains("loại sản phẩm")) {
            chucNangs.add(new TimKiem("Loại Sản Phẩm", "/admin/loai-san-pham"));
        }
        if (keyword.toLowerCase().contains("nhà sản xuất")) {
            chucNangs.add(new TimKiem("Nhà Sản Xuất", "/admin/nha-san-xuat"));
        }
        if (keyword.toLowerCase().contains("quảng cáo")) {
            chucNangs.add(new TimKiem("Quảng Cáo", "/admin/quang-cao"));
        }
        if (keyword.toLowerCase().contains("sản phẩm")) {
            chucNangs.add(new TimKiem("Sản Phẩm", "/admin/san-pham"));
        }
        if (keyword.toLowerCase().contains("quên mật khẩu")) {
            chucNangs.add(new TimKiem("Quên Mật Khẩu", "/admin/quen-mat-khau"));
        }
        if (keyword.toLowerCase().contains("đổi mật khẩu")) {
            chucNangs.add(new TimKiem("Đổi Mật Khẩu", "/admin/doi-mat-khau"));
        }

        return chucNangs;
    }
}
