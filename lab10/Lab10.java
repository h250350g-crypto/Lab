import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.*;

record Student(String name, double score) {
}

public class Lab10 {

    // Ex 1 (Basic): save five typed lines, then read them back
    static void saveAndRead(Scanner in, Path file) throws IOException {
        List<String> lines = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            System.out.print("Line " + i + ": ");
            lines.add(in.nextLine());
        }
        Files.write(file, lines); // creates/overwrites
        System.out.println("--- read back ---");
        Files.readAllLines(file).forEach(System.out::println);
    }

    // Ex 3 (Intermediate): lines, words, characters
    static void countFile(Path file) throws IOException {
        String text = Files.readString(file);
        long lines = Files.readAllLines(file).size();
        long words = Arrays.stream(text.split("\\s+")).filter(w -> !w.isEmpty()).count();
        System.out.println("Lines: " + lines + ", Words: " + words + ", Characters: " + text.length());
    }

    // Ex 5 (Challenge): group learners from CSV by grade band, one report per band
    static String band(double score) {
        return score >= 70 ? "A" : score >= 50 ? "B" : "C";
    }

    static void groupReports(Path csv, Path outDir) throws IOException {
        List<Student> learners = Files.readAllLines(csv).stream()
                .skip(1) // header: name,score
                .filter(l -> !l.isBlank())
                .map(l -> l.split(","))
                .map(p -> new Student(p[0].trim(), Double.parseDouble(p[1].trim())))
                .toList();
        Map<String, List<Student>> byBand = learners.stream().collect(Collectors.groupingBy(s -> band(s.score())));
        Files.createDirectories(outDir);
        for (var entry : new TreeMap<>(byBand).entrySet()) {
            List<String> report = new ArrayList<>();
            report.add("Band " + entry.getKey() + " report (" + entry.getValue().size() + " learners)");
            entry.getValue().forEach(s -> report.add(s.name() + " - " + s.score()));
            Path out = outDir.resolve("band_" + entry.getKey() + ".txt");
            Files.write(out, report);
            System.out.println("Wrote " + out + ": " + report);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("== Ex 1 ==");
        Path tmp = Files.createTempFile("lines", ".txt");
        saveAndRead(new Scanner("alpha\nbeta\ngamma\ndelta\nepsilon\n"), tmp); // simulated typing

        System.out.println("\n== Ex 2 ==");
        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        Predicate<Integer> isEven = n -> n % 2 == 0;
        nums.removeIf(isEven);
        System.out.println(nums);

        System.out.println("\n== Ex 3 ==");
        countFile(tmp);

        System.out.println("\n== Ex 4 ==");
        List<Student> cls = List.of(new Student("Tariro", 78), new Student("Farai", 52),
                new Student("Chipo", 90), new Student("Rudo", 61), new Student("Tendai", 45));
        double avg = cls.stream().mapToDouble(Student::score).average().orElse(0);
        System.out.println("Average = " + avg);
        cls.stream().filter(s -> s.score() > avg).map(Student::name).sorted().forEach(System.out::println);

        System.out.println("\n== Ex 5 ==");
        Path csv = Files.createTempFile("learners", ".csv");
        Files.write(csv, List.of("name,score", "Tariro,78", "Farai,52", "Chipo,90", "Rudo,61", "Tendai,45", "Kuda,70"));
        groupReports(csv, Files.createTempDirectory("reports"));
    }
}
