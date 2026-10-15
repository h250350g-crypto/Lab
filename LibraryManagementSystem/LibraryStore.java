import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

/** CSV persistence: items.csv, members.csv, loans.csv inside a data folder. */
public class LibraryStore {

    public static void save(Library lib, Path dir) throws IOException {
        Files.createDirectories(dir);

        List<String> items = new ArrayList<>();
        items.add("type,id,title,detail");
        for (LibraryItem i : lib.getItems()) {
            String detail = (i instanceof Dvd d) ? String.valueOf(d.getDurationMinutes()) : ((Book) i).getAuthor();
            items.add(row(i.typeName(), i.getId(), i.getTitle(), detail));
        }
        Files.write(dir.resolve("items.csv"), items);

        List<String> members = new ArrayList<>();
        members.add("id,name");
        for (Member m : lib.getMembers()) members.add(row(m.getId(), m.getName()));
        Files.write(dir.resolve("members.csv"), members);

        List<String> loans = new ArrayList<>();
        loans.add("itemId,memberId,borrowDate,dueDate,returnDate");
        for (Loan l : lib.getLoans())
            loans.add(row(l.getItem().getId(), l.getMember().getId(), l.getBorrowDate().toString(),
                    l.getDueDate().toString(), l.getReturnDate() == null ? "" : l.getReturnDate().toString()));
        Files.write(dir.resolve("loans.csv"), loans);
    }

    /** Loads whatever exists; a missing folder simply gives an empty library. */
    public static Library load(Path dir, Supplier<LocalDate> clock) throws IOException {
        Library lib = new Library(clock);
        Path items = dir.resolve("items.csv"), members = dir.resolve("members.csv"), loans = dir.resolve("loans.csv");

        if (Files.exists(items)) for (List<String> f : rows(items)) {
            switch (f.get(0)) {
                case "Book" -> lib.addItem(new Book(f.get(1), f.get(2), f.get(3)));
                case "DVD"  -> lib.addItem(new Dvd(f.get(1), f.get(2), Integer.parseInt(f.get(3))));
                default -> throw new IOException("Unknown item type: " + f.get(0));
            }
        }
        if (Files.exists(members)) for (List<String> f : rows(members)) lib.addMember(new Member(f.get(0), f.get(1)));
        if (Files.exists(loans)) for (List<String> f : rows(loans)) {
            LocalDate returned = f.get(4).isEmpty() ? null : LocalDate.parse(f.get(4));
            lib.restoreLoan(f.get(0), f.get(1), LocalDate.parse(f.get(2)), LocalDate.parse(f.get(3)), returned);
        }
        return lib;
    }

    // ---------- tiny CSV helpers (handle commas and quotes inside fields) ----------
    private static String row(String... fields) {
        StringJoiner j = new StringJoiner(",");
        for (String f : fields) j.add(escape(f));
        return j.toString();
    }
    private static String escape(String s) {
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) return "\"" + s.replace("\"", "\"\"") + "\"";
        return s;
    }
    private static List<List<String>> rows(Path file) throws IOException {
        List<List<String>> out = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);
        for (int i = 1; i < lines.size(); i++)                  // skip header
            if (!lines.get(i).isBlank()) out.add(parse(lines.get(i)));
        return out;
    }
    static List<String> parse(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (quoted) {
                if (c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') { cur.append('"'); i++; }
                else if (c == '"') quoted = false;
                else cur.append(c);
            } else if (c == '"') quoted = true;
            else if (c == ',') { out.add(cur.toString()); cur.setLength(0); }
            else cur.append(c);
        }
        out.add(cur.toString());
        return out;
    }
}
