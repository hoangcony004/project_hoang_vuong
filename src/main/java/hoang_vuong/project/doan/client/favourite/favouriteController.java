package hoang_vuong.project.doan.client.favourite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class favouriteController {
      @Autowired
    private favouriteService dvl;
        @GetMapping("/apps/favourite")
    public ResponseEntity<List<favourite>> getFavouritesByUserId(HttpSession session) {
        Integer khachhang_Id = (Integer) session.getAttribute("khachhang_Id");
        List<favourite> favourites = dvl.getFavouritesByUserId(khachhang_Id);
        return new ResponseEntity<>(favourites, HttpStatus.OK);
    }
}
