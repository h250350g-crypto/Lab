import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

/** Core service: owns the collections and enforces the rules. */
public class Library {
    private final Map<String, LibraryItem> items = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final List<Loan> loans = new ArrayList<>();
    private final Supplier<LocalDate> today;        // injectable clock makes overdue testable

    public Library()                         { this(LocalDate::now); }
    public Library(Supplier<LocalDate> today){ this.today = today; }

    public LocalDate today() { return today.get(); }

    // ---------- registration ----------
    public void addItem(LibraryItem item) {
        if (items.containsKey(item.getId())) throw new IllegalArgumentException("Item id already exists: " + item.getId());
        items.put(item.getId(), item);
    }
    public void addMember(Member member) {
        if (members.containsKey(member.getId())) throw new IllegalArgumentException("Member id already exists: " + member.getId());
        members.put(member.getId(), member);
    }

    // ---------- borrowing ----------
    public Loan borrow(String memberId, String itemId)
            throws ItemNotAvailableException, MemberLimitExceededException {
        Member member = findMember(memberId);                  // 1. look up (IllegalArgumentException if missing)
        LibraryItem item = findItem(itemId);
        member.checkCanBorrow();                               // 2. loan limit
        item.borrow(member);                                   //    availability (throws if on loan)
        LocalDate start = today.get();                         // 3. create the Loan and record it
        Loan loan = new Loan(item, member, start, start.plusDays(item.loanPeriodDays()));
        member.addLoan(loan);
        loans.add(loan);
        return loan;
    }

    public Loan giveBack(String itemId) {
        LibraryItem item = findItem(itemId);
        Loan loan = loans.stream().filter(l -> l.getItem() == item && l.isActive()).findFirst()
                .orElseThrow(() -> new IllegalStateException("Item " + itemId + " is not on loan"));
        loan.markReturned(today.get());
        loan.getMember().removeLoan(loan);
        item.giveBack();
        return loan;
    }

    // ---------- queries (streams) ----------
    public List<Loan> overdueLoans() {
        LocalDate now = today.get();
        return loans.stream().filter(l -> l.isOverdue(now))
                .sorted(Comparator.comparing(Loan::getDueDate)).toList();
    }
    public List<LibraryItem> searchByTitle(String text) {
        String q = text.toLowerCase();
        return items.values().stream()
                .filter(i -> i.getTitle().toLowerCase().contains(q))
                .sorted(Comparator.comparing(LibraryItem::getTitle, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public LibraryItem findItem(String id) {
        LibraryItem i = items.get(id == null ? "" : id.trim());
        if (i == null) throw new IllegalArgumentException("No such item: " + id);
        return i;
    }
    public Member findMember(String id) {
        Member m = members.get(id == null ? "" : id.trim());
        if (m == null) throw new IllegalArgumentException("No such member: " + id);
        return m;
    }

    public Collection<LibraryItem> getItems()  { return Collections.unmodifiableCollection(items.values()); }
    public Collection<Member> getMembers()     { return Collections.unmodifiableCollection(members.values()); }
    public List<Loan> getLoans()               { return Collections.unmodifiableList(loans); }

    /** Used by the store when reloading saved loans (keeps item/member state consistent). */
    void restoreLoan(String itemId, String memberId, LocalDate borrowed, LocalDate due, LocalDate returned) {
        LibraryItem item = findItem(itemId);
        Member member = findMember(memberId);
        Loan loan = new Loan(item, member, borrowed, due);
        if (returned != null) {
            loan.markReturned(returned);
        } else {
            try { item.borrow(member); }
            catch (ItemNotAvailableException e) { throw new IllegalStateException("Corrupt data: " + e.getMessage(), e); }
            member.addLoan(loan);
        }
        loans.add(loan);
    }
}
