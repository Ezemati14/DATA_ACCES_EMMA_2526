package org.emma.catchupperiod.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Collection;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "isbn", nullable = false, length = 13)
    private String isbn;

    @Column(name = "title", nullable = false, length = 90)
    private String title;

    @ColumnDefault("1")
    @Column(name = "copies")
    private Integer copies;

    @Column(name = "outline")
    private String outline;

    @Column(name = "publisher", length = 60)
    private String publisher;

    //@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category", nullable = false)
    private Category category;

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

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}