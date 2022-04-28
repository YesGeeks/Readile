package com.readile.readile.models.book;

import java.util.Objects;

public class Author {
    private Integer authorId;
    private String name;

    public Author() {
    }

    public Author(Integer authorId, String authorName) {
        this.authorId = authorId;
        this.name = authorName;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return name;
    }

    public void setAuthorName(String authorName) {
        this.name = authorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(authorId, author.authorId) && name.equals(author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, name);
    }
}