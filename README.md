# Bloogie backend

This is my personal project for practicing reactive backend web development. Bloogie backend is reactive RESTfull web service implemented with Java 8 and Spring Webflux. It serves `Account`, `Blog` and `Post` resources from following functional endpoints:

    POST /login - authenticate with Basic authentication

    GET /{basePath}/{id} - fetch resource by id
    PUT /{basePath}/{id} - update existing resource
    DELETE /{basePath}/{id} - delete resource by id
    GET /{basePath} - fetch all resources
    POST /{basePath} - create new resource

The *{basePath}* is determined by resource as follows:

    /accounts - for Account resources
    /blogs - for Blog resources
    /posts - for Post resource

Domain objects are defined in path *src/main/java/bloogie/backend/domain*.

## Implementation

Application is implemented with Java 8 and Spring framework (Spring Boot, Spring WebFlux, Spring Security and Spring Data MongoDB). It uses reactive mongo repositories, which can be configured in `ReactiveMongoConfiguration` class and in application.properties file. With current settings application uses local mongo database.

API endpoints authenticate with Basic authentication. Requests to protected endpoints should have authorization header (`Authorization: {base64token}`) matching existing user. Security settings can be configured in `DevelopmentSecurityConfiguration` class.

Jackson JSON Views is used for serializing the domain objects to server response (removing password from Account domain object etc).

## System requirements

- Java 8+
- Maven
- Local MongoDB (or define remote mongoDB in application.properties file)

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

List of things to do in some distant future:

- More tests; tests covers only router and handler functions
- Expanding domain to have more interaction (Comments, Likes, Pictures ...)
- Better validations for domain objects
- Authorization for PUT and DELETE endpoints, allowing handle only resources you own
- Security settings; cors policies, csrf security, https etc.