package hoang_vuong.project.doan.admin.chitietdonhang;

import java.text.DecimalFormat;

public class ThongKeSanPhamDTO {
    private String tenSP;
    private Float donGia;
    private Long soLuongBan;
    private Float tongTien;

    // Constructor
    public ThongKeSanPhamDTO(String tenSP, Float donGia, Long soLuongBan, Float tongTien) {
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.soLuongBan = soLuongBan;
        this.tongTien = tongTien;
    }

    // Getter và Setter cho tenSP
    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    // Getter và Setter cho donGia
    public Float getDonGia() {
        return donGia;
    }

    public void setDonGia(Float donGia) {
        this.donGia = donGia;
    }

    // Getter và Setter cho soLuongBan
    public Long getSoLuongBan() {
        return soLuongBan;
    }

    public void setSoLuongBan(Long soLuongBan) {
        this.soLuongBan = soLuongBan;
    }

    // Getter và Setter cho tongTien
    public Float getTongTien() {
        return tongTien;
    }

    public void setTongTien(Float tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public String toString() {
        return "ThongKeSanPhamDTO{" +
                "tenSP='" + tenSP + '\'' +
                ", donGia=" + donGia +
                ", soLuongBan=" + soLuongBan +
                ", tongTien=" + tongTien +
                '}';
    }

    // Format the currency to avoid scientific notation
    public String getFormattedDonGia() {
        DecimalFormat df = new DecimalFormat("#,###" + " vn₫");
        return df.format(donGia);
    }

    public String getFormattedTongTien() {
        DecimalFormat df = new DecimalFormat("#,###" + " vn₫");
        return df.format(tongTien);
    }
}
