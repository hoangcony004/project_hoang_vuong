package hoang_vuong.project.doan.admin.sanpham;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuat;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuatService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin")
public class SanPhamController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SanPhamService dvl;

    @Autowired
    private NhaSanXuatService nhaSanXuatService;

    @GetMapping("/san-pham")
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
        Page<SanPham> sanPhamPage = dvl.duyetSanPham(PageRequest.of(pageIndex, pageSize));

        // lấy số lượng sản phẩm đan có
        Long totalProducts = dvl.getTotalProducts();
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("role", "1");

        // Cập nhật mô hình với dữ liệu phân trang
        List<NhaSanXuat> dsNhaSanXuat = nhaSanXuatService.dsNhaSanXuat();
        model.addAttribute("dsNhaSanXuat", dsNhaSanXuat);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhamPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ds", sanPhamPage.getContent());
        model.addAttribute("dl", new SanPham());

        model.addAttribute("title", "Quản Lý Sản Phẩm");
        model.addAttribute("title_duyet", "Sản Phẩm");
        model.addAttribute("title_body", "Thêm Sản Phẩm");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("phan_trang", "san-pham");
        model.addAttribute("action", "/admin/san-pham/them");
        model.addAttribute("content", "admin/sanpham/duyet.html");

        model.addAttribute("title_loc", "Lọc Sản Phẩm");
        model.addAttribute("action_loc", "/admin/san-pham/loc");

        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        return "layouts/layout-admin.html";
    }

    @PostMapping("/san-pham/them")
    public String postAdd(@ModelAttribute("SanPham") SanPham dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        dl.setNgayTao(LocalDate.now());
        try {
            dvl.them(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/san-pham";
    }

    @GetMapping("/san-pham/xem")
    public String getShow(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            var dl = dvl.xem(id);
            model.addAttribute("title_body", "Xem Sản Phẩm");
            model.addAttribute("dl", dl);
            model.addAttribute("action", "/admin/san-pham/xem");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể xem. Mã lỗi: " + e.getMessage());
        }

        return "admin/sanpham/form-xem-sp-bs4.html";

    }

    @GetMapping("/san-pham/sua")
    public String getEdit(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        List<NhaSanXuat> dsNhaSanXuat = nhaSanXuatService.dsNhaSanXuat();
        var dl = dvl.xem(id);

        model.addAttribute("dsNhaSanXuat", dsNhaSanXuat);
        model.addAttribute("title_body", "Sửa Sản Phẩm");
        model.addAttribute("title_sm", "Cập nhật");
        model.addAttribute("dl", dl);
        model.addAttribute("action", "/admin/san-pham/sua");

        return "admin/sanpham/form-bs4-sp.html";

    }

    @PostMapping("/san-pham/sua")
    public String postEdit(@ModelAttribute("SanPham") SanPham dl,
            RedirectAttributes redirectAttributes) {

        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        dl.setNgaySua(LocalDate.now());

        try {
            dvl.sua(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã sửa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể sửa. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/san-pham";
    }

    @PostMapping("/san-pham/xoa")
    public String postDelete(@RequestParam("id") int id,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            this.dvl.xoa(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã xóa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/san-pham";
    }

    @PostMapping("/san-pham/loc")
    public String postFilter(
            @RequestParam(value = "minPrice", required = false) Float minPrice,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice,
            @RequestParam(value = "maNSX", required = false) Integer maNSX,
            @RequestParam(value = "banChay", required = false) Boolean banChay,
            @RequestParam(value = "noiBat", required = false) Boolean noiBat,
            RedirectAttributes redirectAttributes,
            Model model,
            HttpServletRequest request) {

        // Kiểm tra nếu nhân viên chưa đăng nhập
        if (Qdl.NhanVienChuaDangNhap(request)) {
            return "redirect:/admin/dang-nhap";
        }

        try {
            // Gọi service để lọc sản phẩm
            Page<SanPham> sanPhams = dvl.locSanPham(minPrice, maxPrice, maNSX, banChay, noiBat, PageRequest.of(0, 5));

            // Kiểm tra nếu không có sản phẩm nào được tìm thấy
            if (sanPhams.isEmpty()) {
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không có kết quả nào khớp với nội dung lọc.");
                return "redirect:/admin/san-pham";
            } else {
                // Nếu có kết quả, truyền dữ liệu vào model
                List<NhaSanXuat> dsNhaSanXuat = nhaSanXuatService.dsNhaSanXuat();
                model.addAttribute("ds", sanPhams.getContent());
                model.addAttribute("dsNhaSanXuat", dsNhaSanXuat);
                model.addAttribute("dl", new SanPham());
                model.addAttribute("title", "Quản Lý Sản Phẩm");
                model.addAttribute("title_duyet", "Sản Phẩm");
                model.addAttribute("title_body", "Thêm Sản Phẩm");
                model.addAttribute("title_sm", "Thêm mới");
                model.addAttribute("phan_trang", "san-pham");
                model.addAttribute("action", "/admin/san-pham/them");
                model.addAttribute("content", "admin/sanpham/duyet-loc.html");
                model.addAttribute("title_loc", "Lọc Sản Phẩm");
                model.addAttribute("action_loc", "/admin/san-pham/loc");
                model.addAttribute("THONG_BAO_SUCCESS", "Lọc thành công!");

            }

        } catch (Exception e) {
            // Xử lý lỗi
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể lọc. Mã lỗi: " + e.getMessage());
            return "redirect:/admin/san-pham";
        }

        return "layouts/layout-admin.html";
    }

    @GetMapping("/san-pham/tim-kiem")
    public String getSearch(Model model,
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu nhân viên chưa đăng nhập thì điều hướng đến trang đăng nhập
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        // Kiểm tra nếu không có từ khóa tìm kiếm
        if (query == null || query.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Chưa nhập từ khóa tìm kiếm.");
            return "redirect:/admin/san-pham";
        }

        // lấy số lượng sản phẩm đan có
        Long totalProducts = dvl.getTotalProducts();
        model.addAttribute("totalProducts", totalProducts);

        // Tìm kiếm sản phẩm dựa trên từ khóa
        List<SanPham> list = dvl.timSanPhamTheoTen(query);
        int total = list.size();

        // Kiểm tra nếu không có kết quả nào
        if (total == 0) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không có kết quả nào trùng với từ khóa tìm kiếm.");
            return "redirect:/admin/san-pham";
        }

        // Phân trang danh sách kết quả
        int start = (page - 1) * pageSize;
        int end = Math.min((start + pageSize), list.size());
        Page<SanPham> sanPhamPage = new PageImpl<>(list.subList(start, end), PageRequest.of(page - 1, pageSize), total);

        // Thêm số thứ tự
        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        // Thêm các thuộc tính cho giao diện
        model.addAttribute("ds", sanPhamPage.getContent());
        model.addAttribute("page", sanPhamPage);
        model.addAttribute("dl", new SanPham());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhamPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);

        redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Tìn kiếm thành công !");
        model.addAttribute("action", "/admin/san-pham/them");
        model.addAttribute("title_body", "Thêm Sản Phẩm");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title", "Quản Lý Sản Phẩm");
        model.addAttribute("content", "admin/sanpham/duyet.html");

        return "layouts/layout-admin.html";
    }

}
