import java.util.*;

// Ex 1 (Basic): abstract Shape
abstract class Shape {
    abstract double area();

    abstract double perimeter();

    public String toString() {
        return String.format("%s area=%.2f perimeter=%.2f", getClass().getSimpleName(), area(), perimeter());
    }
}

class Circle extends Shape {
    private final double r;

    Circle(double r) {
        this.r = r;
    }

    double area() {
        return Math.PI * r * r;
    }

    double perimeter() {
        return 2 * Math.PI * r;
    }
}

class Rectangle extends Shape {
    protected final double w, h;

    Rectangle(double w, double h) {
        this.w = w;
        this.h = h;
    }

    double area() {
        return w * h;
    }

    double perimeter() {
        return 2 * (w + h);
    }
}

class Square extends Rectangle {
    Square(double side) {
        super(side, side);
    }
}

// Ex 2 (Basic): interface shared by unrelated classes
interface Flyable {
    void fly();
}

class Bird implements Flyable {
    public void fly() {
        System.out.println("Bird flaps its wings");
    }
}

class Aeroplane implements Flyable {
    public void fly() {
        System.out.println("Aeroplane uses jet engines");
    }
}

// Ex 3 (Intermediate): abstract Account
abstract class Account {
    protected double balance;

    void deposit(double amt) {
        if (amt <= 0)
            throw new IllegalArgumentException("Deposit must be positive");
        balance += amt; // concrete, shared by all accounts
    }

    abstract double monthlyFee(); // each account type decides

    void applyMonthEnd() {
        balance -= monthlyFee();
    }
}

class SavingsAccount extends Account {
    double monthlyFee() {
        return 0.0;
    }
}

class CurrentAccount extends Account {
    double monthlyFee() {
        return 5.0;
    }
}

// Ex 4 (Intermediate): Comparable (natural order by name) + Comparator (by
// marks)
class Student implements Comparable<Student> {
    final String name;
    final int marks;

    Student(String n, int m) {
        name = n;
        marks = m;
    }

    @Override
    public int compareTo(Student o) {
        return name.compareTo(o.name);
    }

    public String toString() {
        return name + "(" + marks + ")";
    }
}

// Ex 5 (Challenge): diamond conflict between two default methods
interface A {
    default void hello() {
        System.out.println("Hello from A");
    }
}

interface B {
    default void hello() {
        System.out.println("Hello from B");
    }
}

class C implements A, B {
    @Override
    public void hello() {
        A.super.hello(); // explicitly choose, or write own logic
        B.super.hello();
    }
}
/*
 * RULE: if a class inherits two default methods with the same signature from
 * unrelated interfaces,
 * the compiler refuses to guess the class MUST override the method. Inside the
 * override you can
 * delegate with InterfaceName.super.method(). (A class method always wins over
 * an interface default.)
 */

public class Lab6 {
    public static void main(String[] args) {
        System.out.println("== Ex 1 ==");
        for (Shape s : new Shape[] { new Circle(2), new Rectangle(3, 4), new Square(5) })
            System.out.println(s);

        System.out.println("\n== Ex 2 ==");
        for (Flyable f : new Flyable[] { new Bird(), new Aeroplane() })
            f.fly();

        System.out.println("\n== Ex 3 ==");
        Account[] accts = { new SavingsAccount(), new CurrentAccount() };
        for (Account a : accts) {
            a.deposit(100);
            a.applyMonthEnd();
            System.out.println(a.getClass().getSimpleName() + " balance " + a.balance);
        }

        System.out.println("\n== Ex 4 ==");
        List<Student> list = new ArrayList<>(
                List.of(new Student("Tariro", 72), new Student("Farai", 55), new Student("Chipo", 90)));
        Collections.sort(list);
        System.out.println("By name:  " + list);
        list.sort(Comparator.comparingInt((Student s) -> s.marks).reversed());
        System.out.println("By marks (high to low): " + list);

        System.out.println("\n== Ex 5 ==");
        new C().hello();
    }
}
