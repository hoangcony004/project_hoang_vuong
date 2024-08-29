package hoang_vuong.project.doan.qdl;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimKiemService {

    public List<TimKiem> timKiemChucNang(String keyword) {
        List<TimKiem> chucNangs = new ArrayList<>();
        String normalizedKeyword = StringUtils.removeDiacritics(keyword);

        // Ví dụ: Tìm kiếm dựa trên từ khóa
        if (normalizedKeyword.contains("cài đặt")) {
            chucNangs.add(new TimKiem("Cài Đặt", "/admin/cai-dat"));
        }
        if (normalizedKeyword.contains("dashboard")) {
            chucNangs.add(new TimKiem("Dashboard", "/admin/dashboard"));
        }
        if (normalizedKeyword.contains("nhân viên")) {
            chucNangs.add(new TimKiem("Nhân Viên", "/admin/nhan-vien"));
        }
        if (normalizedKeyword.contains("đơn hàng")) {
            chucNangs.add(new TimKiem("Đơn Hàng", "/admin/don-hang"));
        }
        if (normalizedKeyword.contains("ảnh sản phẩm")) {
            chucNangs.add(new TimKiem("Ảnh Sản Phẩm", "/admin/anh-san-pham"));
        }
        if (normalizedKeyword.contains("khách hàng")) {
            chucNangs.add(new TimKiem("Khách Hàng", "/admin/khach-hang"));
        }
        if (normalizedKeyword.contains("liên hệ")) {
            chucNangs.add(new TimKiem("Liên Hệ", "/admin/lien-he"));
        }
        if (normalizedKeyword.contains("loại sản phẩm")) {
            chucNangs.add(new TimKiem("Loại Sản Phẩm", "/admin/loai-san-pham"));
        }
        if (normalizedKeyword.contains("loại")) {
            chucNangs.add(new TimKiem("Loại", "/admin/loai"));
        }
        if (normalizedKeyword.contains("nhà sản xuất")) {
            chucNangs.add(new TimKiem("Nhà Sản Xuất", "/admin/nha-san-xuat"));
        }
        if (normalizedKeyword.contains("quảng cáo")) {
            chucNangs.add(new TimKiem("Quảng Cáo", "/admin/quang-cao"));
        }
        if (normalizedKeyword.contains("sản phẩm")) {
            chucNangs.add(new TimKiem("Sản Phẩm", "/admin/san-pham"));
        }
        if (normalizedKeyword.contains("quên mật khẩu")) {
            chucNangs.add(new TimKiem("Quên Mật Khẩu", "/admin/quen-mat-khau"));
        }
        if (normalizedKeyword.contains("đổi mật khẩu")) {
            chucNangs.add(new TimKiem("Đổi Mật Khẩu", "/admin/doi-mat-khau"));
        }
        
        return chucNangs;
    }
}
