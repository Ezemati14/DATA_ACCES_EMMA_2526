package model;

//POJO simple para representar un libro que vamos a mandar a la API
public class Book {

    private String isbn;
    private String title;
    private Integer copies;
    private String categoryCode;

    public Book() {
    }

    public Book(String isbn, String title, Integer copies, String categoryCode) {
        this.isbn = isbn;
        this.title = title;
        this.copies = copies;
        this.categoryCode = categoryCode;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategory(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Override
    public String toString() {
        return "Book{isbn=" + isbn + ", title=" + title + ", copies=" + copies + ", categoryCode=" + categoryCode + "}";
    }
}
