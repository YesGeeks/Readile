package com.readile.readile.utils;

import java.util.List;

public class ResultBook {

    private Integer id;
    private String name;
    private String coverURL;
    private Integer length;
    private List<String> authorNames;


    public ResultBook() {
    }

    public ResultBook(String name, String coverURL, Integer length, List<String> authorNames) {
        this.name = name;
        this.coverURL = coverURL;
        this.length = length;
        this.authorNames = authorNames;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    @Override
    public String toString() {
        return "ResultBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coverURL='" + coverURL + '\'' +
                ", length=" + length +
                ", authorNames=" + authorNames +
                '}';
    }
}