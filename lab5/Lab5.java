// ===== Lab 5 solutions: polymorphism =====

// Ex 1 (Basic): overloaded print
class Printer {
    void print(int x)      { System.out.println("int: " + x); }
    void print(double x)   { System.out.println("double: " + x); }
    void print(String x)   { System.out.println("String: " + x); }
    void print(int[] x)    { System.out.println("int[]: " + java.util.Arrays.toString(x)); }
}

// Ex 2 (Basic): Animal hierarchy
class Animal { String sound() { return "..."; } }
class Cat extends Animal  { @Override String sound() { return "Meow"; } }
class Cow extends Animal  { @Override String sound() { return "Moo"; } }
class Duck extends Animal { @Override String sound() { return "Quack"; } }

// Ex 3 (Intermediate): payroll with Intern added; the payroll loop never changes
class Employee {
    protected final String name; protected final double base;
    Employee(String name, double base) { this.name = name; this.base = base; }
    double calculatePay() { return base; }
}
class Manager extends Employee {
    private final double bonus;
    Manager(String n, double b, double bonus) { super(n, b); this.bonus = bonus; }
    @Override double calculatePay() { return base + bonus; }
}
class Developer extends Employee {
    private final int overtime;
    Developer(String n, double b, int ot) { super(n, b); this.overtime = ot; }
    @Override double calculatePay() { return base + overtime * 25.0; }
}
class Intern extends Employee {                       // NEW class, no change anywhere else
    Intern(String n, double stipend) { super(n, stipend); }
    @Override double calculatePay() { return base * 0.8; }   // reduced stipend
}

// Ex 4 (Intermediate): methods are polymorphic, fields are not
class Parent {
    String label = "Parent field";
    String who() { return "Parent method"; }
    static String kind() { return "Parent static"; }
}
class Child extends Parent {
    String label = "Child field";                     // HIDES, does not override
    @Override String who() { return "Child method"; }
    static String kind() { return "Child static"; }   // hides, does not override
}

// Ex 5 (Challenge): two designs for a payment processor
interface Payment { }
record CardPayment(String cardNo, double amount) implements Payment { }
record MobileMoneyPayment(String phone, double amount) implements Payment { }

class PaymentProcessor {
    // Design A: instanceof pattern matching - one method must know EVERY type
    static void pay(Payment p) {
        if (p instanceof CardPayment c) {
            System.out.println("Charging card " + c.cardNo() + " amount " + c.amount());
        } else if (p instanceof MobileMoneyPayment m) {
            System.out.println("Mobile money to " + m.phone() + " amount " + m.amount());
        } else {
            throw new IllegalArgumentException("Unknown payment type");
        }
    }
}
// Design B: polymorphic process() - each type knows how to process itself
interface ProcessablePayment { void process(); }
class Card implements ProcessablePayment {
    private final String cardNo; private final double amount;
    Card(String c, double a) { cardNo = c; amount = a; }
    public void process() { System.out.println("Charging card " + cardNo + " amount " + amount); }
}
class MobileMoney implements ProcessablePayment {
    private final String phone; private final double amount;
    MobileMoney(String p, double a) { phone = p; amount = a; }
    public void process() { System.out.println("Mobile money to " + phone + " amount " + amount); }
}
/* COMPARISON
 * A (instanceof): all logic in one place, easy to read for 2 types, works on types you cannot edit.
 *   But every new payment type means editing the if/else chain; forgetting one gives a runtime error.
 * B (polymorphism): add a new class (e.g. BankTransfer) and NOTHING else changes (open/closed
 *   principle); the compiler forces every implementer to supply process().
 *   Prefer B when you own the types and they will grow; use A for small, closed sets or external types.
 */
public class Lab5 {
    public static void main(String[] args) {
        System.out.println("== Ex 1: overloading ==");
        Printer p = new Printer();
        p.print(5); p.print(3.14); p.print("hello"); p.print(new int[]{1, 2, 3});

        System.out.println("\n== Ex 2: animals ==");
        Animal[] zoo = { new Cat(), new Cow(), new Duck() };
        for (Animal a : zoo) System.out.println(a.getClass().getSimpleName() + " says " + a.sound());

        System.out.println("\n== Ex 3: payroll ==");
        Employee[] staff = { new Manager("Nyasha", 3500, 800), new Developer("Tatenda", 3000, 10),
                             new Intern("Chipo", 500), new Employee("Kudzai", 2000) };
        double total = 0;
        for (Employee e : staff) total += e.calculatePay();   // loop unchanged by Intern
        System.out.println("Total payroll = " + total);

        System.out.println("\n== Ex 4: field vs method ==");
        Parent ref = new Child();
        System.out.println(ref.who());          // Child method  (runtime type decides)
        System.out.println(ref.label);          // Parent field  (declared type decides)
        System.out.println(Parent.kind() + " / " + Child.kind());
        System.out.println(((Child) ref).label);
        System.out.println("Why: methods use dynamic dispatch on the OBJECT's real class; fields and static\n"
                         + "methods are resolved at compile time from the reference's DECLARED type.");

        System.out.println("\n== Ex 5: payments ==");
        PaymentProcessor.pay(new CardPayment("4111-xxxx", 50));
        PaymentProcessor.pay(new MobileMoneyPayment("0771234567", 20));
        for (ProcessablePayment pp : new ProcessablePayment[]{ new Card("4111-xxxx", 50), new MobileMoney("0771234567", 20) })
            pp.process();
    }
}
