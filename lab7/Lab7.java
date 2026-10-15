import java.util.*;

// Ex 3 (Intermediate): checked custom exception
class InvalidAgeException extends Exception {
    InvalidAgeException(String msg) { super(msg); }
}
// Ex 4 (Intermediate): custom exception for withdrawals
class InsufficientFundsException extends Exception {
    private final double shortfall;
    InsufficientFundsException(double shortfall) {
        super("Insufficient funds, short by " + shortfall);
        this.shortfall = shortfall;
    }
    double getShortfall() { return shortfall; }
}
class BankAccount {
    private double balance;
    BankAccount(double opening) { balance = opening; }
    void withdraw(double amt) throws InsufficientFundsException {
        if (amt <= 0) throw new IllegalArgumentException("Withdrawal must be positive");
        if (amt > balance) throw new InsufficientFundsException(amt - balance);
        balance -= amt;
    }
    double getBalance() { return balance; }
}
/* TRADE-OFFS: boolean return vs exception
 *  boolean: simple, cheap, but callers can silently IGNORE false and carry on; no detail about why.
 *  checked exception: the compiler FORCES the caller to handle it, and it can carry data
 *    (the shortfall). Costs: more try/catch code, and it is heavier. Good when the failure is a
 *    normal, recoverable business case that must not be ignored.
 */
public class Lab7 {

    // Ex 1 (Basic): divide two numbers read from a Scanner
    static void divide(Scanner in) {
        try {
            System.out.print("Enter two integers: ");
            int a = Integer.parseInt(in.next());
            int b = Integer.parseInt(in.next());
            System.out.println(a + " / " + b + " = " + (a / b));
        } catch (NumberFormatException e) {
            System.out.println("Error: input was not a whole number");
        } catch (ArithmeticException e) {
            System.out.println("Error: cannot divide by zero");
        }
    }

    // Ex 2 (Basic)
    static void getElement(int[] arr, int index) {
        try {
            System.out.println("arr[" + index + "] = " + arr[index]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Index " + index + " is outside 0.." + (arr.length - 1));
        }
    }

    // Ex 3
    static void validateAge(int age) throws InvalidAgeException {
        if (age < 0 || age > 120) throw new InvalidAgeException("Age out of range 0-120: " + age);
        System.out.println("Age " + age + " is valid");
    }

    // Ex 5 (Challenge): return inside try, print inside finally
    @SuppressWarnings("finally")
    static int tryFinally() {
        try {
            System.out.println("in try");
            return 1;
        } finally {
            System.out.println("in finally");   // runs BEFORE the method actually returns
        }
    }
    /* Predicted output: in try / in finally / then main prints 1.
     * WHY: the JVM guarantees finally runs on every exit path (normal, return, break, exception).
     * The return value (1) is computed and parked, finally executes, then the method returns it.
     * Only System.exit(), a JVM crash or an infinite loop stops finally.
     * Trap: a `return` inside finally would OVERRIDE the try's return value (and swallow exceptions). */

    public static void main(String[] args) {
        System.out.println("== Ex 1 (inputs '10 2', 'ten 2', '5 0') ==");
        divide(new Scanner("10 2"));
        divide(new Scanner("ten 2"));
        divide(new Scanner("5 0"));

        System.out.println("\n== Ex 2 ==");
        int[] data = {10, 20, 30};
        getElement(data, 1); getElement(data, 5);

        System.out.println("\n== Ex 3 ==");
        for (int age : new int[]{25, -3, 150}) {
            try { validateAge(age); } catch (InvalidAgeException e) { System.out.println("Rejected: " + e.getMessage()); }
        }

        System.out.println("\n== Ex 4 ==");
        BankAccount acc = new BankAccount(100);
        try { acc.withdraw(30); acc.withdraw(200); }
        catch (InsufficientFundsException e) { System.out.println(e.getMessage() + " | balance " + acc.getBalance()); }

        System.out.println("\n== Ex 5 ==");
        System.out.println("returned " + tryFinally());
    }
}
