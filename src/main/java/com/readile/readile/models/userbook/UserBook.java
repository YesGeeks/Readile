package com.readile.readile.models.userbook;

import com.readile.readile.models.book.Book;
import com.readile.readile.models.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "User_Book")
public class UserBook {
    @EmbeddedId
    private UserBookId id = new UserBookId();

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer currentPage;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Enumerated(EnumType.STRING)
    private Status status;

    public UserBook() {
    }

    public UserBook(Book book, User user, Integer currentPage, Date startDate, Date endDate, Rating rating, Status status) {
        this.book = book;
        this.user = user;
        this.currentPage = currentPage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
        this.status = status;
    }

    public UserBookId getId() {
        return id;
    }

    public void setId(UserBookId id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBook userBook = (UserBook) o;
        return book.equals(userBook.book) && user.equals(userBook.user) && Objects.equals(currentPage, userBook.currentPage) && Objects.equals(startDate, userBook.startDate) && Objects.equals(endDate, userBook.endDate) && rating == userBook.rating && status == userBook.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, user, currentPage, startDate, endDate, rating, status);
    }

    @Override
    public String toString() {
        return "UserBook{" +
                "id=" + id +
                ", book=" + book +
                ", user=" + user +
                ", currentPage=" + currentPage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", rating=" + rating +
                ", status=" + status +
                '}';
    }
}