package srp;

public class ReportManager {
    private final ReportData data;  // ← final

    public ReportManager(ReportData data) {
        this.data = data;
    }

    public void generateReport() {
        System.out.println(" Менеджер отчетов: Генерация отчета - " + data.getData());
    }
}