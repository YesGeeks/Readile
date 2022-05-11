package com.readile.readile.services.implementation;

import com.readile.readile.models.book.Book;
import com.readile.readile.repositories.BookRepository;
import com.readile.readile.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService implements CrudService<Book> {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book save(Book entity) {
        return bookRepository.save(entity);
    }

    @Override
    public Book update(Book entity) {
        return bookRepository.save(entity);
    }

    @Override
    public void delete(Book entity) {
        bookRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Book> entities) {
        bookRepository.deleteAllInBatch(entities);
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}