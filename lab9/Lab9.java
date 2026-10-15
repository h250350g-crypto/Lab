import java.util.*;

// Ex 1 (Basic)
enum Level {
    LOW, MEDIUM, HIGH
}

// Ex 2 (Basic)
record Student(String name, int age) {
}

// Ex 3 (Intermediate): equals/hashCode by ISBN
class Book {
    private final String isbn, title;

    Book(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Book b))
            return false;
        return isbn.equals(b.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    } // same field as equals

    @Override
    public String toString() {
        return title + " [" + isbn + "]";
    }
}

// Ex 4 (Intermediate): linked list with static nested Node and inner Iterator
class MyLinkedList<T> implements Iterable<T> {
    private static class Node<T> { // static: a node does not need the list instance
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head, tail;
    private int size;

    void add(T value) {
        Node<T> n = new Node<>(value);
        if (head == null)
            head = tail = n;
        else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<T> { // inner: uses the list's 'head'
        private Node<T> current = head;

        public boolean hasNext() {
            return current != null;
        }

        public T next() {
            if (current == null)
                throw new NoSuchElementException();
            T v = current.value;
            current = current.next;
            return v;
        }
    }
}

// Ex 5 (Challenge): Coin enum
enum Coin {
    PENNY(1), NICKEL(5), DIME(10), QUARTER(25);

    private final int value;

    Coin(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }

    // Greedy (largest coin first) gives the fewest coins for these US denominations
    static Map<Coin, Integer> fewestCoins(int amount) {
        Map<Coin, Integer> result = new LinkedHashMap<>();
        Coin[] coins = values();
        for (int i = coins.length - 1; i >= 0; i--) {
            int n = amount / coins[i].value;
            if (n > 0) {
                result.put(coins[i], n);
                amount %= coins[i].value;
            }
        }
        return result;
    }
}

public class Lab9 {
    static void advice(Level l) {
        switch (l) {
            case LOW -> System.out.println("LOW: relax, nothing to do");
            case MEDIUM -> System.out.println("MEDIUM: keep an eye on it");
            case HIGH -> System.out.println("HIGH: act immediately");
        }
    }

    public static void main(String[] args) {
        System.out.println("== Ex 1 ==");
        for (Level l : Level.values())
            advice(l);

        System.out.println("\n== Ex 2 ==");
        Student s1 = new Student("Nokutenda", 20), s2 = new Student("Nokutenda", 20);
        System.out.println(s1 + " equal? " + s1.equals(s2) + " == ? " + (s1 == s2) + " name=" + s1.name());

        System.out.println("\n== Ex 3 ==");
        Set<Book> books = new HashSet<>();
        books.add(new Book("111", "Clean Code"));
        boolean second = books.add(new Book("111", "Clean Code (2nd copy)"));
        System.out.println("Duplicate added? " + second + ", set size = " + books.size());

        System.out.println("\n== Ex 4 ==");
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        for (String x : list)
            System.out.println(x);

        System.out.println("\n== Ex 5 ==");
        System.out.println("68 cents -> " + Coin.fewestCoins(68));
        System.out.println("41 cents -> " + Coin.fewestCoins(41));
    }
}
