package srp;

public class ReportPrinter {
    private final ReportData data;

    public ReportPrinter(ReportData data) {
        this.data = data;
    }

    @SuppressWarnings("java:S106")
    public void printReport() {
        System.out.println("🖨️  Печать отчета: " + data.getData());
    }
}