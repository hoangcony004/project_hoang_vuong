package hoang_vuong.project.doan.admin.donhang;

import java.util.Map;

public class RevenueModel {
    private Map<String, Float> monthlyRevenue;

    public RevenueModel(Map<String, Float> monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Map<String, Float> getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(Map<String, Float> monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }
}
