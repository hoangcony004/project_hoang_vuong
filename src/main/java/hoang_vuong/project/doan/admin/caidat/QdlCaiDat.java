package hoang_vuong.project.doan.admin.caidat;

import java.util.List;

// Thư viện web: Java Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import hoang_vuong.project.doan.qdl.Qdl;

@Controller
public class QdlCaiDat {
    @Autowired
    private DvlCaiDat dvl;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Controller
    @RequestMapping("/admin")
    public class adminController {

        @GetMapping({
                "/cai-dat",
                "/cai-dat/duyet"
        })
        public String getDuyet(Model model) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/admin/dang-nhap";

            List<CaiDat> list = dvl.duyetCaiDat();

            model.addAttribute("ds", list);

            model.addAttribute("title", "Quản Lý Cài Đặt");

            model.addAttribute("content", "admin/caidat/duyet.html");
            return "layouts/layout-admin.html";
        }

        @GetMapping("/cai-dat/them")
        public String getThem(Model model) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/admin/dangnhap";

            CaiDat dl = new CaiDat();

            model.addAttribute("dl", dl);
            model.addAttribute("content", "caidat/them.html");

            return "layouts/layout-admin.html";
        }

        @PostMapping("/cai-dat/them")
        public String postThem(@ModelAttribute("CaiDat") CaiDat dl) {
            if (Qdl.NhanVienChuaDangNhap(request))
                return "redirect:/qdl/nhanvien/dangnhap";

            dvl.luuCaiDat(dl);
            session.setAttribute("message", "Đã hoàn tất việc thêm mới !");

            return "redirect:/qdl/caidat/duyet";
        }

    }

    @GetMapping("/caidat/sua")
    public String getSua(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        CaiDat dl = dvl.xemCaiDat(id);

        model.addAttribute("dl", dl);
        model.addAttribute("content", "caidat/sua.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/caidat/xoa")
    public String getXoa(Model model, @RequestParam(value = "id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        CaiDat dl = dvl.timCaiDatTheoId(id);

        model.addAttribute("dl", dl);
        model.addAttribute("content", "caidat/xoa.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/qdl/caidat/xem/{id}")
    public String getXem(Model model, @PathVariable(value = "id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        // Lấy ra bản ghi theo id
        CaiDat dl = dvl.xemCaiDat(id);

        // Gửi đối tượng dữ liệu sang bên view
        model.addAttribute("dl", dl);

        // Hiển thị view giao diện
        // Nội dung riêng của trang...
        model.addAttribute("content", "caidat/xem.html");

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html";
    }

    @PostMapping("/qdl/caidat/sua")
    public String postSua(@ModelAttribute("CaiDat") CaiDat dl) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        dvl.luuCaiDat(dl);

        // Gửi thông báo thành công từ view Add/Edit sang view List
        session.setAttribute("message", "Đã hoàn tất việc cập nhật !");

        return "redirect:/qdl/caidat/duyet";
    }

    @PostMapping("/qdl/caidat/xoa")
    public String postXoa(Model model, @RequestParam("Id") int id, RedirectAttributes redirectAttributes)
    // public String postXoa(Model model, @RequestParam("Id") int id)
    {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        // Xoá dữ liệu
        this.dvl.xoaCaiDat(id);
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");

        // Điều hướng sang trang giao diện
        return "redirect:/qdl/caidat/duyet";
    }

}
