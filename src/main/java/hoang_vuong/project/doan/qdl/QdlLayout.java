package hoang_vuong.project.doan.qdl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import hoang_vuong.project.doan.admin.caidat.CaiDatService;

@ControllerAdvice
public class QdlLayout {
    @Autowired
    CaiDatService dvlCaiDat;

    @ModelAttribute("testGlobalText")
    public String getTest() {
        return "THIS IS A GLOBAL TEXT for All *.html(s), really cool ";
    }

    @ModelAttribute("caiDatTinh")
    public HashMap<String, String> getCaiDatTinh() {
        var map = new HashMap<String, String>();
        map.put("staticAppNam", "Web Shop Blog");
        map.put("staticTel", "19001010");

        return map;
    }

    @ModelAttribute("caidat")
    public HashMap<String, String> getCaiDat(Model model) {
        var map = new HashMap<String, String>();

        var list = dvlCaiDat.duyetCaiDat();
        for (var obj : list) {
            map.put(obj.getKhoa(), obj.getGiaTri());
        }
        return map;
    }

}