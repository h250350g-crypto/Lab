/** Abstract parent of everything the library lends (abstraction + inheritance). */
public abstract class LibraryItem implements Borrowable {
    private final String id;
    private final String title;
    private boolean available = true;
    private Member borrower;                    // null when available

    protected LibraryItem(String id, String title) {
        if (id == null || id.isBlank())       throw new IllegalArgumentException("Item id is required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title is required");
        this.id = id.trim();
        this.title = title.trim();
    }

    /** Each subclass decides its own loan period (polymorphism). */
    public abstract int loanPeriodDays();
    /** Short type label used in menus and CSV files. */
    public abstract String typeName();
    /** Type-specific extra field (author or duration), stored in the CSV. */
    public abstract String detail();

    public String getId()        { return id; }
    public String getTitle()     { return title; }
    public boolean isAvailable() { return available; }
    public Member getBorrower()  { return borrower; }

    @Override
    public void borrow(Member member) throws ItemNotAvailableException {
        if (!available) throw new ItemNotAvailableException(id, title);
        available = false;
        borrower = member;
    }

    @Override
    public void giveBack() {
        available = true;
        borrower = null;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s \"%s\" (%s) - %s, loan period %d days",
                id, typeName(), title, detail(), available ? "available" : "on loan", loanPeriodDays());
    }
}
