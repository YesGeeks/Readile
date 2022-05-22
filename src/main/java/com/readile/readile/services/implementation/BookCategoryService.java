package com.readile.readile.services.implementation;

import com.readile.readile.models.book.Book;
import com.readile.readile.models.book.BookCategory;
import com.readile.readile.models.book.Category;
import com.readile.readile.repositories.BookCategoryRepository;
import com.readile.readile.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCategoryService implements CrudService<BookCategory> {

    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Override
    public BookCategory save(BookCategory entity) {
        return bookCategoryRepository.save(entity);
    }

    @Override
    public BookCategory update(BookCategory entity) {
        return bookCategoryRepository.save(entity);
    }

    @Override
    public void delete(BookCategory entity) {
        bookCategoryRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        bookCategoryRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<BookCategory> entities) {
        bookCategoryRepository.deleteAllInBatch(entities);
    }

    @Override
    public BookCategory findById(Long id) {
        return bookCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<BookCategory> findAll() {
        return bookCategoryRepository.findAll();
    }

    public List<BookCategory> findAllByCategory(Category category) {
        return bookCategoryRepository.findAllByCategory(category);
    }

    public List<BookCategory> findAllByBook(Book book) {
        return bookCategoryRepository.findAllByBook(book);
    }

    public void deleteAllByBook(Book book) {
        bookCategoryRepository.deleteAllByBook(book);
    }
}
