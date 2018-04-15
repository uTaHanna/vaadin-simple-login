## A Very Simple Vaadin Login Demo

### Technologies Used:

* Java (1.8.0_144)
* Maven (3.3.9)
* MySQL (5.7.19)
* Spring Boot
* Vaadin

### Preliminary Remarks

This small application shows login authentication, using Spring BCrypt, within the framework of<br> 
Spring Boot and Vaadin. The design and implementation owe to several tutorials and other sources<br>
as acknowledged in the references section below. In particular, if you are interested in building a<br>
similar application by yourself, I would suggest starting with [Alejandro's introductory Vaadin tutorial](https://vaadin.com/blog/building-a-web-ui-for-mysql-databases-in-plain-java-),<br>
but you need to add Spring Security for BCrypt when generating a project template by<br>
Spring Initializr. I also listed sources, including those on MySQL and Spring, which might help you<br>
in starting your own project. 


### How to Run the Application

1. Start MySQL and create a new database called "simple_login".
1. Add a new table called "user" as follows:<br>
CREATE TABLE user(user_name VARCHAR(50) PRIMARY KEY, email VARCHAR(50) NOT NULL, password VARCHAR(255) NOT NULL);<br>
1. Edit the application.properties file in the resources directory of the source code by adding<br>
your MySQL credentials (the port in the file is the default of MySQL).<br>
1. Build the source code with Maven and then run the project jar file generated.<br>
Alternatively, a simpler way is to just open the source code on IntelliJ and click "Run".<br>
1. Once Tomcat starts, go to localhost:8080/simple_login and click "New User?" to sign up.<br>
1. After registering the new user, sign in with the credentials.<br>

For sign-up,<br>

![sign_up](/md_images/sign_up.PNG)

For sign-in,<br>

![sign_in](/md_images/sign_in.PNG)

![main](/md_images/main.PNG)

If you do SQL SELECT on the MySQL shell console, you will see that the password is in fact encrypted.<br>

![password_hashing](/md_images/password_hashing.PNG)

### References

For the basic design, Alejandro Duarte's Vaadin tutorials:<br>
https://vaadin.com/blog/implementing-remember-me-with-vaadin<br>
https://vaadin.com/blog/building-a-web-ui-for-mysql-databases-in-plain-java-<br>

For a more detailed introduction to Vaadin Binder, Kirill Bulatov's tutorial:<br>
https://vaadin.com/blog/15624687<br>

For use of view navigation and Spring beans in Vaadin, their documentation and tutorial:<br>
https://vaadin.com/docs/v8/framework/application/application-architecture.html<br>
https://vaadin.com/docs/v8/framework/advanced/advanced-navigator.html#advanced.navigator<br>
https://vaadin.com/docs/v8/framework/articles/AccessControlForViews.html<br>
https://vaadin.github.io/spring-tutorial/

For UI styling,
https://vaadin.com/docs/v8/framework/themes/themes-overview.html<br>
https://vaadin.com/docs/v8/framework/themes/themes-creating.html  

For Spring configuration of authentication,<br>
eparvan's post: https://stackoverflow.com/questions/42562893/could-not-autowire-no-beans-of-userdetailservice-type-found<br>
Roland Ewald's post: https://stackoverflow.com/questions/35912404/spring-boot-security-with-vaadin-login<br>
Eugen Paraschiv's tutorial: http://www.baeldung.com/spring-security-registration-password-encoding-bcrypt<br>
Spring documentation: https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html

 
### Bibliography

Vaadin YouTube channel:<br>
https://www.youtube.com/user/vaadinofficial/videos

A very helpful first tutorial on MySQL by Chua:<br>
https://www.ntu.edu.sg/home/ehchua/programming/sql/MySQL_HowTo.html

A relatively accessible introduction to Spring, worth reading before tackling<br>
the official documentation:<br>
Johnson, Rod. “J2EE development frameworks.” Computer 38.1 (2005): 107-110
  
  

   

   
   

           
               

          

