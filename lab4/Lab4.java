import java.util.ArrayList;

// Ex 1 + 2 (Basic): Person -> Student, Teacher share Person's code
class Person {
    protected String name; protected int age;
    Person(String name, int age) { this.name = name; this.age = age; }
    void display() { System.out.println("Name: " + name + ", Age: " + age); }
}
class Student extends Person {
    private String course;
    Student(String name, int age, String course) { super(name, age); this.course = course; }
    @Override void display() { super.display(); System.out.println("  Course: " + course); }
}
class Teacher extends Person {
    private String subject;
    Teacher(String name, int age, String subject) { super(name, age); this.subject = subject; }
    @Override void display() { super.display(); System.out.println("  Teaches: " + subject); }
}

// Ex 3 (Intermediate): multilevel inheritance, constructor order
class Vehicle {
    Vehicle() { System.out.println("1. Vehicle constructor"); }
}
class Car extends Vehicle {
    Car() { /* implicit super() */ System.out.println("2. Car constructor"); }
}
class ElectricCar extends Car {
    ElectricCar() { System.out.println("3. ElectricCar constructor"); }
}

// Ex 4 (Intermediate): why this fails to compile
class Animal {
    String name;
    Animal(String name) { this.name = name; }   // NO no-argument constructor exists
}
/* DOES NOT COMPILE:
 *   class Dog extends Animal { }
 * error: constructor Animal in class Animal cannot be applied to given types;
 *
 * REASON: every constructor must start by calling a superclass constructor. If you write none,
 * the compiler inserts super() (the no-argument one). Animal has only Animal(String), so
 * the inserted super() cannot be matched and compilation fails.
 * FIX: call the existing constructor explicitly.
 */
class Dog extends Animal {
    Dog(String name) { super(name); }
}

// Ex 5 (Challenge): inheritance vs composition for a Stack
class BadStack<T> extends ArrayList<T> {          // IS-A ArrayList: exposes everything
    void push(T x) { add(x); }
    T pop() { return remove(size() - 1); }
}
class GoodStack<T> {                              // HAS-A ArrayList: exposes only stack operations
    private final ArrayList<T> items = new ArrayList<>();
    void push(T x) { items.add(x); }
    T pop() {
        if (items.isEmpty()) throw new IllegalStateException("Stack empty");
        return items.remove(items.size() - 1);
    }
    boolean isEmpty() { return items.isEmpty(); }
}
/* What the inheritance version wrongly exposes: add(int, E), remove(int), get(int), set(...),
 * addAll, sort, clear... Anyone can insert or remove in the MIDDLE, which breaks the
 * last-in-first-out rule. Inheritance also locks the class to ArrayList forever and breaks
 * the IS-A rule: a Stack is not an ArrayList. Composition hides the list, so the
 * implementation can change without affecting callers.
 */
public class Lab4 {
    public static void main(String[] args) {
        System.out.println("== Ex 1-2 ==");
        new Student("Tariro", 20, "Software Engineering").display();
        new Teacher("Mr Moyo", 40, "Java").display();

        System.out.println("\n== Ex 3: constructor order for new ElectricCar() ==");
        new ElectricCar();

        System.out.println("\n== Ex 4 ==");
        System.out.println(new Dog("Rex").name);

        System.out.println("\n== Ex 5 ==");
        BadStack<Integer> bad = new BadStack<>();
        bad.push(1); bad.push(2); bad.push(3);
        bad.add(0, 99);                         // breaks LIFO: inserts at the bottom
        bad.remove(1);                          // removes from the middle
        System.out.println("BadStack after illegal operations: " + bad);
        GoodStack<Integer> good = new GoodStack<>();
        good.push(1); good.push(2); good.push(3);
        System.out.println("GoodStack pops: " + good.pop() + ", " + good.pop() + ", " + good.pop());
    }
}
