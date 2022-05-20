package com.readile.readile.services.implementation;

import com.readile.readile.models.book.Author;
import com.readile.readile.models.book.AuthorBook;
import com.readile.readile.models.book.AuthorBookId;
import com.readile.readile.models.book.Book;
import com.readile.readile.models.user.User;
import com.readile.readile.models.userbook.UserBook;
import com.readile.readile.models.userbook.UserBookId;
import com.readile.readile.repositories.AuthorBookRepository;
import com.readile.readile.repositories.UserBookRepository;
import com.readile.readile.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorBookService implements CrudService<AuthorBook> {
    @Autowired
    private AuthorBookRepository authorBookRepository;

    @Override
    public AuthorBook save(AuthorBook entity) {
        return authorBookRepository.save(entity);
    }

    @Override
    public AuthorBook update(AuthorBook entity) {
        return authorBookRepository.save(entity);
    }

    @Override
    public void delete(AuthorBook entity) {
        authorBookRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        authorBookRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<AuthorBook> entities) {
        authorBookRepository.deleteAllInBatch(entities);
    }

    @Override
    public AuthorBook findById(Long id) {
        return authorBookRepository.findById(id).orElse(null);
    }

    @Override
    public List<AuthorBook> findAll() {
        return authorBookRepository.findAll();
    }

    public List<AuthorBook> findAllByAuthor(Author author) {
        return authorBookRepository.findAllByAuthor(author);
    }

    public List<AuthorBook> findAllByBook(Book book) {
        return authorBookRepository.findAllByBook(book);
    }


    public AuthorBook findById(AuthorBookId id) {
        return authorBookRepository.findById(id);
    }
}