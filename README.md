# Museum of Interesting Things

### Problem Statement

The Institute of Interesting Things is a museum of interesting  
things that are exposed. Many things must be tracked such as:
* data regarding item
* distinguish whether item was lost or not
* distinguish whether item is damaged or not
* location of each item
* employee's data and his/her phone number(s)
* all appraise values of an item that where done by museum's employee(s)

In addition, the museum wants to generate reoports regarding item(s). Also, items/employees suppose to be easily modified by admin.

### Solution
CRUD web application with which managing items and employees would be easy.


### Project Objectives

* Provide documentation on all services to ensure ease of use for end user. ?
* Create a web application to display museum items data and employee data.
* CRUD operations as admin to manage items and employees data/entries

## Project Technologies

* Security/Authentication
    * Tomcat's JDBC Realm Authentication
* Database
    * MySQL 8.0.22
* ORM Framework
    * Hibernate 5
* Build Tool & Dependency Management
    * Maven
* CSS
    * Bootstrap
* Logging
    * Log4J2
* Unit Testing
    * JUnit tests to achieve covergae on most business logic methods and repository class(dao)
* IDE: IntelliJ IDEA
* Spring Framework
    * Spring Security
    * Spring MVC
    * Spring DI & IoC


#### Pages and functionality:
###### Index Page
* Home page with exposed items
* employee's items
* lost items
* admin functionality with CRUD for items
* admin functionality with CRUD for employees
* admin functionality to generate reports



### Project Design Documents
* [Link to the journal](Journal.md)
* [Link to the detailed specifications](gereral_ER_model_specifications.docx)