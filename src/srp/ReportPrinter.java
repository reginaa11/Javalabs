package srp;

public class ReportPrinter {
    private final ReportData data;  // ← final

    public ReportPrinter(ReportData data) {
        this.data = data;
    }

    public void printReport() {
        System.out.println("  Принтер отчетов: Печать отчета - " + data.getData());
    }
}