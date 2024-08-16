package hoang_vuong.project.doan.admin.nhanvien;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class DvlNhanVien 
{
    @Autowired private KdlNhanVien kdl;

    @Autowired
    private KdlNhanVien nhanVienRepository;

    public Page<NhanVien> duyetNhanVien(Pageable pageable) {
        return nhanVienRepository.findAll(pageable);
    }

    public List<NhanVien> timKiemTheoTen(String ten) {
        return kdl.findByTenDayDuContainingIgnoreCase(ten);
    }

    public List<NhanVien> timKiemTheoEmail(String email) {
        return kdl.findByEmailContainingIgnoreCase(email);
    }

    public List<NhanVien> timKiemTheoDienThoai(String dienThoai) {
        return kdl.findByDienThoaiContainingIgnoreCase(dienThoai);
    }

    public List<NhanVien> dsNhanVien()
    {
        return kdl.findAll();
    }

    public List<NhanVien>  duyetNhanVien() 
    {
        return kdl.findAll();
    }

    public NhanVien  timNhanVienTheoId(int id)// 
    {
        NhanVien dl = null;

        Optional<NhanVien> optional = kdl.findById(id);

        if
        (optional.isPresent())
        {
            dl = optional.get();
        } else
        {
        
        }

        return dl;

    }

    public NhanVien xemNhanVien(int id)
    {

        NhanVien dl = null;

        Optional<NhanVien> optional = kdl.findById(id);

        if
        (optional.isPresent())
        {
            dl = optional.get();
        } else
        {
            
        }

        return dl;

    }

        public void luuNhanVien(NhanVien dl)
    {
        this.kdl.save(dl);
    }

        public void xoaNhanVien(int id)
    {
        this.kdl.deleteById(id);
    }

        public NhanVien timNhanVienTheoTenDangNhap(String tdn)
    {
        NhanVien dl = null;

        dl = kdl.findOneByTenDangNhap(tdn);

        return dl;
    }

        public Boolean DaCoTenDangNhap(String tdn)
    {
        return kdl.existsByTenDangNhap(tdn);
    }



}
