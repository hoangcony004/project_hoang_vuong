package hoang_vuong.project.doan.admin.donhang;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;

import hoang_vuong.project.doan.admin.anhsanpham.AnhSanPham;
import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.lienhe.LienHe;
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

    // v1
    // @GetMapping("/don-hang")
    // public String getDuyet(Model model,
    // @RequestParam(defaultValue = "1") int page,
    // @RequestParam(defaultValue = "5") int pageSize,
    // HttpServletRequest request) {

    // if (Qdl.NhanVienChuaDangNhap(request))
    // return "redirect:/admin/dang-nhap";

    // // Chuyển đổi page từ 1-based thành 0-based
    // int pageIndex = page - 1;

    // // Tạo đối tượng Pageable chỉ với thông tin phân trang, không sắp xếp
    // Pageable pageable = PageRequest.of(pageIndex, pageSize);

    // // Lấy dữ liệu phân trang
    // Page<DonHang> donHangPage = dvl.duyetDonHang(pageable);
    // List<DonHang> list = donHangPage.getContent();

    // List<KhachHang> dsKhachHang = dvl_KhSV.dsKhachHang();
    // model.addAttribute("dsKhachHang", dsKhachHang);

    // List<SanPham> dsSanPham = dvl_SPSV.dsSanPham();
    // model.addAttribute("dsSanPham", dsSanPham);

    // // Thêm các thuộc tính cần thiết vào model để hiển thị trong view
    // model.addAttribute("ds", donHangPage);
    // model.addAttribute("page", donHangPage);
    // model.addAttribute("dl", new DonHang());
    // // model.addAttribute("chiTietDonHang", new ChiTietDonHang());

    // // DonHangDTO donHangDTO = new DonHangDTO();
    // // donHangDTO.setDonHang(new DonHang());
    // // donHangDTO.setChiTietDonHang(new ChiTietDonHang());
    // // model.addAttribute("donHangDTO", donHangDTO);

    // model.addAttribute("currentPage", page);
    // model.addAttribute("totalPages", donHangPage.getTotalPages());
    // model.addAttribute("pageSize", pageSize);

    // model.addAttribute("action", "/admin/don-hang/them");
    // model.addAttribute("title_body", "Thêm Đơn Hàng");
    // model.addAttribute("phan_trang", "don-hang");
    // model.addAttribute("title_sm", "Thêm mới");
    // model.addAttribute("title", "Quản Lý Đơn Hàng");
    // model.addAttribute("title_duyet", "Đơn Hàng");
    // model.addAttribute("title_btn_add", "Thêm Đơn Hàng");
    // model.addAttribute("content", "admin/donhang/duyet.html");

    // int startIndex = (page - 1) * pageSize;
    // model.addAttribute("startIndex", startIndex);

    // return "layouts/layout-admin.html";
    // }

    // v2
    @GetMapping("/don-hang")
    public String getDuyet(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            HttpServletRequest request) {

        // Kiểm tra xem nhân viên đã đăng nhập chưa
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Tính toán chỉ số trang (pageIndex)
        int pageIndex = page - 1;

        // Lấy dữ liệu phân trang từ dịch vụ
        Page<DonHang> donHangPage = dvl.duyetDonHang(PageRequest.of(pageIndex, pageSize));
        List<DonHang> dsDonHang = dvl.dsDonHang();
        model.addAttribute("dsDonHang", dsDonHang);

        List<KhachHang> dsKhachHang = dvl_KhSV.dsKhachHang();
        model.addAttribute("dsKhachHang", dsKhachHang);

        List<SanPham> dsSanPham = dvl_SPSV.dsSanPham();
        model.addAttribute("dsSanPham", dsSanPham);
        // Cập nhật mô hình với dữ liệu phân trang
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", donHangPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ds", donHangPage.getContent());
        model.addAttribute("dl", new DonHang());
        model.addAttribute("title", "Quản Lý Đơn Hàng");
        model.addAttribute("phan_trang", "don-hang");
        model.addAttribute("title_duyet", "Đơn Hàng");
        model.addAttribute("title_btn_add", "Thêm Đơn Hàng");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("action", "/admin/don-hang/them");
        model.addAttribute("content", "admin/donhang/duyet.html");

        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        return "layouts/layout-admin.html";
    }

    @PostMapping("/don-hang/them")
    public String postAdd(@ModelAttribute("DonHang") DonHang dl,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";
        dl.setNgayTao(LocalDate.now());
        // dl.setMaKH(null);

        try {
            dvl.them(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/don-hang";
    }

}
