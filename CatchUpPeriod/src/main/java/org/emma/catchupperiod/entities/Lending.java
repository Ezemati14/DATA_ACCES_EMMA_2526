package org.emma.catchupperiod.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "lendings")
public class Lending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('lendings_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "lendingdate", nullable = false)
    private LocalDate lendingdate;

    @Column(name = "returningdate")
    private LocalDate returningdate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrower", nullable = false)
    private User borrower;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getLendingdate() {
        return lendingdate;
    }

    public void setLendingdate(LocalDate lendingdate) {
        this.lendingdate = lendingdate;
    }

    public LocalDate getReturningdate() {
        return returningdate;
    }

    public void setReturningdate(LocalDate returningdate) {
        this.returningdate = returningdate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

}