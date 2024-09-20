package hoang_vuong.project.doan.admin.donhang;

import java.text.DecimalFormat;

public class DoanhThuThang {
    private Double tongTien; // Thay Float bằng Double
    private int day; // Thêm thuộc tính day
    private int month;
    private int year;

    // Constructor với kiểu Double
    public DoanhThuThang(Double tongTien, int day, int month, int year) {
        this.tongTien = tongTien;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // Getters và Setters cho tongTien
    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    // Getters và Setters cho day
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    // Getters và Setters cho month
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    // Getters và Setters cho year
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Phương thức định dạng số tiền
    public String getFormattedTongTien() {
        DecimalFormat df = new DecimalFormat("#,###" + " vn₫"); // Định dạng với dấu phẩy ngăn cách hàng nghìn
        return df.format(tongTien);
    }

    @Override
    public String toString() {
        return "DoanhThuThang{" +
                "tongTien=" + tongTien +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
