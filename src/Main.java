import lsp.*;
import ocp.*;
import srp.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(" ДЕМОНСТРАЦИЯ ПРИНЦИПОВ SOLID");
        System.out.println("=" .repeat(50));

        // Демонстрация LSP
        System.out.println("\n ПРИНЦИП LSP (LISKOV SUBSTITUTION)");
        System.out.println("-".repeat(40));

        Bird eagle = new Eagle();
        Bird sparrow = new Sparrow();
        Bird penguin = new Penguin();

        System.out.println("Демонстрация полиморфизма:");
        eagle.eat();
        sparrow.eat();
        penguin.eat();

        System.out.println("\nДемонстрация специфического поведения:");
        // Используем летающих птиц
        FlyingBird flyingEagle = new Eagle();
        FlyingBird flyingSparrow = new Sparrow();

        flyingEagle.fly();
        flyingSparrow.fly();

        // Пингвин плавает вместо полета
        Penguin swimmingPenguin = new Penguin();
        swimmingPenguin.swim();

        // Демонстрация OCP
        System.out.println("\n ПРИНЦИП OCP (OPEN/CLOSED)");
        System.out.println("-".repeat(40));

        DiscountCalculator calculator = new DiscountCalculator();
        double originalPrice = 100.0;

        System.out.println("Оригинальная цена: " + originalPrice + " руб.");
        System.out.println();

        calculator.setDiscountStrategy(new RegularDiscount());
        System.out.println(" Обычная скидка (5%): " + calculator.calculate(originalPrice) + " руб.");

        calculator.setDiscountStrategy(new VipDiscount());
        System.out.println("VIP скидка (15%): " + calculator.calculate(originalPrice) + " руб.");

        calculator.setDiscountStrategy(new SuperVipDiscount());
        System.out.println(" Super VIP скидка (25%): " + calculator.calculate(originalPrice) + " руб.");

        // Демонстрация SRP
        System.out.println("\n ПРИНЦИП SRP (SINGLE RESPONSIBILITY)");
        System.out.println("-".repeat(40));

        ReportData reportData = new ReportData("Ежемесячный финансовый отчет за ноябрь 2024");

        System.out.println("Каждый класс выполняет одну задачу:");
        ReportManager manager = new ReportManager(reportData);
        manager.generateReport();

        ReportPrinter printer = new ReportPrinter(reportData);
        printer.printReport();

        ReportSaver saver = new ReportSaver(reportData);
        saver.saveReport();

        System.out.println("\n" + "=" .repeat(50));
        System.out.println(" ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА УСПЕШНО!");
    }
}