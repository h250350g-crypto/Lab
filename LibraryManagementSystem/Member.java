import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Member {
    public static final int MAX_LOANS = 3;

    private final String id;
    private String name;
    private final List<Loan> activeLoans = new ArrayList<>();

    public Member(String id, String name) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Member id is required");
        this.id = id.trim();
        setName(name);
    }

    public String getId()   { return id; }
    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        this.name = name.trim();
    }

    /** Read-only view so outside code cannot bypass the limit check. */
    public List<Loan> getActiveLoans() { return Collections.unmodifiableList(activeLoans); }

    public void checkCanBorrow() throws MemberLimitExceededException {
        if (activeLoans.size() >= MAX_LOANS) throw new MemberLimitExceededException(id, MAX_LOANS);
    }
    void addLoan(Loan loan)    { activeLoans.add(loan); }
    void removeLoan(Loan loan) { activeLoans.remove(loan); }

    @Override public String toString() {
        return String.format("[%s] %s (%d/%d loans)", id, name, activeLoans.size(), MAX_LOANS);
    }
}
