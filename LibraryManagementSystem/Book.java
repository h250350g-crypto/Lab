public class Book extends LibraryItem {
    private final String author;

    public Book(String id, String title, String author) {
        super(id, title);
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Author is required");
        this.author = author.trim();
    }
    public String getAuthor() { return author; }

    @Override public int loanPeriodDays() { return 21; }
    @Override public String typeName()    { return "Book"; }
    @Override public String detail()      { return author; }
}
