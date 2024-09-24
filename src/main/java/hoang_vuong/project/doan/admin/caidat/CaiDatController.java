package hoang_vuong.project.doan.admin.caidat;

import java.time.LocalDate;
import java.util.List;

// Thư viện web: Java Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import hoang_vuong.project.doan.admin.lienhe.LienHe;
import hoang_vuong.project.doan.qdl.Qdl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/admin")
public class CaiDatController {
    @Autowired
    private CaiDatService dvl;

    @Autowired
    private HttpServletRequest request;

    // v1
    @GetMapping({ "/cai-dat" })
    public String getDuyet(Model model) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        List<CaiDat> list = dvl.duyetCaiDat();

        model.addAttribute("ds", list);
        model.addAttribute("dl", new CaiDat());
        model.addAttribute("title", "Quản Lý Cài Đặt");
        model.addAttribute("title_duyet", "Cài Đặt");
        model.addAttribute("title_btn_add", "Thêm Cài Đặt");
        model.addAttribute("title_body", "Thêm Cài Đặt");
        model.addAttribute("action", "/admin/cai-dat/them");
        model.addAttribute("title_sm", "Thêm mới");
        model.addAttribute("content", "admin/caidat/duyet.html");

        return "layouts/layout-admin.html";
    }

    // v2
    // @GetMapping({ "/cai-dat" })
    // public String getDuyet(@RequestParam(defaultValue = "1") int page,
    // @RequestParam(defaultValue = "5") int size,
    // Model model) {
    // if (Qdl.NhanVienChuaDangNhap(request))
    // return "redirect:/admin/dang-nhap";

    // Pageable pageable = PageRequest.of(page - 1, size);
    // Page<CaiDat> pageCaiDat = dvl.pageCaiDat(pageable);

    // // Tạo đường link phân trang
    // String paginationLink = "/admin/cai-dat?page=" + page + "&size=" + size;

    // model.addAttribute("ds", pageCaiDat.getContent());
    // model.addAttribute("currentPage", page);
    // model.addAttribute("totalPages", pageCaiDat.getTotalPages());
    // model.addAttribute("totalItems", pageCaiDat.getTotalElements());
    // model.addAttribute("paginationLink", paginationLink);

    // model.addAttribute("dl", new CaiDat());
    // model.addAttribute("title", "Quản Lý Cài Đặt");
    // model.addAttribute("title_duyet", "Cài Đặt");
    // model.addAttribute("title_btn_add", "Thêm Cài Đặt");
    // model.addAttribute("title_body", "Thêm Cài Đặt");
    // model.addAttribute("action", "/admin/cai-dat/them");
    // model.addAttribute("title_sm", "Thêm mới");
    // model.addAttribute("content", "admin/caidat/duyet.html");

    // return "layouts/layout-admin.html";
    // }

    @PostMapping("/cai-dat/them")
    public String postAdd(@ModelAttribute("CaiDat") CaiDat dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        try {
            dvl.luuCaiDat(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã thêm mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể thêm mới. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/cai-dat";
    }

    @GetMapping("/cai-dat/sua")
    public String getEdit(Model model, @RequestParam("id") int id) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        var dl = dvl.xemCaiDat(id);
        model.addAttribute("title_body", "Sửa Cài Đặt");
        model.addAttribute("title_sm", "Cập nhật");
        model.addAttribute("dl", dl);
        model.addAttribute("action", "/admin/cai-dat/sua");

        return "admin/caidat/form-bs4-caidat.html";
    }

    @PostMapping("/cai-dat/sua")
    public String postEdit(@ModelAttribute("CaiDat") CaiDat dl,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/admin/dang-nhap";

        try {
            dvl.luuCaiDat(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã sửa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR", "Không thể sửa. Mã lỗi: " + e.getMessage());
        }

        return "redirect:/admin/cai-dat";
    }

    @PostMapping("/cai-dat/xoa")
    public String postDelete(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NhanVienChuaDangNhap(request))
            return "redirect:/qdl/nhanvien/dangnhap";

        // Xoá dữ liệu
        try {
            this.dvl.xoaCaiDat(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_SUCCESS", "Đã xóa thành công !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_ERROR",
                    "Không thể xóa. Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/cai-dat";
    }
}