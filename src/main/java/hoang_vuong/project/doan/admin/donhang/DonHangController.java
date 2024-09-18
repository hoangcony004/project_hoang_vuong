package hoang_vuong.project.doan.admin.donhang;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

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
import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHangService;
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

    @Autowired
    private ChiTietDonHangService chiTietDonHangService;

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

        // Lấy danh sách chi tiết đơn hàng từ service
        List<ChiTietDonHang> dsChiTietDonHang = chiTietDonHangService.dsChiTietDonHang();
        model.addAttribute("chiTietDonHangList", dsChiTietDonHang);

        // Tạo đối tượng DonHang và thiết lập danh sách ChiTietDonHang
        DonHang donHang = new DonHang();
        donHang.setChiTietDonHangList(dsChiTietDonHang); // Gán danh sách chi tiết đơn hàng vào đơn hàng
        model.addAttribute("dl", donHang); // Truyền đối tượng DonHang vào model

        // Cập nhật mô hình với dữ liệu phân trang
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", donHangPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ds", donHangPage.getContent());
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

    // v1 thêm đơn hàng
    // @PostMapping("/don-hang/them")
    // public String postAdd(@ModelAttribute("DonHang") DonHang dl,
    // RedirectAttributes redirectAttributes,
    // @RequestParam("soLuong") int soLuong,
    // @RequestParam("MaSP") int MaSP,
    // Model model) {

    // System.out.println("so Luong la:" + soLuong);
    // System.out.println("Ma sp la:" + MaSP);

    // if (Qdl.NhanVienChuaDangNhap(request))
    // return "redirect:/admin/dang-nhap";
    // dl.setNgayTao(LocalDate.now());
    // // dl.setMaKH(null);

    // try {
    // dvl.them(dl);
    // model.addAttribute("soLuong", soLuong);
    // redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành
    // công!");
    // } catch (Exception e) {
    // redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
    // "Không thể thêm mới. Mã lỗi: " + e.getMessage());
    // }

    // return "redirect:/admin/don-hang";
    // }

    // v2 thêm đơn hàng và chi tiết đơn hàng
    @PostMapping("/don-hang/them")
    public String postAdd(@ModelAttribute("DonHang") DonHang dl,
            RedirectAttributes redirectAttributes,
            @RequestParam("soLuong") int soLuong,
            @RequestParam("MaSP") int MaSP,
            Model model) {

        System.out.println("Số lượng là: " + soLuong);
        System.out.println("Mã sản phẩm là: " + MaSP);

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        boolean emailExists = dvl.isEmailExists(dl.getEmail());
        boolean dienThoaiExists = dvl.isDienThoaiExists(dl.getDienThoai());
        if (emailExists || dienThoaiExists) {
            // Nếu email hoặc số điện thoại đã tồn tại, thêm thông báo vào
            // redirectAttributes
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Email hoặc số điện thoại đã tồn tại.");
            // Quay lại trang đơn hàng
            return "redirect:/admin/don-hang";
        }

        String maDonHang = dvl.generateOrderCode();
        System.out.println("Mã đơn hàng " + maDonHang);
        dl.setNgayTao(LocalDate.now());
        dl.setMaDH(maDonHang);

        try {
            // Tạo đối tượng ChiTietDonHang
            ChiTietDonHang chiTiet = new ChiTietDonHang();
            chiTiet.setSoLuong(soLuong);
            chiTiet.setMaSP(MaSP);
            chiTiet.setDonHang(dl);
            chiTiet.setNgayTao(LocalDate.now());

            // Thêm ChiTietDonHang vào danh sách chiTietDonHangList của DonHang
            if (dl.getChiTietDonHangList() == null) {
                dl.setChiTietDonHangList(new ArrayList<>()); // Nếu danh sách rỗng, khởi tạo mới
            }
            dl.getChiTietDonHangList().add(chiTiet); // Thêm chi tiết vào danh sách

            // Lưu cả DonHang và danh sách ChiTietDonHang (cascade = CascadeType.ALL)
            dvl.them(dl);

            // Thêm thông báo và chuyển hướng
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/don-hang";
    }

    @GetMapping("/don-hang/sua")
    public String getEdit(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Lấy thông tin của đơn hàng theo ID
        DonHang dl = dvl.xem(id);

        if (dl == null) {
            model.addAttribute("THONG_BAO_ERROR", "Đơn hàng không tồn tại.");
            return "redirect:/admin/don-hang";
        }

        List<KhachHang> dsKhachHang = dvl_KhSV.dsKhachHang();
        model.addAttribute("dsKhachHang", dsKhachHang);

        List<SanPham> dsSanPham = dvl_SPSV.dsSanPham();
        model.addAttribute("dsSanPham", dsSanPham);
        // Lấy danh sách chi tiết đơn hàng liên kết với đơn hàng
        List<ChiTietDonHang> chiTietDonHangList = dl.getChiTietDonHangList();

        // Truyền các thông tin cần thiết vào model để hiển thị trên giao diện
        model.addAttribute("title_btn_add", "Sửa Đơn Hàng");
        model.addAttribute("title_sm", "Cập nhật");
        model.addAttribute("dl", dl); // Đơn hàng
        model.addAttribute("chiTietDonHangList", chiTietDonHangList); // Danh sách chi tiết đơn hàng
        model.addAttribute("action", "/admin/don-hang/sua");

        return "admin/donhang/form-bs4-dh.html";
    }

}
