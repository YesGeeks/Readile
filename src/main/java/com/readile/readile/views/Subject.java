package com.readile.readile.views;

public interface Subject {
    void addSubscriber(Observer observer);
    void notify(boolean isDarkTheme);
}