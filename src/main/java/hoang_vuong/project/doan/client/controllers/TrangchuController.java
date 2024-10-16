
package hoang_vuong.project.doan.client.controllers;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hoang_vuong.project.doan.admin.anhsanpham.AnhSanPhamService;
import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHangService;
import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.nhasanxuat.NhaSanXuatService;
import hoang_vuong.project.doan.admin.quangcao.QuangCao;
import hoang_vuong.project.doan.admin.quangcao.QuangCaoService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.client.DTO.sanphamDTOlist;
import hoang_vuong.project.doan.client.danhgiasanpham.DanhGiaSanPham;
import hoang_vuong.project.doan.client.danhgiasanpham.DanhGiaSanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class TrangchuController {
  @Autowired
  private HttpServletRequest request;

  @Autowired
  private SanPhamService dvl;

  @Autowired
  private NhaSanXuatService nsxdv;

  @Autowired
  private AnhSanPhamService anhdv;

  @Autowired
  private QuangCaoService quangcao;

  @Autowired
  private ChiTietDonHangService chiTietDonHangService;

  @Autowired
  private DanhGiaSanPhamService danhGiaService;

  @Autowired
  private KhachHangService khachHangService;

  public String formatPrice(float price) {
    DecimalFormat formatter = new DecimalFormat("#,###.00"); // Định dạng: phân cách hàng nghìn và 2 chữ số thập phân
    return formatter.format(price);
  }

  @PostMapping("/post/mnsx")
  @ResponseBody
  public List<sanphamDTOlist> getProductsByNsx(@RequestParam("id") int nsxId) {
    // Lấy danh sách sản phẩm theo mã nhà sản xuất
    List<SanPham> sanPhamList = dvl.timMaNSX(nsxId);
    return sanPhamList.stream()
        .map(sp -> new sanphamDTOlist(sp.getId(), sp.getTenSP(), sp.getAnhDaiDien(), sp.getDonGia()))
        .collect(Collectors.toList());
  }

  @GetMapping({
      "/apps",
      "/"
  })
  public String get(Model model, HttpSession session) {

    // java.util.List<SanPham> list = dvl.dsSanPham();
    List<SanPham> noibat = dvl.dsSanPhamNoiBat();
    List<SanPham> banchay = dvl.dsSanPhamBanChay();
    List<QuangCao> dsquangcao = quangcao.choPhep();
    List<SanPham> dsall = dvl.dsSanPham();
    model.addAttribute("spAll", dsall);
    // Format giá cho từng sản phẩm trong danh sách banchay
    model.addAttribute("dsquangcao", dsquangcao);
    model.addAttribute("ds_noibat", noibat);
    model.addAttribute("ds_banchay", banchay);
    // model.addAttribute("ds", list);

    model.addAttribute("content", "client/index.html");


    return "layouts/layout-client";
  }

  @GetMapping("/apps/product")
  public String getXem(Model model, @RequestParam(value = "id") int id) {

    var dl = anhdv.dsmasp(id);
    model.addAttribute("ds", dl);
    var dls = dvl.timTheoId(id);

    List<SanPham> randomSanPhams = dvl.getRandomSanPhams();
    model.addAttribute("randomSanPhams", randomSanPhams);
    
    // int khachhangId = (int) request.getSession().getAttribute("khachhang_Id");
    List<DanhGiaSanPham> danhGia = danhGiaService.getDanhGiaBySanPham(id);
    System.out.println("tim id" + danhGia);
    // System.out.println("id khach hang la" + khachhangId);


    model.addAttribute("dl", dls);

    model.addAttribute("content", "client/product-sidebar.html");
    return "layouts/layout-client";

  }

  @GetMapping("/apps/lay-san-pham-theo-nsx")
  public String getSanPhamByNSX(@RequestParam("maNSX") Integer maNSX, Model model) {
    System.out.println("mã nsx" + maNSX); // In ra giá trị maNSX để kiểm tra
    List<SanPham> sanPhams = dvl.getSanPhamsByNSX(maNSX); // Lấy danh sách sản phẩm từ dịch vụ
    model.addAttribute("sanPhams", sanPhams); // Thêm danh sách sản phẩm vào model
    System.out.println("du lieu" + sanPhams);
    return "client/lay-san-pham-theo-nsx"; // Trả về tên template
  }


  @PostMapping("/apps/danh-gia-san-pham")
  public String danhGiaSanPham(@RequestParam("id") Integer id,
      @RequestParam("star-rating") int starRating,
      @RequestParam("nhanXet") String nhanXet,
      Model model, RedirectAttributes redirectAttributes) {
    if (Qdl.KhachHangChuaDangNhap(request)) {
      redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Bạn phải đăng nhập để đánh giá sản phẩm");
      return "redirect:/apps/product?id=" + id;
    }

    int khachhangId = (int) request.getSession().getAttribute("khachhang_Id");

    System.out.println("ID khach hang: " + khachhangId);
    System.out.println("ID sản phẩm: " + id);
    System.out.println("Số sao: " + starRating);
    System.out.println("Nhận xét: " + nhanXet);

    try {

      // Tạo đối tượng DanhGia của KhachHang
      var khachHang = khachHangService.timKhachHangTheoId(khachhangId);
      System.out.println("id khach hang la" + khachHang);

      SanPham sanPham = dvl.timTheoId(id);

      // Tạo đối tượng DanhGia mới
      DanhGiaSanPham danhGiaMoi = new DanhGiaSanPham();

      // Gán các giá trị vào đối tượng
      danhGiaMoi.setKhachHang(khachHang);
      danhGiaMoi.setSanPham(sanPham);
      danhGiaMoi.setDiemDanhGia(starRating);
      danhGiaMoi.setNhanXet(nhanXet);
      danhGiaMoi.setNgayDanhGia(LocalDate.now());

      // Gọi phương thức để lưu vào cơ sở dữ liệu
      danhGiaService.them(danhGiaMoi);

      redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm phần đánh giá của bạn!");

    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể thêm đánh giá!. Mã lỗi: " + e.getMessage());
    }

    return "redirect:/apps/product?id=" + id;
  }


  @GetMapping("/san-pham/tim-kiem")
  public String getSearch(Model model,
      @RequestParam("query") String query,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "9") int pageSize,
      HttpServletRequest request,
      RedirectAttributes redirectAttributes) {

    // Kiểm tra nếu không có từ khóa tìm kiếm
    if (query == null || query.trim().isEmpty()) {
      redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
          "Chưa nhập từ khóa tìm kiếm.");
      return "redirect:/";
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
      return "redirect:/";
    }

    // Phân trang danh sách kết quả
    int start = (page - 1) * pageSize;
    int end = Math.min((start + pageSize), list.size());
    Page<SanPham> sanPhamPage = new PageImpl<>(list.subList(start, end), PageRequest.of(page - 1, pageSize), total);

    // Thêm số thứ tự
    int startIndex = (page - 1) * pageSize;
    model.addAttribute("startIndex", startIndex);

    // Thêm các thuộc tính cho giao diện
    model.addAttribute("dlns", sanPhamPage.getContent());
    model.addAttribute("page", sanPhamPage);
    model.addAttribute("dl", new SanPham());

    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", sanPhamPage.getTotalPages());
    model.addAttribute("pageSize", pageSize);

    redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Tìn kiếm thành công !");
    model.addAttribute("content", "client/category.html");
    return "layouts/layout-client";
  }

  @GetMapping("/apps/error")
  public String getError(Model model) {
    model.addAttribute("content", "client/404.html");
    return "layouts/layout-client";
  }

}
