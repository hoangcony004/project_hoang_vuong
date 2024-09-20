package hoang_vuong.project.doan.admin.chitietdonhang;

import hoang_vuong.project.doan.admin.sanpham.SanPham;

public class SanPhamBanChayDTO {
    private SanPham sanPham;
    private Long soLuongDaBan;

    public SanPhamBanChayDTO(SanPham sanPham, Long soLuongDaBan) {
        this.sanPham = sanPham;
        this.soLuongDaBan = soLuongDaBan;
    }

    // Getter và setter
    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public Long getSoLuongDaBan() {
        return soLuongDaBan;
    }

    public void setSoLuongDaBan(Long soLuongDaBan) {
        this.soLuongDaBan = soLuongDaBan;
    }

    @Override
    public String toString() {
        return "SanPhamBanChayDTO{" +
                "sanPham=" + sanPham.getTenSP() + // giả sử SanPham có phương thức getTenSanPham()
                ", soLuongDaBan=" + soLuongDaBan +
                '}';
    }
}
