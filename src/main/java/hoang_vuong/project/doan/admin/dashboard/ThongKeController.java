package hoang_vuong.project.doan.admin.dashboard;

import org.apache.catalina.connector.Request;
// import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;

import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHangService;
import hoang_vuong.project.doan.admin.chitietdonhang.SanPhamBanChayDTO;
import hoang_vuong.project.doan.admin.donhang.DoanhThuThang;
import hoang_vuong.project.doan.admin.donhang.DonHangService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class ThongKeController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SanPhamService dvl;

    @Autowired
    private ChiTietDonHangService chiTietDonHangService;

    @Autowired
    private DonHangService donHangService;

    @GetMapping("/thong-ke-san-pham")
    public String getThongKeSanPham(Model model, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            Map<String, Long> thongKe = dvl.thongKeSanPhamTheoNhaSanXuat();
            Map<String, Long> thongKeTrangThai = dvl.thongKeSanPhamTheoTrangThai();
            model.addAttribute("thongKeData", thongKe);
            model.addAttribute("thongKeDataTrangThai", thongKeTrangThai);
            // Ví dụ nếu thongKeTrangThai là boolean
            // Kiểm tra dữ liệu trước khi thêm vào model
            if (thongKe == null || thongKeTrangThai == null) {
                System.err.println("Dữ liệu thống kê là null.");
            } else {
                System.out.println("Trang thai la: " + thongKeTrangThai);
            }

            List<SanPhamBanChayDTO> top10SanPham = chiTietDonHangService.layTop10SanPhamBanChay();
            model.addAttribute("top10SanPham", top10SanPham);
            System.out.println("Array là: " + top10SanPham);

            model.addAttribute("title", "Thống Kê Sản Phẩm");
            model.addAttribute("content", "admin/dashboard/thongkesanpham.html");
            model.addAttribute("title_duyet", "Thống Kê Sản Phẩm");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thống kê sản phẩm. Mã lỗi: " + e.getMessage());
        }

        return "layouts/layout-admin.html";
    }

    @GetMapping("/thong-ke-doanh-thu")
    public String getThongKeDoanhThu(Model model, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            // Lấy dữ liệu doanh thu theo năm từ service
            Map<Integer, String> revenueByYear = donHangService.getRevenueByYear();
            // Đưa dữ liệu vào mô hình
            model.addAttribute("revenueByYear", revenueByYear);
            System.out.println("thống kê theo năm" + revenueByYear);

            model.addAttribute("title", "Thống Kê Doanh Thu");
            model.addAttribute("content", "admin/dashboard/thongkedoanhthu.html");
            model.addAttribute("title_duyet", "Thống Kê Doanh Thu");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thống kê sản phẩm. Mã lỗi: " + e.getMessage());
        }
        return "layouts/layout-admin.html";
    }

    @GetMapping("/thong-ke-chi-tiet-tung-nam")
    public String thongKeChiTietTungNam(@RequestParam("year") int year, Model model,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            // tổng doanh thu
            String totalRevenue = donHangService.getTotalRevenueByYear(year);
            model.addAttribute("tongDoanhThu", totalRevenue);
            System.out.println("Tổng doanh Thu " + totalRevenue);

            // doanh thu 1 năm hiển thị theo 12 tháng
            List<String> doanhThuTheoThang = donHangService.getDoanhThuTheoThang(year);
            model.addAttribute("doanhThuTheoThang", doanhThuTheoThang);
            System.out.println("Doanh thu là: " + doanhThuTheoThang);

            model.addAttribute("title", "Thống Kê Doanh Thu Chi Tiết Theo Năm");
            model.addAttribute("content", "admin/dashboard/thongkechitiettungnam.html");
            model.addAttribute("title_duyet", "Thống Kê Doanh Thu Năm");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thống kê sản phẩm. Mã lỗi: " + e.getMessage());
        }

        return "layouts/layout-admin.html";
    }

    @GetMapping("/thong-ke-chi-tiet-tung-thang")
    public String thongKeChiTietTungThang(@RequestParam("year") int year,
            @RequestParam("month") int month, Model model,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            // Lấy danh sách doanh thu từ service
            List<DoanhThuThang> doanhThu = donHangService.thongKeDoanhThuTheoThang(year, month);

            // Nhóm dữ liệu theo tuần
            List<List<DoanhThuThang>> weeks = groupByWeeks(doanhThu);

            // Thêm danh sách doanh thu vào model
            model.addAttribute("weeks", weeks);
            model.addAttribute("year", year);
            model.addAttribute("month", month);

            System.out.println("Doanh thu tháng " + doanhThu);
            System.out.println("Năm: " + year);
            System.out.println("Tháng: " + month);

            model.addAttribute("title", "Thống Kê Doanh Thu Chi Tiết Theo Năm");
            model.addAttribute("content", "admin/dashboard/thongkechitiettungthang.html");
            model.addAttribute("title_duyet", "Thống Kê Doanh Thu Tháng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thống kê sản phẩm. Mã lỗi: " + e.getMessage());
        }

        return "layouts/layout-admin.html";
    }

    private List<List<DoanhThuThang>> groupByWeeks(List<DoanhThuThang> doanhThuList) {
        List<List<DoanhThuThang>> weeks = new ArrayList<>();
        List<DoanhThuThang> currentWeek = new ArrayList<>();

        for (DoanhThuThang item : doanhThuList) {
            currentWeek.add(item);
            if (currentWeek.size() == 7) {
                weeks.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
        }

        if (!currentWeek.isEmpty()) {
            weeks.add(currentWeek);
        }

        return weeks;
    }

}
