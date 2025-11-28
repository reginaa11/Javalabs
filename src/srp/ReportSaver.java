package srp;

public class ReportSaver {
    private final ReportData data;

    public ReportSaver(ReportData data) {
        this.data = data;
    }

    @SuppressWarnings("java:S106")
    public void saveReport() {
        System.out.println("💾 Сохранение отчета: " + data.getData());
    }
}