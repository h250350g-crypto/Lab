import java.nio.file.*;
import java.time.LocalDate;

/** Automated checks of the rules: java SelfTest (prints PASS or throws). */
public class SelfTest {
    static int checks = 0;
    static void check(boolean ok, String what) { checks++; if (!ok) throw new AssertionError("FAILED: " + what); }

    public static void main(String[] args) throws Exception {
        LocalDate[] now = { LocalDate.of(2026, 10, 1) };
        Library lib = new Library(() -> now[0]);
        lib.addItem(new Book("B1", "Clean Code", "Robert Martin"));
        lib.addItem(new Book("B2", "Effective Java, 3rd ed", "Joshua Bloch"));
        lib.addItem(new Book("B3", "Head First Java", "Sierra"));
        lib.addItem(new Dvd("D1", "The Matrix", 136));
        lib.addMember(new Member("M1", "Tariro"));
        lib.addMember(new Member("M2", "Farai"));

        // polymorphism: different loan periods
        check(lib.findItem("B1").loanPeriodDays() == 21 && lib.findItem("D1").loanPeriodDays() == 7, "loan periods");

        lib.borrow("M1", "B1"); lib.borrow("M1", "B2"); lib.borrow("M1", "D1");
        try { lib.borrow("M1", "B3"); check(false, "4th loan should fail"); }
        catch (MemberLimitExceededException e) { check(true, "limit"); }
        try { lib.borrow("M2", "B1"); check(false, "double loan should fail"); }
        catch (ItemNotAvailableException e) { check(true, "unavailable"); }
        try { lib.borrow("M9", "B1"); check(false, "unknown member"); }
        catch (IllegalArgumentException e) { check(true, "unknown member"); }

        check(lib.overdueLoans().isEmpty(), "nothing overdue yet");
        now[0] = LocalDate.of(2026, 10, 10);                  // DVD was due 8 Oct
        check(lib.overdueLoans().size() == 1 && lib.overdueLoans().get(0).getItem().getId().equals("D1"), "DVD overdue");

        lib.giveBack("D1");
        check(lib.overdueLoans().isEmpty(), "return clears overdue");
        lib.borrow("M1", "B3");                               // slot freed, now allowed
        check(lib.searchByTitle("java").size() == 2, "search 'java' (case-insensitive)");

        // persistence round trip (including a comma in a title)
        Path dir = Files.createTempDirectory("libtest");
        LibraryStore.save(lib, dir);
        Library again = LibraryStore.load(dir, () -> now[0]);
        check(again.getItems().size() == 4 && again.getMembers().size() == 2, "reload counts");
        check(again.findItem("B2").getTitle().equals("Effective Java, 3rd ed"), "comma survives CSV");
        check(!again.findItem("B3").isAvailable() && again.findItem("D1").isAvailable(), "availability restored");
        check(again.findMember("M1").getActiveLoans().size() == 3, "member loans restored");
        System.out.println("PASS: " + checks + " checks");
    }
}
