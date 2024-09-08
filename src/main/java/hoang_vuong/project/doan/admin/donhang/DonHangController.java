package hoang_vuong.project.doan.admin.donhang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Pageable;

import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class DonHangController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DonHangService dvl;

    @Autowired
    private KhachHangService dvl_KhSV;

    @Autowired
    private SanPhamService dvl_SPSV;

    @GetMapping("/don-hang")
    public String getDuyet(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            HttpServletRequest request) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Chuyển đổi page từ 1-based thành 0-based
        int pageIndex = page - 1;

        // Tạo đối tượng Pageable chỉ với thông tin phân trang, không sắp xếp
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        // Lấy dữ liệu phân trang
        Page<DonHang> donHangPage = dvl.duyetDonHang(pageable);
        List<DonHang> list = donHangPage.getContent();

        List<KhachHang> dsKhachHang = dvl_KhSV.dsKhachHang();
        model.addAttribute("dsKhachHang", dsKhachHang);

        List<SanPham> dsSanPham = dvl_SPSV.dsSanPham();
        model.addAttribute("dsSanPham", dsSanPham);

        // Thêm các thuộc tính cần thiết vào model để hiển thị trong view
        model.addAttribute("ds", list);
        model.addAttribute("page", donHangPage);
        model.addAttribute("dl", new DonHang());
        model.addAttribute("chiTietDonHang", new ChiTietDonHang());

        // DonHangDTO donHangDTO = new DonHangDTO();
        // donHangDTO.setDonHang(new DonHang());
        // donHangDTO.setChiTietDonHang(new ChiTietDonHang());
        // model.addAttribute("donHangDTO", donHangDTO);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", donHangPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("action", "/admin/don-hang/them");
        model.addAttribute("title_body", "Thêm Đơn Hàng");
        model.addAttribute("phan_trang", "don-hang");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title", "Quản Lý Đơn Hàng");
        model.addAttribute("title_duyet", "Đơn Hàng");
        model.addAttribute("title_btn_add", "Thêm Đơn Hàng");
        model.addAttribute("content", "admin/donhang/duyet.html");

        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        return "layouts/layout-admin.html";
    }

}
