package org.emma.catchupperiod.entitiesDTO;

public class BookDto {
    private String isbn;
    private String title;
    private Integer copies;
    private String outline;
    private String publisher;

    public BookDto() {
    }

    public BookDto(String isbn, String title, Integer copies, String outline, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.copies = copies;
        this.outline = outline;
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Integer getCopies() {
        return copies;
    }

    public String getOutline() {
        return outline;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
