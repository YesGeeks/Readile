package com.readile.readile.models.book;

import java.util.Objects;

public class Category {
    private Integer category_id;
    private String name;

    public Category() {
    }

    public Category(Integer category_id, String category_name) {
        this.category_id = category_id;
        this.name = category_name;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return name;
    }

    public void setCategory_name(String category_name) {
        this.name = category_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(category_id, category.category_id) && name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category_id, name);
    }
}