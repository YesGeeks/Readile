[![GitHub issues](https://img.shields.io/github/issues/YesGeeks/Readile?style=flat-square)](https://github.com/YesGeeks/Readile/issues)
[![GitHub forks](https://img.shields.io/github/forks/YesGeeks/Readile?style=flat-square)](https://github.com/YesGeeks/Readile/network)
![GitHub closed issues](https://img.shields.io/github/issues-closed-raw/YesGeeks/Readile?color=red&style=flat-square)
![GitHub pull requests](https://img.shields.io/github/issues-pr/YesGeeks/Readile?color=lightblue&style=flat-square)
![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed-raw/YesGeeks/Readile?color=light%20green&style=flat-square)
[![GitHub stars](https://img.shields.io/github/stars/YesGeeks/Readile?style=flat-square)](https://github.com/YesGeeks/Readile/stargazers)
![GitHub contributors](https://img.shields.io/github/contributors/YesGeeks/Readile?style=flat-square)
![Lines of code](https://img.shields.io/tokei/lines/github/YesGeeks/Readile?style=flat-square)

<img src="https://user-images.githubusercontent.com/46399191/165711330-14a2b271-e3ef-4e01-91fc-b18d75a62a6e.png" width="150" alt="Readile">

# Readile 
A simple yet powerful desktop application that tracks users’ book reading activity and allows them to create reading goals.

<div align="center">
<img style="float: right;"  src="https://user-images.githubusercontent.com/46399191/219680603-9c49d57f-4504-4d3b-aa0f-6073de9a7fe8.png" width="400" alt="Readile"/>
<p><b>Readile</b> bridges the gap between  user readings and their reading habits, allowing them to track their readings effortlessly</p>
</div>

## User Requirements
<div>
<img align="left" src="https://user-images.githubusercontent.com/46399191/219682130-624d6888-6206-4f08-9f8b-f2e021bcc309.png" width="220" alt="Readile"/>
<ul>
<br>
<li>As a user, I should be able to search for a particular book</li>
<li>As a user, I should be able to track a particular book</li>
<li>As a user, I should be able to categorize my books</li>
<li>As a user, I should be able to delete a book</li>
<li>As a user, I should be able to rate a book</li>
<li>...</li>
</ul>
<br>
</div>

## Prototype & UI
* The prototype is designed using [Figma](https://www.figma.com/).
* Each scene was designed separately using [Scene Builder](https://gluonhq.com/products/scene-builder/).

![image](https://user-images.githubusercontent.com/46399191/219687323-2cbd0e0c-5cd8-4d44-9dcc-170585c72c3c.png)

## Database ER Diagram ([SQL Schema](https://github.com/YesGeeks/Readile/blob/develop/src/main/resources/database/schema.sql))
![readile](https://user-images.githubusercontent.com/46399191/219695227-909ea57e-7901-430c-ac8e-679782132af1.png)

## System Design & Architecture
Utilized the MVC design pattern

![image](https://user-images.githubusercontent.com/46399191/219697667-616bea75-5d09-487a-8c16-fb83a15e5833.png)

## Git Workflow
* Adopted the popular git feature branch based workflow.
* Each feature is developed in its own branch and a corresponding pull request is opened when it is finished.
<img src="https://user-images.githubusercontent.com/46399191/219787213-242dd00f-8df5-469b-85e4-a8918ab412af.png" width="400" alt="main - develop"/>

## CRUD Operations
* At this stage, we faced a serious issue in integrating the spring boot framework with javafx.
* The problem was solved by using the [JavaFX Waver](https://github.com/rgielen/javafx-weaver) library.
* The Repository design pattern from Spring Data JPA is used to implement basic CRUD operations.

<img src="https://user-images.githubusercontent.com/46399191/219749396-58929641-d674-4b15-a922-3aae6c8a69b5.png" width="400" alt="Interface"/>

## Used Libraries & Technologies
<div align="center">
<img src="https://user-images.githubusercontent.com/46399191/219760552-5ffe6d05-80d4-498d-8ffc-fed1e78fffbc.png" width="400" alt="Technologies"/>
</div>

- [`JavaFX`](https://openjfx.io/), [`JavaFX Waver`](https://github.com/rgielen/javafx-weaver), [`JFoenix`](https://github.com/sshahine/JFoenix), [`MaterialFX`](https://github.com/palexdev/MaterialFX), [`ControlsFX`](https://github.com/controlsfx/controlsfx), [`JavaSystemThemeDetector`](https://github.com/Dansoftowner/jSystemThemeDetector)
- [`Spring Boot`](https://spring.io/projects/spring-boot), [`Spring Security`](https://spring.io/projects/spring-security)
- [`MySQL DBMS`](https://www.mysql.com/), [`JDBC`](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html), [`Hibernate JPA`](https://hibernate.org/orm/), [`Spring Data JPA`](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [`Lombok`](https://projectlombok.org/), [`Google API's`](https://developers.google.com/apis-explorer), [`OAuth2`](https://developers.google.com/identity/protocols/oauth2), [`Open Library API`](https://openlibrary.org/developers/api), [`Unsplash API`](https://unsplash.com/developers)

## Screenshots
![Screenshot2](https://user-images.githubusercontent.com/46399191/219766772-eacca78e-727a-4d68-bd82-f67527b564cc.png)
![Screenshot1](https://user-images.githubusercontent.com/46399191/219766790-ec486508-5857-421a-8692-5bc8ea5ad71a.png)
![Screenshot3](https://user-images.githubusercontent.com/46399191/219766777-a3e5307a-e9c7-48a8-a340-fbf29be0eb55.png)
![Screenshot4](https://user-images.githubusercontent.com/46399191/219766782-a373fb8f-1028-4a2f-8b32-b334746a006a.png)

## Guides
The following guides illustrate how to use some features concretely:
* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Using WebSocket to build an interactive web application](https://spring.io/guides/gs/messaging-stomp-websocket/)

## Contact Details
Feel free to reach out to us at: readileapp@gmail.com

<i>© 2023 Readile</i>
