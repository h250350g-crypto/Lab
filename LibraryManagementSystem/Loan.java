import java.time.LocalDate;

/** One borrowing event: who has which item, since when, due when. */
public class Loan {
    private final LibraryItem item;
    private final Member member;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;                  // null while still on loan

    public Loan(LibraryItem item, Member member, LocalDate borrowDate, LocalDate dueDate) {
        if (item == null || member == null || borrowDate == null || dueDate == null)
            throw new IllegalArgumentException("Loan needs item, member and dates");
        if (dueDate.isBefore(borrowDate)) throw new IllegalArgumentException("Due date is before borrow date");
        this.item = item; this.member = member;
        this.borrowDate = borrowDate; this.dueDate = dueDate;
    }

    public LibraryItem getItem()    { return item; }
    public Member getMember()       { return member; }
    public LocalDate getBorrowDate(){ return borrowDate; }
    public LocalDate getDueDate()   { return dueDate; }
    public LocalDate getReturnDate(){ return returnDate; }
    public boolean isActive()       { return returnDate == null; }

    void markReturned(LocalDate date) {
        if (!isActive()) throw new IllegalStateException("Loan already returned");
        this.returnDate = date;
    }
    public boolean isOverdue(LocalDate today) { return isActive() && today.isAfter(dueDate); }

    @Override public String toString() {
        return String.format("%s -> %s, borrowed %s, due %s%s", item.getTitle(), member.getName(),
                borrowDate, dueDate, isActive() ? "" : ", returned " + returnDate);
    }
}
