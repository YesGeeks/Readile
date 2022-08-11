package com.readile.readile.models.book.category;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class BookCategoryId implements Serializable {
    @Column(name = "category_id")
    private long categoryId;
    @Column(name = "book_id")
    private long bookId;
}