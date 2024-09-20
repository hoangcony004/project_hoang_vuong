package hoang_vuong.project.doan.client.banking;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHang;
import hoang_vuong.project.doan.admin.chitietdonhang.ChiTietDonHangService;
import hoang_vuong.project.doan.admin.donhang.DonHang;
import hoang_vuong.project.doan.admin.donhang.DonHangService;
import hoang_vuong.project.doan.admin.khachhang.KhachHang;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.client.banking.Config.VNPayService;
import hoang_vuong.project.doan.qdl.Qdl;

import java.util.Date;
@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private KhachHangService kh_dl;
    @Autowired
    private HttpSession session;
     @Autowired
  private  SanPhamService dvlSanPham;
    @Autowired
    private VNPayService vnPayService;
    @Autowired
  private ChiTietDonHangService dvlChiTietDonHang;
 @Autowired
    private  DonHangService dvlDonHang;
    // @PostMapping("/submitOrder")
    // public String submidOrder(@RequestParam("amount") double  orderTotal,
    //                         @RequestParam("orderInfo") String orderInfo,
    //                         HttpServletRequest request){
    //                             int intValue = (int) orderTotal;
    //     String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    //     String vnpayUrl = vnPayService.createOrder(intValue, orderInfo, baseUrl);

    //     return "redirect:" + vnpayUrl;
    // }
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
    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model, HttpSession session){
        int paymentStatus =vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate ="";
        try {
            Date date = inputFormat.parse(paymentTime);
             formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
         
            e.printStackTrace();
        }
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrices = request.getParameter("vnp_Amount");
        float totalPrice = Float.parseFloat(totalPrices); 
        totalPrice = totalPrice / 100;
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", formattedDate);
        model.addAttribute("transactionId", transactionId);
  if (paymentStatus == 1) { // Nếu thanh toán thành công
     
        // Lấy thông tin từ session
        DonHang dh = (DonHang) session.getAttribute("donHang");
        String city = (String) session.getAttribute("city");
        String district = (String) session.getAttribute("district");
        String ward = (String) session.getAttribute("ward");
        String fullAdress = (String) session.getAttribute("fullAdress");
        String note = (String) session.getAttribute("note");

        // Xử lý thêm thông tin vào đơn hàng
        dh.setDiaChi(fullAdress+" ("+ward+" "+district+" "+city+")");
        dh.setGhiChu(note);
        dh.setNgayTao(LocalDate.now());
        dh.setTongTien(tongGiaTriGioHang());
        dh.setThanhtoan("banking");
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        if(khachhang_Id==null){
            khachhang_Id =1010101010;
            dh.setMaKH(khachhang_Id);
        }else{
            dh.setMaKH(khachhang_Id);
        }
        
        var donhang = this.dvlDonHang.luuDonHang(dh);
 int maDon = donhang.getId();
        @SuppressWarnings("unchecked")
        Map<Integer,Integer> cartMap = (Map<Integer,Integer>)session.getAttribute("cart");

        for (Integer maSanPham : cartMap.keySet()) 
        { 
            SanPham sp = dvlSanPham.xem(maSanPham);
            // tongSoSanPham += cartMap.get(maSanPham);
            // var donGiaStr = String.valueOf(sp.getDonGia());
            // float thanhTien = cartMap.get(maSanPham) * sp.getDonGia();
            int soLuong = cartMap.get(maSanPham);
              var ctdh = new ChiTietDonHang();
            ctdh.setDonHangId(maDon);
            ctdh.setMaSP(maSanPham);
            ctdh.setTen(sp.getTenSP());
            ctdh.setModel(sp.getModel());
            ctdh.setDonGia(sp.getDonGia());
            ctdh.setSoLuong(soLuong);
            ctdh.setTongTien(soLuong * sp.getDonGia());
            ctdh.setNgayTao(LocalDate.now());
            ctdh.setNgaySua(LocalDate.now());

            this.dvlChiTietDonHang.luu(ctdh);

        } 
        // Reset session và giỏ hàng sau khi thêm đơn thành công
        session.removeAttribute("donHang");
        session.removeAttribute("city");
        session.removeAttribute("district");
        session.removeAttribute("ward");
        session.removeAttribute("fullAdress");
        session.removeAttribute("note");

        khoiTaoGioHang();
        return "client/ordersuccess";

  }

        return "client/orderfail";
    }
     @PostMapping("/apps/checkout")
    private String postThem(@ModelAttribute("alldatakh") DonHang dh,
    @RequestParam("amount") double  orderTotal,
    HttpServletRequest request,
    @RequestParam("city") String city,
    @RequestParam("district") String district,
    @RequestParam("ward") String ward,
    @RequestParam("fullAdress") String fullAdress,
    @RequestParam("note") String note,
    @RequestParam("method") String method
    ){
        if ("banking".equals(method)) {
            session.setAttribute("donHang", dh);
            session.setAttribute("city", city);
            session.setAttribute("district", district);
            session.setAttribute("ward", ward);
            session.setAttribute("fullAdress", fullAdress);
            session.setAttribute("note", note);
// chuyển sang bên vnpay sử lý
            int intValue = (int) orderTotal;
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String  orderInfor ="thông tin đơn hàng ok";
            String vnpayUrl = vnPayService.createOrder(intValue, orderInfor, baseUrl);
            // Xử lý nếu người dùng chọn chuyển khoản    
            return "redirect:" + vnpayUrl;

        } else if ("monkey".equals(method)) {      
        dh.setDiaChi(fullAdress+" ("+ward+" "+district+" "+city+")");
        dh.setGhiChu(note);
        dh.setNgayTao(LocalDate.now());
        dh.setTongTien(tongGiaTriGioHang());
        dh.setThanhtoan(method);
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        if(khachhang_Id==null){
            khachhang_Id =1010101010;
            dh.setMaKH(khachhang_Id);
        }else{
            dh.setMaKH(khachhang_Id);
        }
        var donhang= this.dvlDonHang.luuDonHang(dh);
        int maDon = donhang.getId();
        @SuppressWarnings("unchecked")
        Map<Integer,Integer> cartMap = (Map<Integer,Integer>)session.getAttribute("cart");

        for (Integer maSanPham : cartMap.keySet()) 
        { 
            SanPham sp = dvlSanPham.xem(maSanPham);
            // tongSoSanPham += cartMap.get(maSanPham);
            // var donGiaStr = String.valueOf(sp.getDonGia());
            // float thanhTien = cartMap.get(maSanPham) * sp.getDonGia();
            int soLuong = cartMap.get(maSanPham);
              var ctdh = new ChiTietDonHang();
            ctdh.setDonHangId(maDon);
            ctdh.setMaSP(maSanPham);
            ctdh.setTen(sp.getTenSP());
            ctdh.setModel(sp.getModel());
            ctdh.setDonGia(sp.getDonGia());
            ctdh.setSoLuong(soLuong);
            ctdh.setTongTien(soLuong * sp.getDonGia());
            ctdh.setNgayTao(LocalDate.now());
            ctdh.setNgaySua(LocalDate.now());

            this.dvlChiTietDonHang.luu(ctdh);

        } 
         System.out.println("Phương thức thanh toán: Tiền mặt");   
          khoiTaoGioHang();
        return "redirect:/";           
          
        }
        khoiTaoGioHang();
        return "redirect:/";
    }

    private void khoiTaoGioHang()
    {
        session.setAttribute("cart", new HashMap<Integer,Integer>());
        session.setAttribute("SoSanPhamTrongGioHang", 0);
    }
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
  
}
