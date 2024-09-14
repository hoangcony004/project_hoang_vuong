package hoang_vuong.project.doan.admin.khachhang;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class KhachHangService {
    @Autowired
    private KhachHangRepository kdl;

    @Autowired
    private KhachHangRepository KhachHangRepository;

    public Page<KhachHang> duyetKhachHang(Pageable pageable) {
        return KhachHangRepository.findAll(pageable);
    }

    // Phương thức lấy số lượng khách hàng đăng ký trong tháng hiện tại
    public long getNumberOfCustomersThisMonth() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(startOfMonth.lengthOfMonth());
        return kdl.countByNgayTaoBetween(startOfMonth, endOfMonth);
    }

    // Phương thức lấy số lượng khách hàng đăng ký trong tháng trước
    public long getNumberOfCustomersLastMonth() {
        LocalDate startOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(startOfLastMonth.lengthOfMonth());
        return kdl.countByNgayTaoBetween(startOfLastMonth, endOfLastMonth);
    }

    // Phương thức tính tỷ lệ phần trăm thay đổi
    public double calculateChangePercentage(long currentMonthCount, long previousMonthCount) {
        if (previousMonthCount == 0) {
            return currentMonthCount > 0 ? 100.0 : 0.0;
        }
        return ((double) (currentMonthCount - previousMonthCount) / previousMonthCount) * 100;
    }

    public List<KhachHang> timKiemTheoTen(String ten) {
        return kdl.findByTenDayDuContainingIgnoreCase(ten);
    }

    public List<KhachHang> timKiemTheoEmail(String email) {
        return kdl.findByEmailContainingIgnoreCase(email);
    }

    public List<KhachHang> timKiemTheoDienThoai(String dienThoai) {
        return kdl.findByDienThoaiContainingIgnoreCase(dienThoai);
    }

    public List<KhachHang> timKhachHangTheoTenDN(String tenDangNhap) {
        return kdl.findByTenDangNhap(tenDangNhap);
    }

    public List<KhachHang> dsKhachHang() {
        return kdl.findAll();
    }

    public List<KhachHang> duyetKhachHang() {
        return kdl.findAll();
    }
    public KhachHang timEmmail(String tdn){
        KhachHang dl = null;
        dl = kdl.findOneByEmail(tdn);
        return dl;
    }
    public KhachHang timSodienthoai(String tdn){
        KhachHang dl = null;
        dl = kdl.findOneByDienThoai(tdn);
        return dl;
    }

    public KhachHang timKhachHangTheoId(int id)//
    {
        KhachHang dl = null;

        Optional<KhachHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public KhachHang xemKhachHang(int id) {

        KhachHang dl = null;

        Optional<KhachHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public void luuKhachHang(KhachHang dl) {
        this.kdl.save(dl);
    }

    public void xoaKhachHang(int id) {
        this.kdl.deleteById(id);
    }

    public KhachHang timKhachHangTheoTenDangNhap(String tdn) {
        KhachHang dl = null;

        dl = kdl.findOneByTenDangNhap(tdn);

        return dl;
    }

    public Boolean DaCoTenDangNhap(String tdn) {
        return kdl.existsByTenDangNhap(tdn);
    }
    public boolean dacoemail(String email){
        return kdl.existsByEmail(email);
    }
    public boolean dacosdt(String email){
        return kdl.existsByDienThoai(email);
    }

}
