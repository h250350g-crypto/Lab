# OOP VIVA QUESTIONS AND ANSWER
# NOKUTENDA BASVI
# SOFTWARE ENGINEERING
# H250350G

1.  What is the difference between a class and an object? Where in memory does each live?
Answer:
A class is a blueprint that defines fields and methods. An object is a real instance created from that blueprint with new, holding its own field values.
The class's metadata (its structure, static fields and method code) lives in the Metaspace (the method area). Objects live on the heap. The reference variable that points to an object is usually on the stack when it is a local variable.
Car c = new Car();   // 'c' on stack, object on heap
2.  Why does Java not allow multiple inheritance of classes, and how do interfaces work around it?
Answer:
If a class could extend two classes that both define show(), the compiler could not tell which version to inherit. This is the diamond problem. Java avoids it by allowing only one superclass.
A class can implement many interfaces instead, because interfaces mostly declare behaviour without state. If two interfaces have the same default method, the class must override it and choose explicitly with A.super.hello(). This gives multiple inheritance of type and behaviour without the ambiguity of inherited state.
3.  Explain the difference between overloading and overriding with an example of each.
Answer:
•	Overloading: the same method name with different parameters in the same class. It is resolved at compile time.
void print(int x) { }
void print(String s) { }
•	Overriding: a subclass replaces an inherited method with the same signature. It is resolved at runtime.
class Animal { String sound() { return "..."; } }
class Cat extends Animal {
    @Override String sound() { return "Meow"; }
}
4.  What happens if a superclass has no no-argument constructor and the subclass omits super(...)?
Answer:
The code does not compile. Every constructor must begin by calling a superclass constructor. If you write none, the compiler inserts super(), which has nothing to match when the superclass only has, for example, Animal(String name).
The fix is to call the existing constructor explicitly:
class Dog extends Animal {
    Dog(String name) { super(name); }
}
5.  When would you choose an abstract class over an interface?
Answer:
Choose an abstract class when subclasses share state (fields) or common concrete code, and have a true "is-a" relationship. Examples are Account with a shared balance and deposit(), or LibraryItem. Abstract classes can also use constructors to set up shared state.
Choose an interface when unrelated classes need the same capability (for example Flyable for Bird and Aeroplane), or when a class must play several roles, since a class can extend only one class.
6.  Why must hashCode be overridden whenever equals is overridden?
Answer:
The contract says that if a.equals(b) is true, then a.hashCode() == b.hashCode() must also be true.
Hash-based collections (HashSet, HashMap) use the hash code first to pick a bucket and only then call equals. If you override only equals, two equal objects can land in different buckets. A HashSet will then accept duplicates and map.get() will fail to find the key.
Use the same fields in both methods (for a Book, the ISBN).
7.  What is the difference between checked and unchecked exceptions? Give two examples of each.
Answer:
•	Checked: the compiler forces you to catch them or declare them with throws. Examples: IOException, SQLException (also InterruptedException). They are for recoverable situations outside the program's control.
•	Unchecked: subclasses of RuntimeException (and Error). The compiler does not force handling. Examples: NullPointerException, ArrayIndexOutOfBoundsException (also IllegalArgumentException). They usually signal programming mistakes.
8.  Why can generics not be used with primitive types, and what is type erasure?
Answer:
Generic type parameters must be reference types. At runtime T is treated as Object (or its bound), and primitives such as int are not objects. Use wrapper classes instead (List<Integer>); autoboxing converts values automatically.
Type erasure means the compiler checks generic types at compile time, then removes them and replaces T with Object or its bound in the bytecode, inserting casts where needed. At runtime a List<String> and a List<Integer> are the same class.
This keeps compatibility with older code. It also means you cannot write new T() or instanceof List<String>.
9.  What does final mean on a variable, a method and a class?
Answer:
•	Variable: it can be assigned only once. For a reference, the reference cannot change, but the object it points to can still be modified.
•	Method: it cannot be overridden by subclasses.
•	Class: it cannot be extended (for example String, and any immutable class).
10.  Explain dynamic method dispatch. Why are fields and static methods not polymorphic?
Answer:
Dynamic method dispatch means that when an overridden method is called through a superclass reference, the JVM chooses the version from the object's actual class at runtime.
Animal a = new Cat();
a.sound();   // runs Cat's version
Fields and static methods are not polymorphic because they are resolved at compile time using the reference's declared type, and they belong to a class rather than to an object. A field in a subclass with the same name only hides the parent's field, so:
Parent p = new Child();
p.label;   // gives Parent's value, not Child's
11.  What is a functional interface, and how does a lambda relate to it?
Answer:
A functional interface has exactly one abstract method (it may also have default and static methods). Examples are Runnable, Comparator and Predicate. It can be marked with @FunctionalInterface.
A lambda is a short anonymous implementation of that single method. Java works out which interface it implements from the context:
Predicate<Integer> isEven = n -> n % 2 == 0;   // implements test()
This is why a lambda can be passed wherever a functional interface is expected, such as in removeIf or stream().filter().
12.  How does try-with-resources differ from try/finally?
Answer:
Try-with-resources automatically closes any resource that implements AutoCloseable when the block ends, whether normally or by exception.
try (BufferedReader br = new BufferedReader(new FileReader("a.txt"))) {
    // use br
}
With try/finally you must write the close() call yourself, and an exception thrown by close() can hide the original exception.
Try-with-resources is shorter and less error-prone. It closes resources in reverse order of opening. If both the body and close() throw, the body's exception is kept and the close exception is attached as a suppressed exception.

