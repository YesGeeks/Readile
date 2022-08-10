package com.readile.readile.models.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Highlight")
public class Highlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(length = 512, nullable = false)
    private String highlight;

    public Highlight(Book book, String highlight) {
        this.book = book;
        this.highlight = highlight;
    }
}