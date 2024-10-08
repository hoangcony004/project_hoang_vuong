package hoang_vuong.project.doan.client.danhgiasanpham;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DanhGiaSanPhamService 
//implements DviDanhGiaSanPham // cũng không cần thiết phải có một interface cho lớp này
{
    @Autowired private DanhGiaSanPhamRepository kdl;// kho dữ liệu;

    // @Override 
    public List<DanhGiaSanPham> dsDanhGiaSanPham() // getAllDanhGiaSanPham()
    {
        return kdl.findAll();
    }

    // @Override // duyệt
    public List<DanhGiaSanPham>  duyet() 
    {
        return kdl.findAll();
    }

    // @Override // tìm theo mã Id
    public DanhGiaSanPham  timTheoId(int id)// 
    {
        DanhGiaSanPham dl = null;

        Optional<DanhGiaSanPham> optional = kdl.findById(id);

        if// nếu
        (optional.isPresent()) // tìm thấy bản ghi trong kho
        {
            dl = optional.get();
        } else// ngược lại
        {
            //throw new RuntimeException("Không tìm thấy thú cưng ! Ko tim thay thu cung !");
        }

        return dl;

    }

    // @Override 
    public DanhGiaSanPham xem(int id)// 
    {

        DanhGiaSanPham dl = null;

        Optional<DanhGiaSanPham> optional = kdl.findById(id);

        if// nếu
        (optional.isPresent()) // tìm thấy bản ghi trong kho
        {
            dl = optional.get();
        } else// ngược lại
        {
            //throw new RuntimeException("Không tìm thấy thú cưng ! Ko tim thay thu cung !");
        }

        return dl;

    }

    // @Override
    public void luu(DanhGiaSanPham dl)
    {
        this.kdl.save(dl);
    }

    public void them(DanhGiaSanPham dl)
    {
        this.kdl.save(dl);
    }

    // public void themDanhGia(int khachhangId, int id, int starRating, String nhanXet) {
    //     // Tạo đối tượng đánh giá mới
    //     DanhGiaSanPham danhGia = new DanhGiaSanPham();
    //     danhGia.setKhachHang(khachhangId);
    //     danhGia.setSanPham(id);
    //     danhGia.setStarRating(starRating);
    //     danhGia.setNhanXet(nhanXet);

    //     // Lưu đánh giá vào cơ sở dữ liệu
    //     kdl.save(danhGia);
    // }

    public void sua(DanhGiaSanPham dl)
    {
        this.kdl.save(dl);
    }

    // @Override
    public void xoa(int id)
    {
        this.kdl.deleteById(id);
    }

}


