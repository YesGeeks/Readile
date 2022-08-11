package com.readile.readile.repositories;

import com.readile.readile.models.book.category.Category;
import com.readile.readile.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
    Category findByName(String name);
}