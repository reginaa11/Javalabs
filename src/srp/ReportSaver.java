package srp;

public class ReportSaver {
    private final ReportData data;  // ← добавлено final

    public ReportSaver(ReportData data) {
        this.data = data;
    }

    public void saveReport() {
        System.out.println(" Сохранятель отчетов: Сохранение отчета - " + data.getData());
    }
}