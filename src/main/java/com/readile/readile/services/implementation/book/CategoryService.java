package com.readile.readile.services.implementation.book;

import com.readile.readile.models.book.category.Category;
import com.readile.readile.models.user.User;
import com.readile.readile.repositories.CategoryRepository;
import com.readile.readile.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Configurable
public class CategoryService implements CrudService<Category> {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category save(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public Category update(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public void delete(Category entity) {
        categoryRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Category> entities) {
        categoryRepository.deleteAllInBatch(entities);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findByUser(User user) {
        return categoryRepository.findByUser(user);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }
}