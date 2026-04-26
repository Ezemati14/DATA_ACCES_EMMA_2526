package org.emma.catchupperiod.entitiesDTO;

public class BookDto {
    private String isbn;
    private String title;
    private Integer copies;

    public BookDto() {
    }

    public BookDto(String isbn, String title, Integer availableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.copies = availableCopies;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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
}
