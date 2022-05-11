package com.readile.readile.services.implementation;

import com.readile.readile.models.book.Author;
import com.readile.readile.repositories.AuthorRepository;
import com.readile.readile.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService implements CrudService<Author> {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author save(Author entity) {
        return authorRepository.save(entity);
    }

    @Override
    public Author update(Author entity) {
        return authorRepository.save(entity);
    }

    @Override
    public void delete(Author entity) {
        authorRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Author> entities) {
        authorRepository.deleteAllInBatch(entities);
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}