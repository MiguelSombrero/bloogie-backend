# Bloogie backend

This is my personal project for practicing reactive web development. Bloogie backend is reactive RESTfull web service implemented with Java 8 and Spring Webflux. It serves `Account`, `Blog` and `Post` resources from following functional endpoints:

    POST /login - authenticate with Basic authentication

    GET /{basePath}/{id} - fetch resource by id
    PUT /{basePath}/{id} - update existing resource
    DELETE /{basePath}/{id} - delete resource by id
    GET /{basePath} - fetch all resources
    POST /{basePath} - create new resource

Here the *{basePath}* is determined by resource as follows:

    /accounts - for Account resources
    /blogs - for Blog resources
    /posts - for Post resource

## Implementation

Application is implemented with Java 8 and Spring framework (Spring Boot, Spring WebFlux, Spring Security and Spring Data MongoDB).

### Authentication

Authentication is achieved with Spring Security and Basic authentication. Requests to protected endpoints should have authorization header (`Authorization: base64token`) matching existing user. Security settings can be configured in `DevelopmentSecurityConfiguration` class.

### Persistence of data

Application uses reactive mongo repositories, which can be configured via `ReactiveMongoConfiguration` class and application-{profile}.properties file. With current settings application uses local mongo database.

## System requirements

Application requires Java 8+. It also requires local mongoDB installation OR you have to define remote mongoDB service in application-{profile}.properties file. Maven is required for managing and building the project.

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
- Validations object could be more comprehensive
- With real-life blog service, it would be nice to have more interaction. At least `Comment` and `Like` model objects would be nice