package hoang_vuong.project.doan.qdl;

import org.springframework.web.bind.annotation.ControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class Qdl
{
    public static Boolean 
    NhanVienChuaDangNhap(HttpServletRequest request) 
    {
        if
        (request.getSession().getAttribute("NhanVien_Id") == null) 
        {
            request.getSession().setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());
            // rồi điều hướng sang trang đăng nhập
            return true;
        }
        return false;
    }

}

