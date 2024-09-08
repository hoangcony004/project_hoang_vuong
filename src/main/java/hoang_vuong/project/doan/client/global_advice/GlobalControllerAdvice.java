package hoang_vuong.project.doan.client.global_advice;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAttributes(HttpSession session, Model model) {
         Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        String khachhang_Email = (String) session.getAttribute("khachhang_Email");
        Integer cartQuantity = (Integer) session.getAttribute("SoSanPhamTrongGioHang");
        if (cartQuantity == null) {
            cartQuantity = 0;
        }
        model.addAttribute("khachhang_Id", khachhang_Id);
        model.addAttribute("khachhang_Email", khachhang_Email);
        model.addAttribute("SoSanPhamTrongGioHang", cartQuantity);
    }
}