package myatt.model;

public class Reports {
    private String reportMonth;
    private int total;

    /**
     * Constructs a Reports object with the total and report month.
     *
     * @param total the total appointments
     * @param reportMonth the month the report is generating.
     */
    public Reports(int total, String reportMonth) {
        this.total = total;
        this.reportMonth = reportMonth;
    }
    public String getReportMonth() {
        return reportMonth;
    }
    public void setReportMonth(String reportMonth) {
        this.reportMonth = reportMonth;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

}
