/** Checked exception: the member already holds the maximum number of loans. */
public class MemberLimitExceededException extends Exception {
    public MemberLimitExceededException(String memberId, int limit) {
        super("Member " + memberId + " already has the maximum of " + limit + " loans");
    }
}
