package com.readile.readile.services.implementation.book;

import com.readile.readile.models.book.Book;
import com.readile.readile.models.book.Highlight;
import com.readile.readile.repositories.HighlightRepository;
import com.readile.readile.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HighlightService implements CrudService<Highlight> {
    @Autowired
    HighlightRepository highlightRepository;

    @Override
    public Highlight save(Highlight entity) {
        return highlightRepository.save(entity);
    }

    @Override
    public Highlight update(Highlight entity) {
        return highlightRepository.save(entity);
    }

    @Override
    public void delete(Highlight entity) {
        highlightRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        highlightRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Highlight> entities) {
        highlightRepository.deleteAllInBatch(entities);
    }

    @Override
    public Highlight findById(Long id) {
        return highlightRepository.findById(id).orElse(null);
    }

    @Override
    public List<Highlight> findAll() {
        return highlightRepository.findAll();
    }

    public List<Highlight> findByBook(Book book) {
        return highlightRepository.findByBook(book);
    }
}