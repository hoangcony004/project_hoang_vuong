package hoang_vuong.project.doan.admin.donhang;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.format.annotation.DateTimeFormat;

import hoang_vuong.project.doan.admin.anhsanpham.AnhSanPham;
import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHang;
import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHangService;
import java.io.IOException; // Để sử dụng IOException
import java.text.SimpleDateFormat; // Để định dạng ngày
import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.lienhe.LienHe;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;

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

        // lấy tổng tất cả đơn hàng
        Long totalOrders = dvl.getTotalOrders();
        model.addAttribute("totalOrders", totalOrders);

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
        donHang.setChiTietDonHangList(dsChiTietDonHang);
        model.addAttribute("dl", donHang);

        // Cập nhật mô hình với dữ liệu phân trang
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", donHangPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ds", donHangPage.getContent());
        model.addAttribute("title", "Quản Lý Đơn Hàng");
        model.addAttribute("title_duyet", "Đơn Hàng");
        model.addAttribute("title_btn_add", "Thêm Đơn Hàng");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("title_loc", "Xuất Dữ Liệu");
        model.addAttribute("action", "/admin/don-hang/them");
        model.addAttribute("action_loc", "/admin/don-hang/export");
        model.addAttribute("content", "admin/donhang/duyet.html");

        int startIndex = (page - 1) * pageSize;
        model.addAttribute("startIndex", startIndex);

        return "layouts/layout-admin.html";
    }

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
        String maDonHang = dvl.generateOrderCode();
        System.out.println("Mã đơn hàng " + maDonHang);
        Float donGia = dl.getTongTien();
        dl.setNgayTao(LocalDate.now());
        dl.setMaDH(maDonHang);

        try {
            // Tạo đối tượng ChiTietDonHang
            ChiTietDonHang chiTiet = new ChiTietDonHang();
            chiTiet.setSoLuong(soLuong);
            chiTiet.setMaSP(MaSP);
            chiTiet.setDonHang(dl);
            chiTiet.setNgayTao(LocalDate.now());
            chiTiet.setDonGia(donGia);
            chiTiet.setTongTien(donGia * soLuong);

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

        // List<KhachHang> dsKhachHang = dvl_KhSV.dsKhachHang();
        // model.addAttribute("dsKhachHang", dsKhachHang);

        // List<SanPham> dsSanPham = dvl_SPSV.dsSanPham();
        // model.addAttribute("dsSanPham", dsSanPham);

        List<ChiTietDonHang> chiTietDonHangs = chiTietDonHangService.getChiTietDonHangByDonHangId(id);
        model.addAttribute("chiTietDonHangs", chiTietDonHangs);

        model.addAttribute("title_btn_add", "Sửa Đơn Hàng");
        model.addAttribute("title_sm", "Cập nhật");
        model.addAttribute("dl", dl);

        model.addAttribute("action", "/admin/don-hang/sua");

        return "admin/donhang/form-sua-dh-bs4.html";
    }

    @GetMapping("/don-hang/tim-kiem")
    public String getTimKiemDonHang(@RequestParam("query") String query, Model model,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";
        try {
            // Tìm kiếm đơn hàng theo maDH
            List<DonHang> orders = dvl.searchOrdersByMaDH(query);

            if (orders != null && !orders.isEmpty()) {

                // Truyền kết quả tìm kiếm vào model để hiển thị trong view
                model.addAttribute("ds", orders);
                // Lấy danh sách chi tiết đơn hàng từ service
                List<ChiTietDonHang> dsChiTietDonHang = chiTietDonHangService.dsChiTietDonHang();
                model.addAttribute("chiTietDonHangList", dsChiTietDonHang);

                // Tạo đối tượng DonHang và thiết lập danh sách ChiTietDonHang
                DonHang donHang = new DonHang();
                donHang.setChiTietDonHangList(dsChiTietDonHang);
                model.addAttribute("dl", donHang);
                model.addAttribute("content", "admin/donhang/duyet_search.html");
                model.addAttribute("title", "Tìm Kiếm Đơn Hàng");
                model.addAttribute("title_duyet", "Tìm Kiếm Đơn Hàng");
                model.addAttribute("title_btn_add", "Thêm Đơn Hàng");
                model.addAttribute("title_sm", "Thêm mới");
                model.addAttribute("action", "/admin/don-hang/them");
                model.addAttribute("THONG_BAO_SUCCESS", "Tìm kiếm đơn hàng thành công!");
            } else {
                redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không có đơn hàng nào hoặc sai mã đơn hàng!");
                return "redirect:/admin/don-hang";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thêm xem. Mã lỗi: " + e.getMessage());
        }
        return "layouts/layout-admin.html";
    }

    @GetMapping("/don-hang/xem")
    public String getXemDonHang(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            List<ChiTietDonHang> chiTietDonHangs = chiTietDonHangService.getChiTietDonHangByDonHangId(id);

            model.addAttribute("chiTietDonHangs", chiTietDonHangs);
            model.addAttribute("title_body", "Xem Đơn Hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể thêm xem. Mã lỗi: " + e.getMessage());
        }

        return "admin/donhang/form-xem-dh-bs4.html";
    }

    @PostMapping("/don-hang/duyet")
    public String postDuyetdonHang(@RequestParam("id") int id, @RequestParam("trangThai") int trangThai,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        System.out.println("ID nhận được trong controller là: " + id);

        try {
            // Sử dụng phương thức timTheoId để tìm đơn hàng
            DonHang donHang = dvl.timTheoId(id);

            // Cập nhật trạng thái
            donHang.setTrangThai(trangThai);
            dvl.luu(donHang); // Gọi hàm lưu

            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã duyệt đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể duyệt. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/don-hang";
    }

    // @PostMapping("/don-hang/export")
    // public String postDonHangToExcel(
    // @RequestParam("startDate") @DateTimeFormat(iso =
    // DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
    // @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    // LocalDate endDate,
    // RedirectAttributes redirectAttributes) {

    // System.out.println("Ngày bắt đầu: " + startDate);
    // System.out.println("Ngày kết thuật: " + endDate);
    // List<DonHang> donHangList = dvl.getDonHangByDateRange(startDate, endDate);

    // System.out.println("Danh sách đơn hàng: " + donHangList);

    // try {

    // redirectAttributes.addFlashAttribute("successMessage", "Xuất Excel thành
    // công!");
    // } catch (Exception e) {
    // redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xuất Excel: " +
    // e.getMessage());
    // }

    // return "redirect:/admin/don-hang";
    // }

    @PostMapping("/don-hang/export")
    public String postDonHangToExcel(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            RedirectAttributes redirectAttributes, HttpServletResponse response) {

        System.out.println("Ngày bắt đầu: " + startDate);
        System.out.println("Ngày kết thúc: " + endDate);

        // Lấy danh sách đơn hàng trong khoảng thời gian được chọn
        List<DonHang> donHangList = dvl.getDonHangByDateRange(startDate, endDate);

        // donHangList.forEach(donHang -> {
        // System.out.println("ID: " + donHang.getId());
        // System.out.println("Tên khách hàng: " + donHang.getMaKH());
        // System.out.println("Tổng tiền: " + donHang.getFomatTongTien());
        // System.out.println("Ngày tạo: " + donHang.getNgayTao());
        // System.out.println("----------");
        // });

        // Kiểm tra xem danh sách đơn hàng có rỗng hay không
        if (donHangList.isEmpty()) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không có đơn hàng nào trong khoảng thời gian đã chọn.");
            return "redirect:/admin/don-hang";
        }

        try {
            // Tạo file Excel từ danh sách đơn hàng
            // (Giả sử bạn đã có phương thức để xuất ra file Excel, gọi nó ở đây)
            exportDonHangToExcel(donHangList, response);
            System.out.println("Đã xuat Excel");
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Xuất Excel thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Lỗi xuất Excel: " + e.getMessage());
        }

        return null;
    }

    // Phương thức xuất Excel (giả định)
    private void exportDonHangToExcel(List<DonHang> donHangList, HttpServletResponse response) throws IOException {
        // Thiết lập tiêu đề và loại file trả về
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=donhang_" + System.currentTimeMillis() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        // Tạo workbook và sheet mới
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Đơn hàng");

        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] columnHeaders = { "ID", "Mã Khách Hàng", "Tên khách hàng", "Số Điện Thoại", "Email", "Tổng tiền",
                "Tỉnh Thành", "Quận huyện", "Xã Phường", "Địa Chỉ", "Ngày Đặt Hàng" };
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }

        // Điền dữ liệu đơn hàng vào các dòng tiếp theo
        int rowIndex = 1;
        for (DonHang donHang : donHangList) {
            Row row = sheet.createRow(rowIndex++);

            // Lấy thông tin từ donHang
            row.createCell(0).setCellValue(donHang.getId());
            row.createCell(1).setCellValue(donHang.getMaKH());
            row.createCell(2).setCellValue(donHang.getTenDayDu());
            row.createCell(3).setCellValue(donHang.getDienThoai());
            row.createCell(4).setCellValue(donHang.getEmail());
            row.createCell(5).setCellValue(donHang.getFomatTongTien());
            row.createCell(6).setCellValue(donHang.getTinhThanh());
            row.createCell(7).setCellValue(donHang.getQuanHuyen());
            row.createCell(8).setCellValue(donHang.getXaPhuong());
            row.createCell(9).setCellValue(donHang.getDiaChi());
            row.createCell(10).setCellValue(donHang.getNgayTaoText());

            // Định dạng ngày tháng
            // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // row.createCell(3).setCellValue(dateFormat.format(donHang.getNgayTao()));
        }

        // Tự động điều chỉnh kích thước các cột
        for (int i = 0; i < columnHeaders.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi workbook ra output stream
        workbook.write(response.getOutputStream());
        workbook.close();

    }

}
