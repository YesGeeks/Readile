package com.readile.readile.services.implementation;

import com.readile.readile.models.userbook.UserBook;
import com.readile.readile.repositories.UserBookRepository;
import com.readile.readile.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBookService implements CrudService<UserBook> {
    @Autowired
    private UserBookRepository userBookRepository;
    
    @Override
    public UserBook save(UserBook entity) {
        return userBookRepository.save(entity);
    }

    @Override
    public UserBook update(UserBook entity) {
        return userBookRepository.save(entity);
    }

    @Override
    public void delete(UserBook entity) {
        userBookRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        userBookRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<UserBook> entities) {
        userBookRepository.deleteAllInBatch(entities);
    }

    @Override
    public UserBook findById(Long id) {
        return userBookRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserBook> findAll() {
        return userBookRepository.findAll();
    }
}