import dip.*;
import isp.*;
import lsp.*;
import ocp.*;
import srp.*;

public class Main {
    @SuppressWarnings("java:S106")
    public static void main(String[] args) {
        System.out.println("🚀 ДЕМОНСТРАЦИЯ ПРИНЦИПОВ SOLID");
        System.out.println("=" .repeat(50));

        // DIP демонстрация
        System.out.println("\n🔄 ПРИНЦИП DIP (Dependency Inversion)");
        System.out.println("-".repeat(40));

        MessageSender emailSender = new EmailSender();
        NotificationService emailService = new NotificationService(emailSender);
        emailService.sendNotification("Ваш заказ готов!");

        MessageSender smsSender = new SmsSender();
        NotificationService smsService = new NotificationService(smsSender);
        smsService.sendNotification("Ваш код подтверждения: 123456");

        // ISP демонстрация
        System.out.println("\n🔌 ПРИНЦИП ISP (Interface Segregation)");
        System.out.println("-".repeat(40));

        Printer oldPrinter = new OldPrinter();
        oldPrinter.print();

        // LSP демонстрация
        System.out.println("\n🦅 ПРИНЦИП LSP (Liskov Substitution)");
        System.out.println("-".repeat(40));

        Bird sparrow = new Sparrow();
        Bird penguin = new Penguin();

        sparrow.eat();
        penguin.eat();

        // OCP демонстрация
        System.out.println("\n💳 ПРИНЦИП OCP (Open/Closed)");
        System.out.println("-".repeat(40));

        DiscountCalculator calculator = new DiscountCalculator();
        double price = 100.0;

        calculator.setDiscountStrategy(new RegularDiscount());
        System.out.println("Обычная скидка: " + calculator.calculate(price));

        calculator.setDiscountStrategy(new VipDiscount());
        System.out.println("VIP скидка: " + calculator.calculate(price));

        calculator.setDiscountStrategy(new SuperVipDiscount());
        System.out.println("Super VIP скидка: " + calculator.calculate(price));

        // SRP демонстрация
        System.out.println("\n📋 ПРИНЦИП SRP (Single Responsibility)");
        System.out.println("-".repeat(40));

        ReportData report = new ReportData("Финансовый отчет");
        ReportManager manager = new ReportManager(report);
        ReportPrinter printer = new ReportPrinter(report);
        ReportSaver saver = new ReportSaver(report);

        manager.generateReport();
        printer.printReport();
        saver.saveReport();

        System.out.println("\n" + "=" .repeat(50));
        System.out.println("✅ ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА!");
    }
}