import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentGrades {

    public static class Student {
        String name;
        List<Integer> grades;
        double average;

        public Student(String name) {
            this.name = name;
            this.grades = new ArrayList<>();
        }

        public void calculateAverage() {
            if (grades.isEmpty()) {
                average = 0;
                return;
            }
            double sum = 0;
            for (int grade : grades) {
                sum += grade;
            }
            average = sum / grades.size();
        }
    }

    public List<Student> analyze(String filename) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length > 1) {
                    Student student = new Student(parts[0]);
                    for (int i = 1; i < parts.length; i++) {
                        try {
                            student.grades.add(Integer.parseInt(parts[i]));
                        } catch (NumberFormatException e) {
                            // Пропускаем некорректные оценки
                        }
                    }
                    student.calculateAverage();
                    students.add(student);
                }
            }
        } catch (IOException e) {
            System.out.println("Файл не найден!");
            return null;
        }

        return students;
    }

    public String bestStudent(List<Student> students) {
        if (students == null || students.isEmpty()) return "Нет студентов";

        Student best = students.get(0);
        for (Student student : students) {
            if (student.average > best.average) {
                best = student;
            }
        }
        return best.name;
    }

    public String worstStudent(List<Student> students) {
        if (students == null || students.isEmpty()) return "Нет студентов";

        Student worst = students.get(0);
        for (Student student : students) {
            if (student.average < worst.average) {
                worst = student;
            }
        }
        return worst.name;
    }

    public void printAnalysis(List<Student> students) {
        if (students == null || students.isEmpty()) {
            System.out.println("Нет данных для анализа");
            return;
        }

        for (Student student : students) {
            System.out.printf("%s: %.2f\n", student.name, student.average);
        }

        System.out.println("Лучший студент: " + bestStudent(students));
        System.out.println("Худший студент: " + worstStudent(students));
    }
}