import java.time.Year;

// ---------- Exercise 1 & 2 (Basic): Car ----------
class Car {
    String brand;
    int year;
    double mileage;

    void display() {
        System.out.println("Brand: " + brand + ", Year: " + year + ", Mileage: " + mileage + " km");
    }

    // EXPLAIN: compares the car's age with 25 using the current year
    boolean isAntique() {
        return Year.now().getValue() - year > 25;
    }
}

// ---------- Exercise 3 & 4 (Intermediate): Rectangle ----------
class Rectangle {
    double width;
    double height;

    double area() {
        return width * height;
    }

    double perimeter() {
        return 2 * (width + height);
    }
}

// ---------- Exercise 5 (Challenge): Address and Person ----------
class Address {
    String street;
    String city;
}

class Person {
    String name;
    Address address; // EXPLAIN: a field that refers to another object
}

public class Lab1 {
    public static void main(String[] args) {

        System.out.println("=== Exercise 1 and 2: Car ===");
        Car c1 = new Car();
        c1.brand = "Toyota";
        c1.year = 2018;
        c1.mileage = 85000;
        Car c2 = new Car();
        c2.brand = "Ford";
        c2.year = 1995;
        c2.mileage = 210000;
        Car c3 = new Car();
        c3.brand = "Honda";
        c3.year = 2022;
        c3.mileage = 15000;
        Car[] cars = { c1, c2, c3 };
        for (Car c : cars) {
            c.display();
            System.out.println("  Antique? " + c.isAntique());
        }

        System.out.println("\n=== Exercise 3: Largest Rectangle ===");
        double[][] sizes = { { 2, 3 }, { 5, 4 }, { 1, 10 }, { 6, 6 }, { 3, 3 } };
        Rectangle[] rects = new Rectangle[5];
        for (int i = 0; i < rects.length; i++) {
            rects[i] = new Rectangle();
            rects[i].width = sizes[i][0];
            rects[i].height = sizes[i][1];
        }
        Rectangle largest = rects[0];
        for (Rectangle r : rects) {
            if (r.area() > largest.area())
                largest = r;
        }
        System.out.println("Largest: " + largest.width + " x " + largest.height
                + ", area = " + largest.area() + ", perimeter = " + largest.perimeter());

        System.out.println("\n=== Exercise 4: Aliasing ===");
        Rectangle r1 = new Rectangle();
        r1.width = 5;
        r1.height = 2;
        Rectangle r2 = r1; // copies the REFERENCE, not the object
        r2.width = 99;
        System.out.println("r1.width = " + r1.width); // 99.0

        System.out.println("\n=== Exercise 5: Person and Address ===");
        Person p = new Person();
        p.name = "Nokutenda";
        p.address = new Address();
        p.address.city = "Harare";
        System.out.println(p.name + " lives in " + p.address.city);

        Person q = new Person();
        q.name = "Farai"; // address was never assigned -> null
        // System.out.println(q.address.city); // NullPointerException
        try {
            System.out.println(q.address.city);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + q.name + " has no address yet");
        }
        // Safe version
        if (q.address != null) {
            System.out.println(q.address.city);
        } else {
            System.out.println("Safe check: address is null, so city is unknown");
        }
    }
}
