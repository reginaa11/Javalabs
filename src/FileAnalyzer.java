import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileAnalyzer {

    public static class FileStats {
        public int lines;
        public int words;
        public int characters;

        @Override
        public String toString() {
            return String.format("Строк: %d\nСлов: %d\nСимволов: %d", lines, words, characters);
        }
    }

    public FileStats analyze(String filename) {
        FileStats stats = new FileStats();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stats.lines++;
                stats.words += countWords(line);
                stats.characters += countCharacters(line);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден!");
            return null;
        }

        return stats;
    }

    private int countWords(String line) {
        if (line.trim().isEmpty()) return 0;
        return line.trim().split("\\s+").length;
    }

    private int countCharacters(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                count++;
            }
        }
        return count;
    }
}