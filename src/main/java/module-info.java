module com.readile.readile {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires AnimateFX;

    requires mysql.connector.java;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires spring.data.jpa;
    requires spring.boot;
    requires spring.core;
    requires spring.context;
    requires spring.beans;
    requires spring.boot.autoconfigure;

    opens com.readile.readile;

    opens com.readile.readile.views to javafx.fxml;
    exports com.readile.readile.views;
}