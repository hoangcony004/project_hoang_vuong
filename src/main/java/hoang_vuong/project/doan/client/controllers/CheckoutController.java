package hoang_vuong.project.doan.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import hoang_vuong.project.doan.qdl.Qdl;

@Controller
public class CheckoutController {
    @Autowired
    private HttpSession session;

    @Autowired
    private KhachHangService kh_dl;
    
    @Autowired
    private SanPhamService dvlSanPham;

    @GetMapping("/apps/checkout")
    public String getGioHangThanhToan(Model model, HttpServletRequest request) {
        if// nếu không có sản phẩm
        (!gioHangCoSanPham()) {
            model.addAttribute("content", "client/cart.html");
            return "layouts/layout-client.html"; // layout.html
        }
        int maKhachHang = 1;
        // Kiểm tra xem, khách hàng là vãng lai(anonymous), hay thành viên (membership)
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        // vãng lai
        if (Qdl.KhachHangChuaDangNhap(request)) {
            maKhachHang = 0;
        } else {
            maKhachHang = khachhang_Id;
            var kh = kh_dl.xemKhachHang(khachhang_Id);
            model.addAttribute("alldatakh", kh);
        }

        // Nếu khách đăng nhập rồi (session.CUS_LOGGED)
        // thì cập nhật lại giá trị cho maKhachHang

        // để còn gán mã khách hàng vào form thanh toán
        // var dh = new DonHang();

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        List<Map<String, String>> cartData = new ArrayList<Map<String, String>>();

        for (Integer maSanPham : cartMap.keySet()) {
            // System.out.println(key + ":" + map.get(key));
            // tongSoSanPham += cartMap.get(maSanPham);
            SanPham sp = dvlSanPham.xem(maSanPham);
            var donGiaStr = String.valueOf(sp.getDonGia());
            float thanhTien = cartMap.get(maSanPham) * sp.getDonGia();

            Map<String, String> map = new HashMap<>();
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

        // model.addAttribute("dl", dh); // gửi dữ liệu khách hàng sang
        // gửi dữ liệu khách hàng sang
        model.addAttribute("content", "client/checkout.html");
        // return "client/checkout.html";
        // ...được đặt vào bố cục chung của toàn website
        return "layouts/layout-client.html"; // layout.html

    }

    private boolean gioHangCoSanPham() {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cartMap == null || cartMap.isEmpty())
            return false;

        return true;
    }

    private String tongGiaTriGioHangVi() {
        return String.format("%,.0f", tongGiaTriGioHang());
    }

    private float tongGiaTriGioHang()// cartGetTotal()
    {

        if// nếu không có sản phẩm
        (!gioHangCoSanPham())
            return 0;

        int tong = 0;
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        for (Integer maSanPham : cartMap.keySet()) {
            SanPham sp = dvlSanPham.xem(maSanPham);
            int soluong = cartMap.get(maSanPham);
            tong += sp.getDonGia() * soluong;
        }

        return tong;
    }
}
