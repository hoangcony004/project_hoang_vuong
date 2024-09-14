package hoang_vuong.project.doan.client.trangchu;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.var;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hoang_vuong.project.doan.admin.donhang.ChiTietDonHang;
import hoang_vuong.project.doan.admin.donhang.DonHang;
import hoang_vuong.project.doan.admin.donhang.DonHangService;
import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;
@Controller
public class checkoutController {
        @Autowired
    private HttpSession session;
    @Autowired
    private KhachHangService kh_dl;
    @Autowired
    private  DonHangService dvlDonHang;
      @Autowired
  private  SanPhamService dvlSanPham;
      @GetMapping("/apps/checkout")
    public String getGioHangThanhToan(Model model, HttpServletRequest request) 
    {
        if//nếu không có sản phẩm
        (!gioHangCoSanPham()){
        return "redirect:/";
        }
        int maKhachHang =1;
        // Kiểm tra xem, khách hàng là vãng lai(anonymous), hay thành viên (membership)
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
       // vãng lai
     
        if (Qdl.KhachHangChuaDangNhap(request)) {
             maKhachHang = 0;        
               var kh = new KhachHang(); 
                model.addAttribute("alldatakh", kh); // Khởi tạo đối tượng rỗng nếu không có dữ liệu       
        }else{
          maKhachHang = khachhang_Id;
          var kh= kh_dl.xemKhachHang(khachhang_Id);
          model.addAttribute("alldatakh", kh); 
        }

     
        // Nếu khách đăng nhập rồi (session.CUS_LOGGED)
        // thì cập nhật lại giá trị cho maKhachHang

        // để còn gán mã khách hàng vào form thanh toán
      //  var dh = new DonHang();
            

        @SuppressWarnings("unchecked")
        Map<Integer,Integer> cartMap = (Map<Integer,Integer>)session.getAttribute("cart");

        List<Map<String,String>> cartData = new ArrayList<Map<String,String>>();

        for (Integer maSanPham : cartMap.keySet()) 
        {
            // System.out.println(key + ":" + map.get(key));
            // tongSoSanPham += cartMap.get(maSanPham);
            SanPham sp = dvlSanPham.xem(maSanPham);
            var donGiaStr = String.valueOf(sp.getDonGia());
            float thanhTien = cartMap.get(maSanPham) * sp.getDonGia();

            Map<String,String> map = new HashMap<>();
            map.put("id", String.valueOf(sp.getId()));
            map.put("ten", sp.getTenSP());
            map.put("donGia", donGiaStr);
            map.put("donGiaVi", String.format("%,.2f", sp.getDonGia()));
            map.put("anh", sp.getAnhDaiDien());
            map.put("soluong", String.valueOf(cartMap.get(maSanPham)));
            map.put("thanhTien", String.format("%,.0f", thanhTien));

            cartData.add(map);
        }
    
        // Gửi dữ liệu giỏ hàng sang bên View
        model.addAttribute("cartData", cartData);
        model.addAttribute("tongGiaTriGioHang", tongGiaTriGioHang());
        model.addAttribute("tongGiaTriGioHangVi", tongGiaTriGioHangVi());

        model.addAttribute("maKhachHang", maKhachHang); 
        model.addAttribute("action", "/apps/checkout"); 
       // model.addAttribute("dl", dh); // gửi dữ liệu khách hàng sang
       // gửi dữ liệu khách hàng sang
        model.addAttribute("content", "client/checkout.html"); 
        //return "client/checkout.html";
        // ...được đặt vào bố cục chung của toàn website
        return "layouts/layout-client.html"; // layout.html
        
    }
    private boolean gioHangCoSanPham()
    {
        @SuppressWarnings("unchecked")
        Map<Integer,Integer> cartMap = (Map<Integer,Integer>)session.getAttribute("cart");

        if(cartMap==null || cartMap.isEmpty())
            return false;

        return true;
    }
    private String tongGiaTriGioHangVi()
    {
        return String.format("%,.0f", tongGiaTriGioHang());
    }

    private float tongGiaTriGioHang()// cartGetTotal() 
    {

        if//nếu không có sản phẩm
        (!gioHangCoSanPham())
            return 0;

        int tong = 0;
        @SuppressWarnings("unchecked")
        Map<Integer,Integer> cartMap = (Map<Integer,Integer>)session.getAttribute("cart");

        for (Integer maSanPham : cartMap.keySet()) 
        {
            SanPham sp = dvlSanPham.xem(maSanPham);
            int soluong = cartMap.get(maSanPham);
            tong +=  sp.getDonGia() * soluong;
        }

        return tong;
    }
    @PostMapping("/apps/checkout")
    private String postThem(@ModelAttribute("alldatakh") DonHang dh,
    @RequestParam("city") String city,
    @RequestParam("district") String district,
    @RequestParam("ward") String ward,
    @RequestParam("fullAdress") String fullAdress,
    @RequestParam("note") String note
    ){
      
        dh.setDiaChi(fullAdress+"("+ward+district+city+")");
        dh.setGhiChu(note);
        dh.setNgayTao(LocalDate.now());
        dh.setTongTien(tongGiaTriGioHang());
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        dh.setMaKH(khachhang_Id);
        var donhang= this.dvlDonHang.luuDonHang(dh);
        int maDon = donhang.getId();
        @SuppressWarnings("unchecked")
        Map<Integer,Integer> cartMap = (Map<Integer,Integer>)session.getAttribute("cart");

        List<Map<String,String>> cartData = new ArrayList<Map<String,String>>();

        for (Integer maSanPham : cartMap.keySet()) 
        { 
            SanPham sp = dvlSanPham.xem(maSanPham);
            // tongSoSanPham += cartMap.get(maSanPham);
            // var donGiaStr = String.valueOf(sp.getDonGia());
            // float thanhTien = cartMap.get(maSanPham) * sp.getDonGia();
            int soLuong = cartMap.get(maSanPham);

             // Với mỗi sản phẩm bên trong Giỏ Hàng, sẽ có 1 chi tiết đơn hàng sinh ra
    
     
        }

        khoiTaoGioHang();
        //return "client/checkout.html";
        // ...được đặt vào bố cục chung của toàn website
        return "redirect:/";
    }
    private void khoiTaoGioHang()
    {
        session.setAttribute("cart", new HashMap<Integer,Integer>());
        session.setAttribute("SoSanPhamTrongGioHang", 0);
    }
}
