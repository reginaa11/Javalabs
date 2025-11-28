package srp;

public class ReportManager {
    private final ReportData data;

    public ReportManager(ReportData data) {
        this.data = data;
    }

    @SuppressWarnings("java:S106")
    public void generateReport() {
        System.out.println("📈 Генерация отчета: " + data.getData());
    }
}