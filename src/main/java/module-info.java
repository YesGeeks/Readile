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
    requires com.jfoenix;
    requires spring.boot.autoconfigure;
    requires org.slf4j;

    opens com.readile.readile;

    opens com.readile.readile.views to javafx.fxml, spring.core;
    opens com.readile.readile.controllers to javafx.fxml;
    opens com.readile.readile.models.userbook to org.hibernate.orm.core, spring.core;
    opens com.readile.readile.models.book to org.hibernate.orm.core, spring.core;
    opens com.readile.readile.models.user to org.hibernate.orm.core, spring.core;
    exports com.readile.readile.views;
    exports com.readile.readile.controllers to spring.beans;
    exports com.readile.readile.services.implementation to spring.beans;
    opens com.readile.readile.services.implementation to spring.core;
}