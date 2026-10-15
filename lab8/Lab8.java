import java.util.*;
import java.util.stream.*;

// Ex 3 (Intermediate): generic Stack<T> backed by ArrayList<T>
class Stack<T> {
    private final ArrayList<T> items = new ArrayList<>();

    void push(T x) {
        items.add(x);
    }

    T pop() {
        if (isEmpty())
            throw new RuntimeException("Stack is empty");
        return items.remove(items.size() - 1);
    }

    T peek() {
        if (isEmpty())
            throw new RuntimeException("Stack is empty");
        return items.get(items.size() - 1);
    }

    boolean isEmpty() {
        return items.isEmpty();
    }
}

public class Lab8 {
    // Ex 5 (Challenge): generic bubble sort
    static <T extends Comparable<T>> void bubbleSort(T[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) { // T is guaranteed to have compareTo
                    T tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }
    /*
     * WHY int[] WILL NOT WORK: generics work only with reference types. T must be a
     * class or
     * interface, and int is a primitive that does not implement Comparable<T>.
     * int[] is an array
     * of primitives, not T[] for any T. Use Integer[] (boxing) instead. Type
     * erasure is the reason:
     * T becomes Object (or its bound) at runtime, and primitives cannot be treated
     * as Objects.
     */

    public static void main(String[] args) {
        System.out.println("== Ex 1 ==");
        List<Integer> nums = new ArrayList<>(List.of(4, 9, 1, 7, 3, 8, 2, 10, 6, 5));
        int sum = 0;
        for (int n : nums)
            sum += n;
        System.out.println("Sum = " + sum + ", Max = " + Collections.max(nums));
        List<Integer> rev = new ArrayList<>(nums);
        Collections.reverse(rev);
        System.out.println("Reversed: " + rev);

        System.out.println("\n== Ex 2 ==");
        List<String> names = List.of("Tariro", "Farai", "Tariro", "Rudo", "Farai", "Chipo");
        Set<String> unique = new LinkedHashSet<>(names); // keeps first-seen order
        System.out.println(unique);

        System.out.println("\n== Ex 3 ==");
        Stack<String> st = new Stack<>();
        st.push("a");
        st.push("b");
        st.push("c");
        System.out.println("peek=" + st.peek() + " pop=" + st.pop() + " pop=" + st.pop() + " empty? " + st.isEmpty());

        System.out.println("\n== Ex 4: word frequency ==");
        String text = "the cat sat on the mat and the dog sat on the log";
        Map<String, Integer> freq = new HashMap<>();
        for (String w : text.toLowerCase().split("\\W+"))
            freq.merge(w, 1, Integer::sum);
        freq.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));

        System.out.println("\n== Ex 5 ==");
        Integer[] ia = { 5, 2, 9, 1, 7 };
        String[] sa = { "pear", "apple", "mango", "banana" };
        bubbleSort(ia);
        bubbleSort(sa);
        System.out.println(Arrays.toString(ia) + " " + Arrays.toString(sa));
    }
}
