import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Ex 1 + 2 (Basic): Student with validated marks; grade is computed, never stored
class Student {
    private String name;
    private int marks;

    Student(String name, int marks) {
        setName(name);
        setMarks(marks);
    }

    String getName() {
        return name;
    }

    int getMarks() {
        return marks;
    }

    void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name required");
        this.name = name.trim();
    }

    void setMarks(int marks) {
        if (marks < 0 || marks > 100)
            throw new IllegalArgumentException("Marks must be 0-100, got " + marks);
        this.marks = marks;
    }

    // derived from marks each time, so grade can never disagree with marks
    // (scale used here: A 70+, B 60-69, C 50-59, F below 50; change it to your
    // department's scale)
    char getGrade() {
        if (marks >= 70)
            return 'A';
        if (marks >= 60)
            return 'B';
        if (marks >= 50)
            return 'C';
        return 'F';
    }
}

// Ex 3 (Intermediate): BankAccount with transaction history
class BankAccount {
    private final String accountNumber;
    private double balance;
    private final List<String> history = new ArrayList<>();

    BankAccount(String accountNumber, double opening) {
        this.accountNumber = accountNumber;
        this.balance = opening;
        history.add("OPEN " + opening);
    }

    void deposit(double amt) {
        if (amt <= 0)
            throw new IllegalArgumentException("Deposit must be positive");
        balance += amt;
        history.add("DEPOSIT " + amt);
    }

    boolean withdraw(double amt) {
        if (amt <= 0)
            throw new IllegalArgumentException("Withdrawal must be positive");
        if (amt > balance) {
            history.add("FAILED WITHDRAW " + amt);
            return false;
        }
        balance -= amt;
        history.add("WITHDRAW " + amt);
        return true;
    }

    double getBalance() {
        return balance;
    }

    // EXPLAIN: returns a read-only VIEW of a COPY, so callers cannot change our
    // private list
    List<String> getHistory() {
        return Collections.unmodifiableList(new ArrayList<>(history));
    }
}

// Ex 4 (Intermediate): immutable class
/*
 * Design choices:
 * - final class : nobody can subclass it and add mutable behaviour.
 * - private final fields: set once in the constructor, can never be reassigned.
 * - no setters : state cannot change after construction.
 * - defensive copy of the list (in AND out): otherwise the caller who passed
 * the list in,
 * or who receives it, could still modify our internal state.
 * - String and int are already immutable, so they need no copying.
 */
final class ImmutablePerson {
    private final String name;
    private final int age;
    private final List<String> hobbies;

    ImmutablePerson(String name, int age, List<String> hobbies) {
        if (age < 0)
            throw new IllegalArgumentException("age");
        this.name = name;
        this.age = age;
        this.hobbies = List.copyOf(hobbies); // defensive copy in (also unmodifiable)
    }

    String getName() {
        return name;
    }

    int getAge() {
        return age;
    }

    List<String> getHobbies() {
        return hobbies;
    } // already unmodifiable, safe to return

    ImmutablePerson withAge(int newAge) {
        return new ImmutablePerson(name, newAge, hobbies);
    }
}

// Ex 5 (Challenge): Temperature stores kelvin internally
class Temperature {
    private double kelvin; // internal representation: hidden from callers

    Temperature(double celsius) {
        setCelsius(celsius);
    }

    void setCelsius(double c) {
        double k = c + 273.15;
        if (k < 0)
            throw new IllegalArgumentException("Below absolute zero: " + c + " C");
        this.kelvin = k;
    }

    double getCelsius() {
        return kelvin - 273.15;
    }

    double getFahrenheit() {
        return getCelsius() * 9 / 5 + 32;
    }
    // If the internal field changed to Celsius or Fahrenheit tomorrow, only this
    // class would
    // change: getCelsius() and getFahrenheit() keep the same signatures, so callers
    // still compile.
}

public class Lab3 {
    public static void main(String[] args) {
        System.out.println("== Ex 1-2: Student ==");
        Student s = new Student("Nokutenda", 72);
        System.out.println(s.getName() + " " + s.getMarks() + " grade " + s.getGrade());
        try {
            s.setMarks(150);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        s.setMarks(48);
        System.out.println("After change grade " + s.getGrade());

        System.out.println("\n== Ex 3: BankAccount history ==");
        BankAccount acc = new BankAccount("ACC-1", 100);
        acc.deposit(50);
        acc.withdraw(30);
        acc.withdraw(500);
        System.out.println(acc.getHistory());
        try {
            acc.getHistory().add("HACK");
        } catch (UnsupportedOperationException e) {
            System.out.println("History is read-only");
        }

        System.out.println("\n== Ex 4: ImmutablePerson ==");
        List<String> hobbies = new ArrayList<>(List.of("basketball", "football"));
        ImmutablePerson p = new ImmutablePerson("Nokutenda", 22, hobbies);
        hobbies.add("sneaky"); // does not affect p
        System.out.println(p.getHobbies() + " age " + p.getAge() + " -> " + p.withAge(23).getAge());

        System.out.println("\n== Ex 5: Temperature ==");
        Temperature t = new Temperature(25);
        System.out.printf("%.1f C = %.1f F%n", t.getCelsius(), t.getFahrenheit());
        try {
            new Temperature(-300);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
