import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu.  Run:  java LibraryApp [dataFolder] [--today=YYYY-MM-DD]
 * --today lets you pretend it is a later date to see overdue items.
 */
public class LibraryApp {
    public static void main(String[] args) throws IOException {
        Path dir = Path.of("library-data");
        LocalDate fixedToday = null;
        for (String a : args) {
            if (a.startsWith("--today=")) fixedToday = LocalDate.parse(a.substring(8));
            else dir = Path.of(a);
        }
        final LocalDate fixed = fixedToday;
        Library lib = LibraryStore.load(dir, () -> fixed != null ? fixed : LocalDate.now());
        System.out.println("Loaded " + lib.getItems().size() + " items, " + lib.getMembers().size()
                + " members, " + lib.getLoans().size() + " loans. Today = " + lib.today());

        Scanner in = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("""

                ===== LIBRARY MENU =====
                1. Add item        2. Register member   3. Borrow
                4. Return item     5. List overdue      6. Search by title
                7. List all items  8. List members      0. Save and exit""");
            System.out.print("Choice: ");
            if (!in.hasNextLine()) break;                        // end of input
            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addItem(lib, in);
                    case "2" -> { Member m = new Member(ask(in, "Member id"), ask(in, "Name")); lib.addMember(m); System.out.println("Registered " + m); }
                    case "3" -> System.out.println("Borrowed: " + lib.borrow(ask(in, "Member id"), ask(in, "Item id")));
                    case "4" -> System.out.println("Returned: " + lib.giveBack(ask(in, "Item id")));
                    case "5" -> { List<Loan> od = lib.overdueLoans();
                                  if (od.isEmpty()) System.out.println("No overdue items.");
                                  else od.forEach(l -> System.out.println("OVERDUE: " + l)); }
                    case "6" -> { List<LibraryItem> r = lib.searchByTitle(ask(in, "Search text"));
                                  if (r.isEmpty()) System.out.println("No matches.");
                                  else r.forEach(System.out::println); }
                    case "7" -> lib.getItems().forEach(System.out::println);
                    case "8" -> lib.getMembers().forEach(System.out::println);
                    case "0" -> running = false;
                    default  -> System.out.println("Unknown option.");
                }
            } catch (ItemNotAvailableException | MemberLimitExceededException e) {
                System.out.println("Cannot borrow: " + e.getMessage());          // business-rule failures
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());                  // bad input, keep running
            }
        }
        LibraryStore.save(lib, dir);
        System.out.println("Data saved to " + dir.toAbsolutePath() + ". Goodbye!");
    }

    private static void addItem(Library lib, Scanner in) {
        String type = ask(in, "Type (B = book, D = DVD)").toUpperCase();
        String id = ask(in, "Item id"), title = ask(in, "Title");
        LibraryItem item;
        if (type.equals("B"))      item = new Book(id, title, ask(in, "Author"));
        else if (type.equals("D")) item = new Dvd(id, title, parseInt(ask(in, "Duration (minutes)")));
        else throw new IllegalArgumentException("Type must be B or D");
        lib.addItem(item);
        System.out.println("Added " + item);
    }
    private static int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Not a whole number: " + s); }
    }
    private static String ask(Scanner in, String prompt) {
        System.out.print(prompt + ": ");
        return in.hasNextLine() ? in.nextLine() : "";
    }
}
