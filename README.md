# Bloogie backend

This is my personal project for practicing web development. Bloogie backend is simple reactive RESTfull web service implemented with Java 8 and Spring Webflux. It serves `Account` and `Blog` resources from following functional endpoints:

    GET /account/{id} - fetch account with id={id}
    GET /account - fetch all accounts
    POST /account - create new account

    POST /login - authenticate with Bsic authentication

    GET /blog - fetch all blogs
    POST /blog - create new blog

I have also created a blog service upon this backend, and it can be viewed here: [bloogie-react](https://github.com/MiguelSombrero/bloogie-react).

## Implementation

Application is implemented with Java 8 and Spring framework (Spring Boot, Spring WebFlux, Spring Security and Spring Data MongoDB).

### Authentication

Authentication is achieved with Spring Security and Basic authentication. Requests to protected endpoints should have authorization header (`Authorization: base64token`) matching existing user. Security settings can be configured in `DevelopmentSecurityConfiguration` class.

### Persistence of data

Application uses reactive mongo repositories, which can be configured via `ReactiveMongoConfiguration` class and application-{profile}.properties file. With current settings application uses local mongo database.

## System requirements

Application requires Java 8+. It also requires local mongoDB installation OR you have to define remote mongoDB service in application-{profile}.properties file. Maven is required for managing the project.

## Installation

Clone project to your machine

    git clone https://github.com/MiguelSombrero/bloogie-backend.git
    cd bloogie-backend

Run application in you favourite IDE or in commandline

    mvn spring-boot:run

Create executable jar-file

    mvn package

Run executable jar-file

    java -jar ./target/BloogieBackend-1.0-SNAPSHOT.ja


## Todo

- Unit tests covers now only router functions. No integration tests are implemented
- Validation of Account and Blog object could be more comprehensive
- Currently service provides only `Account` and `Blog` resources. With real-life blog service, it would be nice to have comments and some other interaction too, likes maybe

When I am student or unemployed at next time, I promise to fix these :)