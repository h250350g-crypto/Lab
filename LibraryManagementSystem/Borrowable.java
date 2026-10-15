/** Anything that can be lent to a member (interface requirement). */
public interface Borrowable {
    void borrow(Member member) throws ItemNotAvailableException;
    void giveBack();
}
