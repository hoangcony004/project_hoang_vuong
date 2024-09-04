package hoang_vuong.project.doan.client.trangchu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import hoang_vuong.project.doan.admin.khachhang.KhachHangService;
import hoang_vuong.project.doan.admin.sanpham.SanPham;
import hoang_vuong.project.doan.admin.sanpham.SanPhamService;
import org.springframework.http.MediaType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class giohangController {

    @Autowired
    private HttpSession session;
    @Autowired
  private  SanPhamService dvlSanPham;
    @GetMapping("/giohang/ajax/get-html")
    public String getGioHangAjax(Model model) 
    {
        // if//nếu không có sản phẩm
        // (!gioHangCoSanPham())
        //     return "client/giohang-trong.html";

       

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
            
            map.put("donGiaVi", String.format("%,.0f", sp.getDonGia()));
            map.put("anh", sp.getAnhDaiDien());
            map.put("soluong", String.valueOf(cartMap.get(maSanPham)));
            map.put("thanhTien", String.format("%,.0f", thanhTien));

            cartData.add(map);
        }
    
        // Gửi dữ liệu giỏ hàng sang bên View
        model.addAttribute("cartData", cartData);
        // model.addAttribute("tongGiaTriGioHang", tongGiaTriGioHang());
        // model.addAttribute("tongGiaTriGioHangVi", tongGiaTriGioHangVi());
        
        return "client/cart.html";
        
    }
    @PostMapping(path = "/giohang/them/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postGioHangThemMoi(Model model, 
        @RequestParam("id_sanpham") int id, 
        @RequestParam("soluong") int soluong,
        @RequestParam("ten") String ten
    ) 
    {

        // Giỏ hàng là 1 tập hợp, mỗi phần tử là một cặp
        // <mã_sản_phẩm --> số_lượng>
        if(session.getAttribute("cart")==null)
        {
            session.setAttribute("cart", new HashMap<Integer,Integer>());
            session.setAttribute("SoSanPhamTrongGioHang", 0);
        }

        @SuppressWarnings("unchecked")
        Map<Integer,Integer> cartMap = (Map<Integer,Integer>)session.getAttribute("cart");
        Integer cartQuantity = (Integer)session.getAttribute("SoSanPhamTrongGioHang");

        if// nếu sản phẩm đã có trong giỏ hàng, thì cộng thêm số lượng
        (cartMap.containsKey(id))
        {
            cartMap.put(id, cartMap.get(id)+soluong);
        }
        else{
            cartMap.put(id, soluong);
        }
        cartQuantity += soluong;

        // Cập nhật giỏ hàng trong Session
        session.setAttribute("cart", cartMap);

        System.out.println("Đã thêm mới vào giỏ hàng sản phẩm id: "+id);
        session.setAttribute("SoSanPhamTrongGioHang", cartQuantity);

        // Gửi dữ liệu json trở lại cho View
        Map<String, Object> data = new HashMap<>();
        data.put("success", "Đã thêm thành công vào giỏ hàng sản phẩm "+ten);
       // data.put("total", demSanPhamTrongGioHang());// tạm thôi

        // Nếu mà thất bại trong việc thêm vào giỏ hàng
        //data.put("redirect", "đường dẫn đến thông tin sản phẩm");// "/product-info.php?product_id={$_POST['product_id']}";

        return new ResponseEntity<>(data, HttpStatus.OK);
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
    @GetMapping("/apps/cart")
    public String getGioHangChiTiet(Model model) 
    {
        if//nếu không có sản phẩm
        (!gioHangCoSanPham())
        {
            model.addAttribute("content", "client/cart.html"); 

            // ...được đặt vào bố cục chung của toàn website
            return "layouts/layout-client.html"; // layout.html
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
        model.addAttribute("tongGiaTriGioHangVi", tongGiaTriGioHangVi());
        
        model.addAttribute("content", "client/cart.html"); 

        // ...được đặt vào bố cục chung của toàn website
        return "layouts/layout-client.html"; // layout.html
        
    }

    @PostMapping(path = "/giohang/sua/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postGioHangSuaAjax(Model model, 
        @RequestParam("id_sanpham") int id, 
        @RequestParam("so_luong") int soluong,
        @RequestParam("ten") String ten
    ) 
    {

        // Giỏ hàng là 1 tập hợp, mỗi phần tử là một cặp
        // <mã_sản_phẩm --> số_lượng>
        if(session.getAttribute("cart")==null)
        {
            session.setAttribute("cart", new HashMap<Integer,Integer>());
            session.setAttribute("SoSanPhamTrongGioHang", 0);
        }

        @SuppressWarnings("unchecked")
        Map<Integer,Integer> cartMap = (Map<Integer,Integer>)session.getAttribute("cart");
        Integer cartQuantity = (Integer)session.getAttribute("SoSanPhamTrongGioHang");

        
        if// nếu sản phẩm đã có trong giỏ hàng, thì tiến hành cập nhật số lượng mới của nó cũng như của toaàn giỏ hàng
        (cartMap.containsKey(id))
        {
            int so_luong_cu = cartMap.get(id);
            cartMap.put(id, soluong);
            cartQuantity += (soluong - so_luong_cu);
        }
        session.setAttribute("SoSanPhamTrongGioHang", cartQuantity);

        // Cập nhật giỏ hàng trong Session
        session.setAttribute("cart", cartMap);
        session.setAttribute("SoSanPhamTrongGioHang", cartQuantity);


        System.out.println("Đã cập nhật giỏ hàng với sản phẩm id: "+id);

        // Gửi dữ liệu json trở lại cho View
        Map<String, Object> data = new HashMap<>();
        data.put("success", "Đã cập nhật giỏ hàng, số lượng mới cho sản phẩm "+ten);

        // Nếu mà thất bại trong việc thêm vào giỏ hàng
        //data.put("redirect", "đường dẫn đến thông tin sản phẩm");// "/product-info.php?product_id={$_POST['product_id']}";

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(path = "/giohang/xoa/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postGioHangXoaAjax(Model model, 
        @RequestParam("id_sanpham") int id, 
        @RequestParam("ten") String ten
    ) 
    {

        // Giỏ hàng là 1 tập hợp, mỗi phần tử là một cặp
        // <mã_sản_phẩm --> số_lượng>
        if(session.getAttribute("cart")==null)
        {
            session.setAttribute("cart", new HashMap<Integer,Integer>());
            session.setAttribute("SoSanPhamTrongGioHang", 0);
        }

        @SuppressWarnings("unchecked")
        Map<Integer,Integer> cartMap = (Map<Integer,Integer>)session.getAttribute("cart");
        Integer cartQuantity = (Integer)session.getAttribute("SoSanPhamTrongGioHang");


        if// nếu sản phẩm đã có trong giỏ hàng, thì tiến hành gỡ bỏ và cập nhật lại thông tin số lượng
        (cartMap.containsKey(id))
        {
            int so_luong_cu = cartMap.get(id);
            cartMap.remove(id);
            cartQuantity -= so_luong_cu;
        }


        // Cập nhật giỏ hàng trong Session
        session.setAttribute("cart", cartMap);
        session.setAttribute("SoSanPhamTrongGioHang", cartQuantity);

        System.out.println("Đã xóa bỏ giỏ hàng sản phẩm id: "+id);

        // Gửi dữ liệu json trở lại cho View
        Map<String, Object> data = new HashMap<>();
        data.put("success", "Đã xóa thành công khỏi giỏ hàng sản phẩm "+ten);

        // Nếu mà thất bại trong việc thêm vào giỏ hàng
        //data.put("redirect", "đường dẫn đến thông tin sản phẩm");// "/product-info.php?product_id={$_POST['product_id']}";

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}