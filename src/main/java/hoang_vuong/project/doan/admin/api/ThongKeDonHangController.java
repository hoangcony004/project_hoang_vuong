package hoang_vuong.project.doan.admin.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hoang_vuong.project.doan.admin.donhang.DonHangService;

@RestController
@RequestMapping("/api/")
public class ThongKeDonHangController {
    @Autowired
    private DonHangService donHangService;

    @GetMapping("/don-hang/count")
    public ResponseEntity<Map<Integer, Long>> getOrderStatistics() {
        Map<Integer, Long> orderStats = donHangService.getOrderCountByYear();
        return ResponseEntity.ok(orderStats);
    }
}

