package com.readile.readile.models.userbook;

import com.readile.readile.models.book.Book;
import com.readile.readile.models.user.User;

import java.util.Date;
import java.util.Objects;

public class UserBook {
    private Book book;
    private User user;
    private Integer currentPage;
    private Date startDate;
    private Date endDate;
    private Rating rating;
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
}