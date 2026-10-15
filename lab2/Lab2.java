// ===== Lab 2 solutions: constructors, this, static =====

// Ex 1 + 2 (Basic): Circle with constructors and a static counter
class Circle {
    private static int count = 0; // one copy shared by all circles
    private final double radius;

    Circle(double radius) {
        this.radius = radius;
        count++;
    }

    Circle() {
        this(1.0);
    } // chaining: default radius 1.0

    static int getCount() {
        return count;
    }

    double area() {
        return Math.PI * radius * radius;
    }

    public String toString() {
        return "Circle(r=" + radius + ")";
    }
}

// Ex 3 (Intermediate): Employee with auto-incrementing id, 3 constructors +
// copy constructor
class Employee {
    private static int nextId = 1000;
    private final int employeeId;
    private final String name;
    private final double salary;
    private final String department;

    Employee(String name, double salary, String department) {
        this.employeeId = nextId++;
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    Employee(String name, double salary) {
        this(name, salary, "General");
    }

    Employee(String name) {
        this(name, 0.0);
    }

    // copy constructor: same data but a NEW id (it is a new employee record)
    Employee(Employee other) {
        this(other.name, other.salary, other.department);
    }

    public String toString() {
        return "#" + employeeId + " " + name + " | " + department + " | " + salary;
    }
}

// Ex 4 (Intermediate): predict the output
// Predicted output when `new Order()` is executed twice:
// static 1, static 2 (once, when the class loads)
// then for EACH object: instance 1, instance 2, constructor
class Order {
    static {
        System.out.println("static 1");
    }
    {
        System.out.println("instance 1");
    }
    static {
        System.out.println("static 2");
    }
    {
        System.out.println("instance 2");
    }

    Order() {
        System.out.println("constructor");
    }
}

// Ex 5 (Challenge): Temperature with a private constructor and static factories
class Temperature {
    private final double celsius;

    private Temperature(double celsius) {
        this.celsius = celsius;
    }

    static Temperature fromCelsius(double c) {
        return new Temperature(c);
    }

    static Temperature fromFahrenheit(double f) {
        return new Temperature((f - 32) * 5 / 9);
    }

    double toCelsius() {
        return celsius;
    }

    double toFahrenheit() {
        return celsius * 9 / 5 + 32;
    }

    public String toString() {
        return String.format("%.1f C / %.1f F", toCelsius(), toFahrenheit());
    }
}

/*
 * WHY STATIC FACTORIES CAN BE CLEARER THAN OVERLOADED CONSTRUCTORS
 * 1. They have names. fromCelsius(30) says what the number means; new
 * Temperature(30) does not.
 * 2. Constructors cannot be told apart by meaning: new Temperature(double) for
 * Celsius and
 * new Temperature(double) for Fahrenheit have the SAME signature, so Java will
 * not allow both.
 * 3. A factory can validate, return a cached object, or return a subclass; a
 * constructor always
 * creates a brand-new object of exactly that class.
 */
public class Lab2 {
    public static void main(String[] args) {
        System.out.println("== Ex 1-2: Circle ==");
        Circle c1 = new Circle(2.5), c2 = new Circle();
        System.out.println(c1 + " " + c2 + " count=" + Circle.getCount());

        System.out.println("\n== Ex 3: Employee ==");
        Employee e1 = new Employee("Nokutenda", 1500, "IT");
        Employee e2 = new Employee("Panashe", 1200);
        Employee e3 = new Employee("Tawana");
        Employee e4 = new Employee(e1);
        System.out.println(e1);
        System.out.println(e2);
        System.out.println(e3);
        System.out.println(e4);

        System.out.println("\n== Ex 4: initialisation order ==");
        new Order();
        System.out.println("--- second object ---");
        new Order();

        System.out.println("\n== Ex 5: Temperature ==");
        System.out.println(Temperature.fromCelsius(30));
        System.out.println(Temperature.fromFahrenheit(212));
    }
}
